export enum Role {
  USER = 'USER',
  REVIEWER = 'REVIEWER',
  ADMIN = 'ADMIN',
  UNASSIGNED = '',
}

export enum NotificationType {
  CONNECTION_REQUEST_PENDING = 'CONNECTION_REQUEST_PENDING',
  CONNECTION_REQUEST_ACCEPTED = 'CONNECTION_REQUEST_ACCEPTED',
  PAYMENT_ACKNOWLEDGEMENT = 'PAYMENT_ACKNOWLEDGEMENT',
  REVIEW_INCOMPLETE = 'REVIEW_INCOMPLETE',
  REVIEW_INPROGRESS = 'REVIEW_INPROGRESS',
  REVIEW_COMPLETED = 'REVIEW_COMPLETED',
  COMMENT_REPLY = 'COMMENT_REPLY',
  TAG = 'TAG',
}

export enum NotificationRole {
  SENDER = 'SENDER',
  RECEIVER = 'RECEIVER',
}

export enum RequestStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  DECLINED = 'DECLINED',
  UNINITIATED = 'UNINITIATED',
}

export enum ERepositoryView {
  DETAILS = 'DETAILS',
  CODE = 'CODE',
}
