import { Role } from '../enums';

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

export interface IResetPasswordForm {
  password: IFormField<string>;
  confirmPassword: IFormField<string>;
}

export interface IChangePasswordForm {
  password: IFormField<string>;
  curPassword: IFormField<string>;
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
