package com.hart.overwatch.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class ConnectionService {


    private final ConnectionRepository connectionRepository;

    private final UserService userService;

    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository, UserService userService) {
        this.connectionRepository = connectionRepository;
        this.userService = userService;
    }

    private Connection getConnetionById(Long connectionId) {
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
}
