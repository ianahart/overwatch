package com.hart.overwatch.connection;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.blockuser.BlockUserService;
import com.hart.overwatch.chatmessage.ChatMessage;
import com.hart.overwatch.connection.dto.ConnectionDto;
import com.hart.overwatch.connection.dto.MinConnectionDto;
import com.hart.overwatch.connectionpin.ConnectionPinService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;

@Service
public class ConnectionService {


    private final ConnectionRepository connectionRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    private final ConnectionPinService connectionPinService;

    private final BlockUserService blockUserService;

    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository, UserService userService,
            PaginationService paginationService, @Lazy ConnectionPinService connectionPinService,
            BlockUserService blockUserService) {
        this.connectionRepository = connectionRepository;
        this.userService = userService;
        this.paginationService = paginationService;
        this.connectionPinService = connectionPinService;
        this.blockUserService = blockUserService;
    }

    public Connection getConnectionById(Long connectionId) {
        return this.connectionRepository.findById(connectionId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Connection with the id %d was not found", connectionId)));
    }

    private boolean checkForExistingConnection(Long receiverId, Long senderId) {
        return this.connectionRepository.findExistingConnection(receiverId, senderId);
    }

    public void createConnection(Long receiverId, Long senderId) {
        try {

            boolean existingConnection = checkForExistingConnection(receiverId, senderId);

            if (existingConnection) {
                throw new BadRequestException("You have already sent a connection request");
            }

            User receiver = this.userService.getUserById(receiverId);
            User sender = this.userService.getUserById(senderId);


            Connection connection = new Connection(RequestStatus.PENDING, sender, receiver);


            this.connectionRepository.save(connection);

        } catch (DataIntegrityViolationException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private Connection getConnectionBySenderIdAndReceverId(Long senderId, Long receiverId) {
        try {
            return this.connectionRepository.findBySenderIdAndReceiverId(senderId, receiverId);

        } catch (DataAccessException ex) {
            throw ex;
        }
    }

    public void deleteConnection(Long senderId, Long receiverId) {

        Connection connection = getConnectionBySenderIdAndReceverId(senderId, receiverId);


        if (connection != null) {
            this.connectionRepository.delete(connection);

        }
    }

    public void updateConnectionStatus(Long senderId, Long receiverId, RequestStatus status) {

        Connection connection = getConnectionBySenderIdAndReceverId(senderId, receiverId);

        connection.setStatus(status);

        this.connectionRepository.save(connection);

    }

    public MinConnectionDto verifyConnection(Long senderId, Long receiverId) {
        Connection connection = getConnectionBySenderIdAndReceverId(senderId, receiverId);

        if (connection == null) {
            return new MinConnectionDto(0L, RequestStatus.UNINITIATED);
        }

        return new MinConnectionDto(connection.getId(), connection.getStatus());
    }


    public void deleteConnectionById(Long connectionId) {
        try {
            Connection connection = getConnectionById(connectionId);

            this.connectionRepository.delete(connection);

        } catch (DataAccessException ex) {
            throw ex;
        }
    }


    private List<ConnectionDto> connectMessages(List<ConnectionDto> connections,
            Long currentUserId) {
        for (ConnectionDto connection : connections) {
            Connection connectionEntity = getConnectionById(connection.getId());
            try {
                ChatMessage lastMessage = connectionEntity.getChatMessages().stream()
                        .filter(v -> v.getUser().getId() != currentUserId).toList().get(0);
                connection.setLastMessage(lastMessage.getText());

            } catch (IndexOutOfBoundsException ex) {
                connection.setLastMessage("");
            }

        }

        return connections;
    }

    public PaginationDto<ConnectionDto> getAllConnections(Long userId, int page, int pageSize,
            String direction, String override) {

        try {
            User user = this.userService.getUserById(userId);

            if (user.getId() != this.userService.getCurrentlyLoggedInUser().getId()) {
                throw new ForbiddenException("Cannot load another user's connections");
            }

            Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

            Page<ConnectionDto> queryResult = null;

            List<Long> pinnedConnectionIds =
                    this.connectionPinService.getOwnerConnectionPins(user.getId());

            List<Long> blockedUserIds = null;
            List<Long> potentialBlockedUserIds = blockUserService.getBlockedUsersForCurUser(user);

            if (potentialBlockedUserIds.size() > 0) {
                blockedUserIds = potentialBlockedUserIds;
            }


            if (user.getRole() == Role.REVIEWER) {

                queryResult = pinnedConnectionIds.size() == 0 || override.equals("true")
                        ? this.connectionRepository.getReceiverConnectionsWithoutPins(pageable,
                                userId, blockedUserIds)
                        : this.connectionRepository.getReceiverConnections(pageable, userId,
                                pinnedConnectionIds, blockedUserIds);
            } else {
                queryResult = pinnedConnectionIds.size() == 0 || override.equals("true")
                        ? this.connectionRepository.getSenderConnectionsWithoutPins(pageable,
                                userId, blockedUserIds)
                        : this.connectionRepository.getSenderConnections(pageable, userId,
                                pinnedConnectionIds, blockedUserIds);
            }

            List<ConnectionDto> connections = connectMessages(queryResult.getContent(), userId);

            return new PaginationDto<ConnectionDto>(connections, queryResult.getNumber(), pageSize,
                    queryResult.getTotalPages(), direction, queryResult.getTotalElements());

        } catch (DataAccessException ex) {
            throw ex;
        }
    }


    public PaginationDto<ConnectionDto> getAllSearchConnections(String query, int page,
            int pageSize, String direction) {

        try {
            User user = this.userService.getCurrentlyLoggedInUser();


            Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

            Page<ConnectionDto> queryResult = null;

            String searchQuery = "%" + query.toLowerCase() + "%";

            List<Long> blockedUserIds = null;
            List<Long> potentialBlockedUserIds = blockUserService.getBlockedUsersForCurUser(user);

            if (potentialBlockedUserIds.size() > 0) {
                blockedUserIds = potentialBlockedUserIds;
            }

            if (user.getRole() == Role.REVIEWER) {
                queryResult = this.connectionRepository.getSearchReceiverConnections(pageable,
                        user.getId(), searchQuery, blockedUserIds);
            } else {
                queryResult = this.connectionRepository.getSearchSenderConnections(pageable,
                        user.getId(), searchQuery, blockedUserIds);
            }

            List<ConnectionDto> connections =
                    connectMessages(queryResult.getContent(), user.getId());

            return new PaginationDto<ConnectionDto>(connections, queryResult.getNumber(), pageSize,
                    queryResult.getTotalPages(), direction, queryResult.getTotalElements());

        } catch (DataAccessException ex) {
            throw ex;
        }



    }



}
