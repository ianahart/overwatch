import { NotificationRole, NotificationType, RequestStatus, Role } from '../enums';

export interface IWorkSpaceState {
  workSpace: IWorkSpaceEntity;
  todoLists: string[];
}

export interface IWorkSpaceEntity {
  id: number;
  createdAt: string;
  userId: number;
  title: string;
  backgroundColor: string;
}

export interface IProgressMapper {
  INCOMPLETE: { text: string; background: string };
  INPROGRESS: { text: string; background: string };
  COMPLETED: { text: string; background: string };
}

export interface ILanguageMap {
  [key: string]: string;
  js: string;
  jsx: string;
  ts: string;
  tsx: string;
  java: string;
  css: string;
  py: string;
  html: string;
  php: string;
}

export interface ISelectedReviewer {
  reviewer: IConnection;
}

export interface IGitHubRepository {
  avatarUrl: string;
  comment: string;
  createdAt: string;
  feedback: string;
  id: number;
  language: string;
  ownerId: number;
  repoName: string;
  repoUrl: string;
  reviewerId: number;
  status: string;
  updatedAt: string;
}

export interface IGitHubTree {
  path: string;
  sha: string;
  size: number;
  type: string;
  url: string;
}

export interface IRepositoryReview {
  avatarUrl: string;
  createdAt: string;
  firstName: string;
  id: number;
  language: string;
  lastName: string;
  ownerId: number;
  profileUrl: string;
  repoName: string;
  repoUrl: string;
  reviewerId: number;
  status: string;
}

export interface IGitHubRepositoryPreview {
  id: number;
  fullName: string;
  avatarUrl: string;
  htmlUrl: string;
  language: string;
  stargazersCount: number;
}

export interface IConnection {
  senderId: number;
  receiverId: number;
  phoneNumber: string;
  lastName: string;
  id: number;
  firstName: string;
  email: string;
  country: string;
  city: string;
  bio: string;
  avatarUrl: string;
  lastMessage: string;
}

export interface IPinnedConnection extends IConnection {
  connectionPinId: number;
}

export interface IMessage {
  firstName: string;
  lastName: string;
  createdAt: string;
  text: string;
  avatarUrl: string;
  id: number;
  connectionId: number;
  userId: number;
}

export interface ITestimonial {
  id: number;
  userId: number;
  name: string;
  text: string;
  createdAt: string;
}

export interface INotification {
  id: number;
  createdAt: string;
  text: string;
  receiverId: number;
  senderId: number;
  avatarUrl: string;
  notificationType: NotificationType;
  notificationRole: NotificationRole;
}

export interface IPaginationState {
  page: number;
  pageSize: number;
  totalElements: number;
  direction: string;
  totalPages: number;
}

export interface ISavePaymentForm {
  firstName: { name: string; value: string; error: string; type: string };
  lastName: { name: string; value: string; error: string; type: string };
  country: { name: string; value: string; error: string; type: string };
  city: { name: string; value: string; error: string; type: string };
  line1: { name: string; value: string; error: string; type: string };
  line2: { name: string; value: string; error: string; type: string };
  state: { name: string; value: string; error: string; type: string };
  postalCode: { name: string; value: string; error: string; type: string };
}

export interface ITimeSlot {
  startTime: string;
  endTime: string;
  id: string;
}

export interface IDayAvailability {
  day: string;
  slots: ITimeSlot[];
}

export interface IPackageItem {
  [key: string]: string | number;
  id: string;
  name: string;
  isEditing: number;
}

export interface IPackage {
  price: string;
  description: string;
  items: IPackageItem[];
}

export interface IWorkExperience {
  id: string;
  title: string;
  desc: string;
}

export interface ITokens {
  token: string;
  refreshToken: string;
}

export interface IPhone {
  id: number;
  isVerified: boolean;
  createdAt: string;
  phoneNumber: string;
}

export interface ISetting {
  id: number;
  userId: number;
  mfaEnabled: boolean;
  createdAt: string;
}

export interface ISkillsFormField {
  name: string;
  id: string;
}

export interface ILanguage extends ISkillsFormField {}

export interface IProgrammingLanguage extends ISkillsFormField {}

export interface IQualification extends ISkillsFormField {}

export interface ILocationAddressResult {
  formatted: string;
  place_id: string;
  city: string;
  country: string;
  county: string;
  state: string;
  street: string;
  housenumber: string;
  zipCode: string;
}

export interface ILocation {
  address: string;
  addressTwo: string;
  city: string;
  country: string;
  state: string;
  zipCode: string;
  phoneNumber: string;
}

export interface IUser {
  abbreviation: string;
  avatarUrl: string;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  id: number;
  loggedIn: boolean;
  profileId: number;
  role: string;
  settingId: number;
  slug: string;
}

export interface IFormField<T> {
  [key: string]: string | T;
  name: string;
  value: T;
  error: string;
  type: string;
}

export interface IProfileSetupForm {
  avatar: IFormField<File | null | string>;
  tagLine: IFormField<string>;
  bio: IFormField<string>;
}

export interface ITestimonialForm {
  name: { name: string; value: string; error: string; type: string; max: number };
  text: { name: string; value: string; error: string; type: string; max: number };
}

export interface IBasicInfoForm {
  fullName: IFormField<string>;
  userName: IFormField<string>;
  email: IFormField<string>;
  contactNumber: IFormField<string>;
}

export interface IAccountForm {
  firstName: IFormField<string>;
  lastName: IFormField<string>;
  email: IFormField<string>;
}

export interface ILocationForm {
  address: IFormField<string>;
  addressTwo: IFormField<string>;
  city: IFormField<string>;
  country: IFormField<string>;
  phoneNumber: IFormField<string>;
  state: IFormField<string>;
  zipCode: IFormField<string>;
}

export interface ISignUpForm {
  firstName: IFormField<string>;
  lastName: IFormField<string>;
  email: IFormField<string>;
  password: IFormField<string>;
  confirmPassword: IFormField<string>;
  role: IFormField<Role>;
}

export interface ISignInForm {
  email: IFormField<string>;
  password: IFormField<string>;
}

export interface IDeleteAccountForm {
  email: IFormField<string>;
  password: IFormField<string>;
}

export interface IForgotPasswordForm {
  email: IFormField<string>;
}

export interface IPaymentMethod {
  id: string;
  city: string;
  country: string;
  line1: string;
  line2: string;
  postalCode: string;
  name: string;
  displayBrand: string;
  type: string;
  expMonth: number;
  expYear: number;
}

export interface IReview {
  id: number;
  authorId: number;
  avatarUrl: string;
  rating: number;
  review: string;
  createdAt: string;
  isEdited: boolean;
  name: string;
}

export interface IResetPasswordForm {
  password: IFormField<string>;
  confirmPassword: IFormField<string>;
}

export interface IChangePasswordForm {
  password: IFormField<string>;
  curPassword: IFormField<string>;
}

export interface ICompatibleProgrammingLanguage extends IProgrammingLanguage {
  isCompatible: boolean;
}

export interface IMinProfile {
  availability: IDayAvailability[];
  avatarUrl: string;
  country: string;
  fullName: string;
  id: number;
  programmingLanguages: ICompatibleProgrammingLanguage[];
  basic: IPackage;
  userId: number;
  numOfReviews: number;
  createdAt: string;
  weekendsAvailable: boolean;
  reviewAvgRating: number;
  isFavorited: boolean;
}

export interface ISignOut {
  refreshToken: string;
  token: string;
}

export interface IBaseResponse {
  message: string;
}

export interface ISignInResponse {
  token: string;
  refreshToken: string;
  user: IUser;
  userId?: number;
}

export interface IFetchSingleLocationResponse {
  message: string;
  data: ILocation;
}

export interface IFetchSingleLocationRequest {
  userId: number;
  token: string;
}

export interface ICreateLocationResponse {
  message: string;
}

export interface ICreateLocationRequest {
  token: string;
  userId: number;
  form: ILocationForm;
}

export interface IRefreshTokenResponse {
  token: string;
  refreshToken: string;
}

export interface IResetPasswordBody {
  token: string;
  passCode: string;
  password: string;
  confirmPassword: string;
}

export interface IUpdateUserPasswordRequest {
  form: IChangePasswordForm;
  userId: number;
  token: string;
}

export interface IUpdateUserRequest {
  form: IAccountForm;
  userId: number;
  token: string;
}

export interface IUpdateUserResponse {
  message: string;
  data: {
    firstName: string;
    lastName: string;
    email: string;
    abbreviation: string;
  };
}

export interface IUpdateSettingsMFARequest {
  mfaEnabled: boolean;
  token: string;
  settingId: number;
}

export interface IUpdateSettingsMFAResponse extends IBaseResponse {
  mfaEnabled: boolean;
}

export interface IFetchLocationsResponse {
  message: string;
  data: string;
}

export interface IFetchLocationsRequest {
  token: string;
  text: string;
}

export interface IFetchSettingsRequest {
  token: string;
  settingId: number;
}

export interface IFetchSettingsResponse {
  message: string;
  data: {
    id: number;
    userId: number;
    createdAt: Date;
    mfaEnabled: boolean;
  };
}

export interface ICreatePhoneRequest {
  token: string;
  phoneNumber: string;
  userId: number;
}

export interface IGetPhoneResponse {
  message: string;
  data: IPhone;
}
export interface IGetPhoneRequest {
  token: string;
  userId: number;
}

export interface IDeletePhoneRequest {
  token: string;
  phoneId: number;
}

export interface IVerifyOTPRequest {
  userId: number;
  otpCode: string;
}

export interface IDeleteUserRequest {
  userId: number;
  password: string;
  token: string;
}

export interface ICreateAvatarResponse {
  message: string;
  data: {
    avatarUrl: string;
  };
}

export interface ICreateAvatarRequest {
  token: string;
  profileId: number;
  formData: FormData;
}

export interface IRemoveAvatarRequest {
  token: string;
  profileId: number;
  avatarUrl: null;
  avatarFilename: null;
}

export interface IUpdateProfileRequest<T> {
  token: string;
  profileId: number;
  formData: T;
}

export interface IFetchProfileRequest {
  token: string;
  profileId: number;
}

export interface IBasicInfoResponse {
  fullName: string;
  userName: string;
  email: string;
  contactNumber: string;
}

export interface IAdditionalInfoResponse {
  availability: IDayAvailability[];
  moreInfo: string;
}

export interface IPckgResponse {
  basic: IPackage;
  standard: IPackage;
  pro: IPackage;
}

export interface IProfileSetupResponse {
  avatar: string;
  tagLine: string;
  bio: string;
}

export interface ISkillsResponse {
  languages: ISkillsFormField[];
  programmingLanguages: ISkillsFormField[];
  qualifications: ISkillsFormField[];
}

export interface IWorkExpResponse {
  workExps: IWorkExperience[];
}

export interface IUserProfileResponse {
  id: number;
  userId: number;
  role: string;
  country: string;
  abbreviation: string;
  city: string;
}

export interface IFetchProfileResponse {
  message: string;
  data: {
    basicInfo: IBasicInfoResponse;
    additionalInfo: IAdditionalInfoResponse;
    pckg: IPckgResponse;
    profileSetup: IProfileSetupResponse;
    skills: ISkillsResponse;
    workExp: IWorkExpResponse;
  };
}

export interface IFullProfile {
  userProfile: IUserProfileResponse;
  basicInfo: IBasicInfoResponse;
  additionalInfo: IAdditionalInfoResponse;
  pckg: IPckgResponse;
  profileSetup: IProfileSetupResponse;
  skills: ISkillsResponse;
  workExp: IWorkExpResponse;
}

export interface IFetchFullProfileResponse {
  message: string;
  data: IFullProfile;
}

export interface IFetchFullProfileRequest {
  profileId: number;
  token: string;
}

export interface ICreatePaymentMethodRequest {
  token: string;
  userId: number;
  body: IPaymentMethod;
}

export interface IGetPaymentMethodRequest {
  token: string;
  userId: number;
}

export interface IGetPaymentMethodResponse {
  message: string;
  data: {
    id: number;
    last4: string;
    displayBrand: string;
    expMonth: number;
    expYear: number;
    name: string;
  };
}

export interface IDeletePaymentMethodRequest {
  token: string;
  id: number;
}

export interface ICreateTestimonialRequest {
  token: string;
  userId: number;
  form: ITestimonialForm;
}

export interface IFetchTestimonialsRequest {
  userId: number;
  token: string;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IFetchTestimonialsResponse {
  message: string;
  data: {
    items: ITestimonial[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IFetchTopTestimonialsRequest {
  token: string;
  userId: number;
}

export interface IFetchTopTestimonialsResponse {
  message: string;
  data: ITestimonial[];
}

export interface IDeleteTestimonialRequest {
  id: number;
  token: string;
}

export interface IFetchAllProfileResponse {
  message: string;
  data: {
    page: number;
    pageSize: number;
    totalElements: number;
    direction: string;
    totalPages: number;
    items: IMinProfile[];
  };
}

export interface IFetchAllProfileRequest {
  token: string;
  page: number;
  pageSize: number;
  direction: string;
  filter: string;
}

export interface ICreateReviewRequest {
  authorId: number;
  reviewerId: number;
  token: string;
  rating: number;
  review: string;
}

export interface IEditReviewRequest extends ICreateReviewRequest {
  reviewId: number;
}

export interface IFetchReviewsRequest {
  userId: number;
  token: string;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IFetchReviewsResponse {
  message: string;
  data: {
    items: IReview[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IFetchReviewResponse {
  message: string;
  data: {
    id: number;
    rating: number;
    review: string;
  };
}

export interface IFetchReviewRequest {
  token: string;
  reviewId?: number | undefined;
}

export interface IDeleteReviewRequest {
  token: string;
  reviewId: number;
}

export interface ICreateConnectionRequest {
  token: string;
  senderId: number;
  receiverId: number;
}

export interface IVerifyConnectionRequest {
  token: string;
  senderId: number;
  receiverId: number;
}

export interface IFetchNotificationsRequest {
  userId: number;
  token: string;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IFetchNotificationsResponse {
  message: string;
  data: {
    items: INotification[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IDeleteNotificationRequest {
  notificationId: number;
  token: string;
  notificationRole: NotificationRole;
  senderId: number;
  receiverId: number;
}

export interface IVerifyConnectionResponse {
  message: string;
  data: {
    status: RequestStatus;
    id: number;
  };
}

export interface IDeleteConnectionRequest {
  token: string;
  connectionId: number;
}

export interface IFetchConnectionsRequest {
  userId: number;
  token: string;
  page: number;
  pageSize: number;
  direction: string;
  override: string;
}

export interface IFetchSearchConnectionsRequest {
  token: string;
  page: number;
  pageSize: number;
  direction: string;
  query: string;
}

export interface IFetchPinnedConnectionsResponse {
  message: string;
  data: IPinnedConnection[];
}

export interface IFetchPinnedConnectionsRequest {
  userId: number;
  token: string;
}

export interface IFetchConnectionsResponse {
  message: string;
  data: {
    items: IConnection[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface ICreatePinnedConnectionResponse {
  message: string;
}

export interface ICreatePinnedConnectionRequest {
  ownerId: number;
  connectionId: number;
  pinnedId: number;
  token: string;
}

export interface IFetchSearchConnectionsResposne extends IFetchConnectionsResponse {}

export interface IFetchChatMessagesRequest {
  token: string;
  connectionId: number;
}

export interface IFetchChatMessagesResponse {
  message: string;
  data: IMessage[];
}

export interface IToggleFavoriteRequest {
  token: string;
  isFavorited: boolean;
  userId: number;
  profileId: number;
}

export interface IDeletePinnedConnectionRequest {
  token: string;
  connectionPinId: number;
}

export interface IFetchGitHubTokenRequest {
  token: string;
  code: string;
}

export interface IFetchGitHubTokenResponse {
  message: string;
  accessToken: string;
}

export interface IFetchGitHubUserReposRequest {
  accessToken: string;
  token: string;
  page: number;
}

export interface IFetchGitHubUserReposResponse {
  message: string;
  data: { repositories: IGitHubRepositoryPreview[]; nextPageUrl: string };
}

export interface ICreateUserRepositoryRequest {
  token: string;
  payload: {
    reviewerId: number;
    ownerId: number;
    repoName: string;
    repoUrl: string;
    avatarUrl: string;
    comment: string;
    language: string;
  };
}

export interface IFetchDistinctRepositoryLanguagesResponse {
  message: string;
  data: string[];
}

export interface IFetchDistinctRepositoryLanguagesRequest {
  token: string;
}

export interface IFetchRepositoriesRequest {
  token: string;
  page: number;
  pageSize: number;
  direction: string;
  sortFilter: string;
  statusFilter: string;
  languageFilter: string;
}

export interface IFetchRepositoriesResponse {
  message: string;
  data: {
    items: IRepositoryReview[];
    page: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
    direction: string;
  };
}

export interface IDeleteUserRepositoryRequest {
  repositoryId: number;
  token: string;
}

export interface IFetchUserCommentRepositoryRequest {
  repositoryId: number;
  token: string;
}

export interface IFetchUserCommentRepositoryResponse {
  message: string;
  data: string;
}

export interface IFetchRepositoryRequest {
  repositoryId: number;
  token: string;
  accessToken: string;
  repositoryPage: number;
}

export interface IFetchRepositoryResponse {
  message: string;
  data: {
    repository: IGitHubRepository;
    contents: {
      tree: IGitHubTree[];
      languages: string[];
    };
  };
}

export interface IUpdateRepositoryCommentRequest {
  token: string;
  repositoryId: number;
  comment: string;
}

export interface ICreateRepositoryFileRequest {
  token: string;
  accessToken: string;
  owner: string;
  repoName: string;
  path: string;
}

export interface ICreateRepositoryFileResponse {
  message: string;
  data: any;
}

export interface IUpdateRepositoryRequest {
  status: string;
  feedback: string;
  token: string;
  repositoryId: number;
}

export interface IUpdateRepositoryResponse {
  message: string;
  data: {
    status: string;
    feedback: string;
  };
}

export interface ICreateWorkSpaceRequest {
  token: string;
  userId: number;
  workSpace: { title: string; backgroundColor: string };
}

export interface IUpdateWorkSpaceRequest {
  token: string;
  userId: number;
  id: number;
  workSpace: { title: string; backgroundColor: string };
}

export interface IUpdateWorkSpaceResponse {
  message: string;
  data: IWorkSpaceEntity;
}

export interface ICreateWorkSpaceResponse {
  message: string;
  data: {
    title: string;
    backgroundColor: string;
  };
}

export interface IFetchWorkSpacesRequest {
  userId: number;
  token: string;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IFetchWorkSpacesResponse {
  message: string;
  data: {
    items: IWorkSpaceEntity[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IDeleteWorkSpaceRequest {
  token: string;
  id: number;
}

export interface IDeleteWorkSpaceResponse extends IBaseResponse {}

export interface IUpdateRepositoryCommentResponse extends IBaseResponse {}

export interface IDeleteUserRepositoryResponse extends IBaseResponse {}

export interface ICreateUserRepositoryResponse extends IBaseResponse {}

export interface IDeletePinnedConnectionResponse extends IBaseResponse {}

export interface IToggleFavoriteResponse extends IBaseResponse {}

export interface IDeleteConnectionResponse extends IBaseResponse {}

export interface IDeleteNotificationResponse extends IBaseResponse {}

export interface ICreateConnectionResponse extends IBaseResponse {}

export interface IDeleteReviewResponse extends IBaseResponse {}

export interface IEditReviewResponse extends IBaseResponse {}

export interface ICreateReviewResponse extends IBaseResponse {}

export interface IDeleteTestimonialResponse extends IBaseResponse {}

export interface ICreateTestimonialResponse extends IBaseResponse {}

export interface IDeletePaymentMethodResponse extends IBaseResponse {}

export interface ICreatePaymentMethodResponse extends IBaseResponse {}

export interface IUpdateProfileResponse extends IBaseResponse {}

export interface IRemoveAvatarResponse extends IBaseResponse {}

export interface IDeleteUserResponse extends IBaseResponse {}

export interface IVerifyOTPResponse extends ISignInResponse {}

export interface IDeletePhoneResponse extends IBaseResponse {}

export interface ICreatePhoneResponse extends IBaseResponse {}

export interface IHeartBeatResponse extends IBaseResponse {}

export interface IForgotPasswordResponse extends IBaseResponse {}

export interface IResetPasswordResponse extends IBaseResponse {}

export interface ISignUpResponse extends IBaseResponse {}

export interface ISignOutResponse extends IBaseResponse {}

export interface IUpdateUserPasswordResponse extends IBaseResponse {}

export interface ISyncUserResponse extends IUser {}
