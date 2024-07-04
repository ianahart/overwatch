package com.hart.overwatch.connectionpin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.connection.Connection;
import com.hart.overwatch.connection.ConnectionService;
import com.hart.overwatch.connectionpin.dto.ConnectionPinDto;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;

@Service
public class ConnectionPinService {

    private Long MAX_PINS = 3L;

    private final ConnectionPinRepository connectionPinRepository;

    private final ConnectionService connectionService;

    private final UserService userService;

    @Autowired
    public ConnectionPinService(ConnectionPinRepository connectionPinRepository,
            ConnectionService connectionService, UserService userService) {
        this.connectionPinRepository = connectionPinRepository;
        this.connectionService = connectionService;
        this.userService = userService;
    }


    public ConnectionPin getConnectionPinById(Long connectionPinId) {
        return this.connectionPinRepository.findById(connectionPinId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Cannot find connection pin with the id %d", connectionPinId)));
    }

    private Long countOwnerConnectionPins(Long ownerId) {
        try {
            return this.connectionPinRepository.countConnectionPinsByOwnerId(ownerId);


        } catch (DataAccessException ex) {
            return MAX_PINS;
        }
    }

    private boolean alreadyPinnedConnection(Long ownerId, Long pinnedId, Long connectionId) {
        try {
            return this.connectionPinRepository.connectionPinAlreadyExists(ownerId, pinnedId,
                    connectionId);
        } catch (DataAccessException ex) {
            return true;
        }
    }


    public List<Long> getOwnerConnectionPins(Long ownerId) {
        List<ConnectionPin> connectionPins =
                this.connectionPinRepository.findConnectionPinByOwnerId(ownerId);

        return connectionPins.stream().map(cp -> cp.getConnection().getId()).toList();

    }

    public void createConnectionPin(Long ownerId, Long pinnedId, Long connectionId) {

        if (countOwnerConnectionPins(ownerId) >= MAX_PINS) {
            throw new BadRequestException("You have pinned the max amount of connections");
        }

        if (alreadyPinnedConnection(ownerId, pinnedId, connectionId)) {
            throw new BadRequestException("You have already pinned this connection");
        }

        User owner = this.userService.getUserById(ownerId);
        User pinned = this.userService.getUserById(pinnedId);
        Connection connection = this.connectionService.getConnectionById(connectionId);

        ConnectionPin connectionPin = new ConnectionPin(connection, owner, pinned);

        this.connectionPinRepository.save(connectionPin);
    }

    public List<ConnectionPinDto> getConnectionPins(Long ownerId) {
        Pageable pageable = PageRequest.of(0, MAX_PINS.intValue());
        Page<ConnectionPinDto> result =
                this.connectionPinRepository.getConnectionPinsByOwnerId(ownerId, pageable);
        result.getContent().stream().forEach(v -> v.setLastMessage(""));
        return result.getContent();

    }


    public void deletePinnedConnection(Long connectionPinId) {
        try {
            ConnectionPin connectionPin = getConnectionPinById(connectionPinId);

            this.connectionPinRepository.delete(connectionPin);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
