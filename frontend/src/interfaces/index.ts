import { NotificationRole, NotificationType, RequestStatus, Role } from '../enums';
import { TPureTodoCard } from '../types';

export interface IMinAppTestimonial {
  id: number;
  developerType: string;
  content: string;
}

export interface IGalleryPhoto {
  id: number;
  title: string;
  src: string;
}

export interface IReportComment {
  id: number;
  details: string;
  reason: string;
  status: string;
  reportedBy: string;
  createdAt: string;
  content: string;
  commentAvatarUrl: string;
  topicTitle: string;
}

export interface IRefund {
  id: number;
  adminNotes: string;
  amount: number;
  currency: string;
  reason: string;
  status: string;
  stripePaymentIntentId: number;
  createdAt: string;
  userId: number;
  fullName: string;
}

export interface IPaymentIntent {
  id: number;
  amount: number;
  currency: string;
  fullName: string;
  reviewerId: number;
  avatarUrl: string;
  createdAt: string;
  status: string;
}

export interface IReplyComment {
  id: number;
  content: string;
  createdAt: string;
  fullName: string;
  avatarUrl: string;
  userId: number;
}

export interface ISaveComment extends IReplyComment {}

export interface IReaction {
  emoji: string;
  count: number;
}

export interface IMinComment {
  id: number;
  content: string;
  userId: number;
  createdAt: string;
  avatarUrl: string;
  fullName: string;
}

export interface IComment {
  id: number;
  content: string;
  userId: number;
  createdAt: string;
  avatarUrl: string;
  fullName: string;
  isEdited: boolean;
  voteDifference: number;
  curUserVoteType: string;
  curUserHasVoted: boolean;
  curUserHasSaved: boolean;
  reactions: IReaction[];
  replyCommentsCount: number;
}

export interface ITag {
  id: number;
  name: string;
}

export interface IFormTopicTag {
  id: string;
  name: string;
}

export interface ITopic {
  id: number;
  title: string;
  description: string;
  tags: ITag[];
  totalCommentCount: number;
}

export interface INotificationSwitch {
  name: string;
  displayName: string;
  isOn: boolean;
}

export interface IBlockedUser {
  createdAt: string;
  fullName: string;
  avatarUrl: string;
  id: number;
  blockedUserId: number;
}

export interface IStatisticAvgRating {
  name: string;
  average: number;
}

export interface IStatisticAvgReviewTime {
  month: string;
  avgReviewTime: number;
}

export interface IStatisticMainLanguage {
  lanugage: string;
  count: number;
}

export interface IStatisticReviewTypesCompleted {
  reviewType: string;
  count: number;
}

export interface IStatisticReviewsCompleted {
  day: string;
  reviewsCompleted: number;
}

export interface IStatisticStatusTypes {
  status: string;
  count: number;
}

export interface IStatisticTopRequesters {
  fullName: string;
  count: number;
}

export interface IStatistics {
  avgRatings: IStatisticAvgRating[];
  avgReviewTimes: IStatisticAvgReviewTime[];
  mainLanguages: IStatisticMainLanguage[];
  reviewTypesCompleted: IStatisticReviewTypesCompleted[];
  reviewsCompleted: IStatisticReviewsCompleted[];
  statusTypes: IStatisticStatusTypes[];
  topRequesters: IStatisticTopRequesters[];
}

export interface IReviewFeedback {
  [key: string]: string | number;
  id: number;
  clarity: number;
  helpfulness: number;
  thoroughness: number;
  responseTime: number;
  repositoryId: number;
  reviewerId: number;
  ownerId: number;
  createdAt: string;
}

export interface IReviewFeedbackFormField {
  title: string;
  name: string;
  value: number;
  desc: string;
}

export interface IReviewFeedbackForm {
  [key: string]: IReviewFeedbackFormField;
  thoroughness: IReviewFeedbackFormField;
  clarity: IReviewFeedbackFormField;
  responseTime: IReviewFeedbackFormField;
  helpfulness: IReviewFeedbackFormField;
}

export interface IError {
  [key: string]: string;
}

export interface IDropDownOption {
  id: number;
  customFieldId: number;
  optionValue: string;
}

export interface ICustomField {
  id: number;
  userId: number;
  todoCardId: number;
  fieldType: string;
  fieldName: string;
  selectedValue: string;
  isActive: boolean;
  dropDownOptions: IDropDownOption[];
}

export interface ICustomFieldTypeOption {
  id: string;
  value: string;
}

export interface ICustomFieldType {
  fieldName: string;
  fieldType: string;
  selectedTitle: string;
  selectedValue?: string | ICustomFieldTypeOption;
  options?: ICustomFieldTypeOption[];
}

export interface IServerError {
  status: number;
  data: string;
}

export interface IActivity {
  id: number;
  userId: number;
  todoCardId: number;
  text: string;
  createdAt: string | Date | null;
  avatarUrl: string;
}

export interface ICheckListItem {
  [key: string]: string | number | boolean;
  id: number;
  userId: number;
  checkListId: number;
  title: string;
  isCompleted: boolean;
}

export interface ICheckList {
  id: number;
  userId: number;
  todoCardId: number;
  createdAt: string | Date | null;
  isCompleted: boolean;
  title: string;
  checkListItems: ICheckListItem[];
}

export interface ITodoCard {
  todoListId: number;
  userId: number;
  id: number;
  createdAt: string;
  label: string;
  title: string;
  color: string;
  index: number;
  details: string;
  startDate: string | Date | null;
  endDate: string | Date | null;
  photo: string;
  todoListTitle: string;
  uploadPhotoUrl: string | null;
}

export interface ITodoList {
  id: number;
  userId: number;
  workSpaceId: number;
  title: string;
  index: number;
  createdAt: string;
  cards: ITodoCard[];
}

export interface ITodoListsState {
  todoLists: ITodoList[];
}

export interface ILabel {
  id: number;
  userId: number;
  workSpaceId: number;
  createdAt: string | Date | null;
  isChecked: boolean;
  title: string;
  color: string;
}

export interface IActiveLabel {
  id: number;
  todoCardId: number;
  labelId: number;
  color: string;
  title: string;
}

export interface IWorkSpaceState {
  workSpace: IWorkSpaceEntity;
  labels: ILabel[];
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

export interface IReviewTypeMapper {
  BUG: { text: string; backgroundColor: string };
  FEATURE: { text: string; backgroundColor: string };
  OPTIMIZATION: { text: string; backgroundColor: string };
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
  reviewStartTime: string;
  reviewEndTime: string;
  reviewDuration: string;
  reviewType: string;
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
  reviewStartTime: string;
  reviewEndTime: string;
  feedback: string;
  paymentPrice: number;
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
  link: string;
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
  requestAcceptedNotifOn: boolean;
  requestPendingNotifOn: boolean;
  paymentAcknowledgementNotifOn: boolean;
  reviewInProgressNotifOn: boolean;
  reviewInCompleteNotifOn: boolean;
  reviewCompletedNotifOn: boolean;
  commentReplyOn: boolean;
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
  lastActive: string;
  lastActiveReadable: string;
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
  data: ISetting;
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

export interface IFetchProfilePackageResponse {
  message: string;
  data: IPckgResponse;
}

export interface IFetchProfilePackageRequest {
  token: string;
  userId: number;
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
    stripeEnabled: boolean;
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
    reviewType: string;
    paymentPrice: number;
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

export interface ICreateTodoListRequest {
  token: string;
  userId: number;
  title: string;
  workSpaceId: number;
  index: number;
}

export interface ICreateTodoListResponse {
  message: string;
  data: ITodoList;
}

export interface IFetchTodoListsResponse {
  message: string;
  data: ITodoList[];
}

export interface IFetchTodoListsRequest {
  workSpaceId: number;
  token: string;
}

export interface IUpdateTodoListResponse {
  message: string;
  data: ITodoList;
}

export interface IUpdateTodoListRequest {
  token: string;
  title: string;
  index: number;
  workSpaceId: number;
  id: number;
}

export interface IDeleteTodoListRequest {
  token: string;
  id: number;
}

export interface IReorderTodoListRequest {
  todoLists: ITodoList[];
  token: string;
  workSpaceId: number;
}

export interface IReorderTodoListResponse {
  message: string;
  data: ITodoList[];
}

export interface ICreateTodoCardRequest {
  userId: number;
  todoListId: number;
  title: string;
  index: number;
  token: string;
}

export interface ICreateTodoCardResponse {
  message: string;
  data: ITodoCard;
}

export interface IUpdateTodoCardRequest {
  card: TPureTodoCard;
  token: string;
}

export interface IUpdateTodoCardResponse {
  message: string;
  data: ITodoCard;
}

export interface IDeleteTodoCardRequest {
  token: string;
  todoCardId: number;
}

export interface IFetchLatestWorkSpaceRequest {
  token: string;
  userId: number;
}

export interface IFetchLatestWorkSpaceResponse {
  message: string;
  data: IWorkSpaceEntity;
}

export interface IReorderTodoCardRequest {
  token: string;
  listId: number;
  oldIndex: number;
  newIndex: number;
  todoCardId: number;
}

export interface IMoveTodoCardRequest {
  token: string;
  sourceListId: number;
  destinationListId: number;
  newIndex: number;
  todoCardId: number;
}

export interface IFetchPexelPhotosRequest {
  token: string;
  query?: string;
}

export interface IFetchPexelPhotosResponse {
  message: string;
  data: string[];
}

export interface ICreateLabelRequest {
  userId: number;
  workSpaceId: number;
  token: string;
  title: string;
  color: string;
}

export interface IFetchLabelRequest {
  token: string;
  workSpaceId: number;
}

export interface IFetchLabelResponse {
  message: string;
  data: ILabel[];
}

export interface IDeleteLabelRequest {
  id: number;
  token: string;
}

export interface ICreateActiveLabelRequest {
  cardId: number;
  labelId: number;
  token: string;
}

export interface IDeleteActiveLabelRequest {
  id: number;
  labelId: number;
  token: string;
}

export interface IFetchActiveLabelsRequest {
  token: string;
  todoCardId: number;
}

export interface IFetchActiveLabelsResponse {
  message: string;
  data: IActiveLabel[];
}

export interface IUpdateLabelRequest {
  token: string;
  label: ILabel;
}

export interface IUpdateLabelResponse {
  message: string;
  data: ILabel;
}

export interface IUploadTodoCardRequest {
  token: string;
  todoCardId: number;
  formData: FormData;
}

export interface IUploadTodoCardResponse {
  message: string;
  data: ITodoCard;
}

export interface ICreateCheckListRequest {
  userId: number;
  title: string;
  todoCardId: number;
  token: string;
}

export interface ICreateCheckListResponse {
  message: string;
}

export interface IFetchCheckListsRequest {
  token: string;
  todoCardId: number;
}

export interface IFetchCheckListsResponse {
  message: string;
  data: ICheckList[];
}

export interface IDeleteCheckListRequest {
  id: number;
  token: string;
}

export interface ICreateCheckListItemRequest {
  token: string;
  userId: number;
  checkListId: number;
  title: string;
}

export interface IUpdateCheckListItemRequest {
  data: ICheckListItem;
  token: string;
}

export interface ICreateCheckListItemResponse {
  message: string;
  data: ICheckListItem;
}

export interface IDeleteCheckListItemRequest {
  id: number;
  token: string;
}

export interface IDeleteCheckListItemResponse {
  message: string;
}

export interface ICreateActivityRequest {
  token: string;
  todoCardId: number;
  text: string;
  userId: number;
}

export interface ICreateActivityResponse {
  message: string;
  data: IActivity;
}

export interface IFetchActivityResponse {
  message: string;
  data: {
    items: IActivity[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IFetchActivityRequest {
  token: string;
  todoCardId: number;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IDeleteActivityRequest {
  activityId: number;
  token: string;
}

export interface ICreateCustomFieldRequest {
  todoCardId: number;
  userId: number;
  token: string;
  fieldType: string;
  fieldName: string;
  selectedValue: string;
  dropDownOptions: ICustomFieldTypeOption[];
}

export interface ICreateCustomFieldResponse {
  message: string;
}

export interface IFetchCustomFieldsRequest {
  token: string;
  todoCardId: number;
  isActive: string;
}

export interface IFetchCustomFieldsResponse {
  message: string;
  data: ICustomField[];
}

export interface IDeleteCustomFieldRequest {
  token: string;
  id: number;
}

export interface IDeleteDropDownOptionRequest {
  token: string;
  id: number;
}

export interface IUpdateCustomFieldRequest {
  token: string;
  id: number;
  isActive: boolean;
}

export interface IUpdateRepositoryReviewStartTimeRequest {
  reviewStartTime: string;
  repositoryId: number;
  status: string;
  token: string;
}

export interface ICreateReviewFeedbackRequest {
  token: string;
  clarity: number;
  helpfulness: number;
  thoroughness: number;
  responseTime: number;
  repositoryId: number;
  reviewerId: number;
  ownerId: number;
}

export interface IGetSingleReviewFeedbackRequest {
  token: string;
  reviewerId: number;
  ownerId: number;
  repositoryId: number;
}

export interface IGetSingleReviewFeedbackResponse {
  message: string;
  data: IReviewFeedback;
}

export interface IFetchStatisticRequest {
  reviewerId: number;
  token: string;
}

export interface IFetchStatisticResponse {
  message: string;
  data: IStatistics;
}

export interface IUpdateSettingRequest {
  token: string;
  setting: ISetting;
}

export interface IUpdateSettingResponse {
  message: string;
  data: any;
}

export interface ICreateBlockedUserRequest {
  token: string;
  blockedUserId: number;
  blockerUserId: number;
}

export interface IFetchBlockedUsersRequest {
  token: string;
  page: number;
  pageSize: number;
  blockerUserId: number;
  direction: string;
}

export interface IFetchBlockedUsersResponse {
  message: string;
  data: {
    items: IBlockedUser[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IDeleteBlockedUserRequest {
  token: string;
  blockUserId: number;
}

export interface IUpdateProfileVisibilityRequest {
  token: string;
  isVisible: boolean;
  profileId: number;
}

export interface IUpdateProfileVisibilityResponse {
  message: string;
  data: boolean;
}

export interface IFetchProfileVisibilityRequest {
  token: string;
  profileId: number;
}

export interface ICreateTopicRequest {
  title: string;
  description: string;
  tags: string[];
  userId: number;
  token: string;
}

export interface IGetTopicsRequest {
  query: string;
}

export interface IGetTopicsResponse {
  message: string;
  data: ITopic[];
}

export interface IGetTopicRequest {
  topicId: number;
}

export interface IGetTopicResponse {
  message: string;
  data: ITopic;
}

export interface ICreateCommentRequest {
  token: string;
  userId: number;
  topicId: number;
  content: string;
}

export interface IGetCommentsRequest {
  topicId: number;
  page: number;
  pageSize: number;
  direction: string;
  sort: string;
  token?: string;
}

export interface IGetCommentsResponse {
  message: string;
  data: {
    items: IComment[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface ICreateVoteRequest {
  token: string;
  commentId: number;
  userId: number;
  voteType: string;
}

export interface ICreateVoteResponse {
  message: string;
  data: any;
}

export interface IUpdateCommentRequest {
  token: string;
  commentId: number;
  userId: number;
  content: string;
}

export interface IDeleteCommentRequest {
  commentId: number;
  token: string;
}

export interface ICreateReportCommentRequest {
  userId: number;
  token: string;
  commentId: number;
  details: string;
  reason: string;
}

export interface ICreateSaveCommentRequest {
  token: string;
  userId: number;
  commentId: number;
}

export interface ICreateReactionRequest {
  token: string;
  userId: number;
  commentId: number;
  emoji: string | null;
}

export interface IGetReactionRequest {
  token: string;
  userId: number;
  commentId: number;
}

export interface IGetReactionResponse {
  message: string;
  data: string | null;
}

export interface IDeleteReactionRequest {
  token: string;
  userId: number;
  commentId: number;
}

export interface IGetAllTopicsRequest {
  page: number;
  pageSize: number;
  direction: string;
}

export interface IGetAllTopicsResponse {
  message: string;
  data: {
    items: ITopic[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface ICreateReplyCommentRequest {
  token: string;
  userId: number;
  commentId: number;
  content: string;
}

export interface IGetCommentRequest {
  token: string;
  commentId: number;
}

export interface IGetCommentResponse {
  message: string;
  data: IMinComment;
}

export interface IGetReplyCommentsByUserRequest {
  token: string;
  userId: number;
  commentId: number;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IGetReplyCommentsByUserResponse {
  message: string;
  data: {
    items: IReplyComment[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IGetReplyCommentsRequest {
  token: string;
  commentId: number;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IGetReplyCommentsResponse {
  message: string;
  data: {
    items: IReplyComment[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IUpdateReplyCommentRequest {
  token: string;
  replyCommentId: number;
  commentId: number;
  content: string;
}

export interface IUpdateReplyCommentResponse {
  message: string;
  data: string;
}

export interface IDeleteReplyCommentRequest {
  commentId: number;
  token: string;
  replyCommentId: number;
}

export interface IGetSaveCommentsRequest {
  token: string;
  userId: number;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IGetSaveCommentsResponse {
  message: string;
  data: {
    items: ISaveComment[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IDeleteSaveCommentRequest {
  token: string;
  saveCommentId: number;
}

export interface IGetAllUserTopicsRequest {
  userId: number;
  token: string;
  page: number;
  direction: string;
  pageSize: number;
}

export interface IGetAllUserTopicsResponse {
  message: string;
  data: {
    items: ITopic[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IUpdateTopicRequest {
  token: string;
  userId: number;
  topicId: number;
  description: string;
  tags: string[];
}

export interface IUpdateTopicResponse {
  message: string;
}

export interface IConnectStripeAccountRequest {
  userId: number;
  token: string;
  email: string;
}

export interface IConnectStripeAccountResponse {
  message: string;
  data: any;
}

export interface ITransferCustomerMoneyToReviewerRequest {
  token: string;
  reviewerId: number;
  ownerId: number;
  repositoryId: number;
}

export interface ITransferCustomerMoneyToReviewerResponse {
  message: string;
  data: any;
}

export interface IGetAllStripePaymentIntentsResponse {
  message: string;
  data: {
    items: IPaymentIntent[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IGetAllStripePaymentIntentsRequest {
  userId: number;
  token: string;
  page: number;
  pageSize: number;
  direction: string;
}

export interface ICreatePaymentRefundRequest {
  token: string;
  userId: number;
  stripePaymentIntentId: number;
  reason: string;
}

export interface IGetAllStripePaymentRefundRequest {
  token: string;
  userId: number;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IGetAllStripePaymentRefundResponse {
  message: string;
  data: {
    items: IRefund[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IUpdatePaymentRefundRequest {
  id: number;
  token: string;
  userId: number;
  adminNotes: string;
  stripePaymentIntentId: number;
  status: string;
}

export interface IGetAllReportCommentRequest {
  token: string;
  page: number;
  pageSize: number;
  direction: string;
}

export interface IGetAllReportCommentResponse {
  message: string;
  data: {
    items: IReportComment[];
    page: number;
    pageSize: number;
    totalPages: number;
    direction: string;
    totalElements: number;
  };
}

export interface IDeleteReportCommentRequest {
  id: number;
  token: string;
}

export interface ICreateAppTestimonialRequest {
  userId: number;
  token: string;
  developerType: string;
  content: string;
}
export interface IUpdateAppTestimonialRequest {
  id: number;
  userId: number;
  token: string;
  developerType: string;
  content: string;
}

export interface IGetAppTestimonialResponse {
  message: string;
  data: IMinAppTestimonial;
}

export interface IGetAppTestimonialRequest {
  token: string;
}

export interface IUpdateAppTestimonialResponse extends IBaseResponse {}

export interface ICreateAppTestimonialResponse extends IBaseResponse {}

export interface IDeleteReportCommentResponse extends IBaseResponse {}

export interface IUpdatePaymentRefundResponse {
  message: string;
}

export interface ICreatePaymentRefundResponse {
  message: string;
}

export interface IDeleteSaveCommentResponse extends IBaseResponse {}

export interface IDeleteReplyCommentResponse extends IBaseResponse {}

export interface ICreateReplyCommentResponse extends IBaseResponse {}

export interface IGetTopicsWithTagsRequest extends IGetAllTopicsRequest {
  query: string;
}

export interface IGetTopicsWithTagsResponse extends IGetAllTopicsResponse {}

export interface IDeleteReactionResponse extends IBaseResponse {}

export interface ICreateReactionResponse extends IBaseResponse {}

export interface ICreateSaveCommentResponse extends IBaseResponse {}

export interface ICreateReportCommentResponse extends IBaseResponse {}

export interface IDeleteCommentResponse extends IBaseResponse {}

export interface IUpdateCommentResponse extends IBaseResponse {}

export interface ICreateCommentResponse extends IBaseResponse {}

export interface ICreateTopicResponse extends IBaseResponse {}

export interface IFetchProfileVisibilityResponse extends IUpdateProfileVisibilityResponse {}

export interface IDeleteBlockedUserResponse extends IBaseResponse {}

export interface ICreateBlockedUserResponse extends IBaseResponse {}

export interface ICreateReviewFeedbackResponse extends IBaseResponse {}

export interface IUpdateRepositoryReviewStartTimeResponse extends IBaseResponse {}

export interface IUpdateCustomFieldResponse extends IBaseResponse {}

export interface IDeleteDropDownOptionResponse extends IBaseResponse {}

export interface IDeleteCustomFieldResponse extends IBaseResponse {}

export interface IDeleteActivityResponse extends IBaseResponse {}

export interface IUpdateCheckListItemResponse extends IBaseResponse {}

export interface IDeleteCheckListResponse extends IBaseResponse {}

export interface ICreateActiveLabelResponse extends IBaseResponse {}

export interface IDeleteActiveLabelResponse extends IBaseResponse {}

export interface IDeleteLabelResponse extends IBaseResponse {}

export interface ICreateLabelResponse extends IBaseResponse {}

export interface IMoveTodoCardResponse extends IBaseResponse {}

export interface IReorderTodoCardResponse extends IBaseResponse {}

export interface IDeleteTodoCardResponse extends IBaseResponse {}

export interface IDeleteTodoListResponse extends IBaseResponse {}

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
