package com.hart.overwatch.connectionpin.request;

public class CreateConnectionPinRequest {

    private Long pinnedId;

    private Long ownerId;

    private Long connectionId;


    public CreateConnectionPinRequest() {

    }

    public CreateConnectionPinRequest(Long pinnedId, Long ownerId, Long connectionId) {
        this.pinnedId = pinnedId;
        this.ownerId = ownerId;
        this.connectionId = connectionId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getPinnedId() {
        return pinnedId;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setPinnedId(Long pinnedId) {
        this.pinnedId = pinnedId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }
}
