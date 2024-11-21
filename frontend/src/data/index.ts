export const landingPageGalleryPhotos = [
  {
    id: 1,
    title: 'Kanban Options',
    src: 'https://res.cloudinary.com/dap5r6vfu/image/upload/v1732043901/overwatch/trello-options_mssue9.png',
  },
  {
    id: 2,
    title: 'Kanboard Board',
    src: 'https://res.cloudinary.com/dap5r6vfu/image/upload/v1732043898/overwatch/main-trello_cv5ty1.png',
  },
  {
    id: 3,
    title: 'Explore Topics',
    src: 'https://res.cloudinary.com/dap5r6vfu/image/upload/v1732043898/overwatch/single_view_community_glefic.png',
  },
  {
    id: 4,
    title: 'Reviewer Profiles',
    src: 'https://res.cloudinary.com/dap5r6vfu/image/upload/v1732043898/overwatch/explore-single-view_tmpdo4.png',
  },
  {
    id: 5,
    title: 'Explore Reviewers',
    src: 'https://res.cloudinary.com/dap5r6vfu/image/upload/v1732043898/overwatch/explore-page_ussp5l.png',
  },
  {
    id: 6,
    title: 'Explore The Community Side Of OverWatch',
    src: 'https://res.cloudinary.com/dap5r6vfu/image/upload/v1732043898/overwatch/community_g1qg2g.png',
  },
];

export const minCommentState = {
  id: 0,
  content: '',
  userId: 0,
  createdAt: '',
  avatarUrl: '',
  fullName: '',
};

export const topicState = {
  id: 0,
  title: '',
  description: '',
  tags: [],
  totalCommentCount: 0,
};

export const repositoryState = {
  avatarUrl: '',
  comment: '',
  createdAt: '',
  feedback: '',
  id: 0,
  language: '',
  ownerId: 0,
  repoName: '',
  repoUrl: '',
  reviewerId: 0,
  status: '',
  updatedAt: '',
  reviewStartTime: '',
  reviewEndTime: '',
  reviewDuration: '',
  reviewType: '',
};

export const pckgsState = {
  basic: { price: '', description: '', items: [] },
  pro: { price: '', description: '', items: [] },
  standard: { price: '', description: '', items: [] },
};

export const labelColors = [
  { id: 1, background: '#F1C93B' },
  { id: 2, background: '#1A5D1A' },
  { id: 3, background: '#A2FF86' },
  { id: 4, background: '#B31312' },
  { id: 5, background: '#E8A9A9' },
  { id: 6, background: '#068FFF' },
  { id: 7, background: '#090580' },
  { id: 8, background: '#E57C23' },
  { id: 9, background: '#FF55BB' },
  { id: 10, background: '#a229ec' },
  { id: 11, background: '#59652a' },
  { id: 12, background: '#021f27' },
  { id: 13, background: '#9acbae' },
  { id: 14, background: '#32bcdf' },
  { id: 15, background: '#ccc5b9' },
];

export const backgroundColors = [
  { id: 1, value: '#fb7185' },
  { id: 2, value: '#f472b6' },
  { id: 3, value: '#e879f9' },
  { id: 4, value: '#c084fc' },
  { id: 5, value: '#a78bfa' },
  { id: 6, value: '#818cf8' },
  { id: 7, value: '#60a5fa' },
  { id: 8, value: '#22d3ee' },
  { id: 9, value: '#34d399' },
  { id: 10, value: '#4ade80' },
  { id: 11, value: '#facc15' },
  { id: 12, value: '#fbb524' },
  { id: 13, value: '#f87171' },
  { id: 14, value: '#a8e29e' },
  { id: 15, value: '#9ca3af' },
];

export const reviewsFilterStatusOptions = [
  { id: 1, value: 'INPROGRESS', name: 'In Progress' },
  { id: 2, value: 'INCOMPLETE', name: 'In Complete' },
  { id: 3, value: 'COMPLETED', name: 'Completed' },
];

export const reviewsFilterSortingOptions = [
  { id: 1, value: 'desc', name: 'Newest' },
  { id: 2, value: 'asc', name: 'Oldest' },
];

export const profileState = {
  additionalInfo: {
    availability: [],
    moreInfo: '',
  },
  basicInfo: {
    contactNumber: '',
    email: '',
    fullName: '',
    userName: '',
  },
  pckg: {
    basic: { price: '', description: '', items: [] },
    pro: { price: '', description: '', items: [] },
    standard: { price: '', description: '', items: [] },
  },
  profileSetup: {
    avatar: '',
    tagLine: '',
    bio: '',
  },
  skills: {
    languages: [],
    programmingLanguages: [],
    qualifications: [],
  },
  userProfile: {
    id: 0,
    userId: 0,
    role: '',
    country: '',
    abbreviation: '',
    city: '',
  },
  workExp: {
    workExps: [],
  },
};

export const connectionState = {
  id: 0,
  receiverId: 0,
  senderId: 0,
  firstName: '',
  lastName: '',
  avatarUrl: '',
  email: '',
  city: '',
  country: '',
  phoneNumber: '',
  bio: '',
  lastMessage: '',
};

export const paginationState = {
  page: 0,
  pageSize: 3,
  totalPages: 0,
  direction: 'next',
  totalElements: 0,
};

export const paginationStateFromZero = {
  page: -1,
  pageSize: 3,
  totalPages: 0,
  direction: 'next',
  totalElements: 0,
};

export const repositoryPaginationState = {
  page: 0,
  pageSize: 5,
  totalPages: 0,
  direction: 'next',
  totalElements: 0,
};

export const savePaymentFormState = {
  firstName: { name: 'firstName', value: '', error: '', type: 'text' },
  lastName: { name: 'lastName', value: '', error: '', type: 'text' },
  country: { name: 'country', value: 'US', error: '', type: 'email' },
  city: { name: 'city', value: '', error: '', type: 'text' },
  line1: { name: 'line1', value: '', error: '', type: 'text' },
  line2: { name: 'line2', value: '', error: '', type: 'text' },
  state: { name: 'state', value: '', error: '', type: 'text' },
  postalCode: { name: 'postalCode', value: '', error: '', type: 'text' },
};

export const reviewFeedbackState = {
  helpfulness: { title: 'Helpfulness', name: 'helpfulness', value: 0, desc: 'How helpful was this feedback?' },
  clarity: { title: 'Clarity', name: 'clarity', value: 0, desc: 'How clear and understandable was this feedback?' },
  responseTime: { title: 'Response Time', name: 'responseTime', value: 0, desc: 'How fast was this feedback?' },
  thoroughness: {
    title: 'Thoroughness',
    name: 'thoroughness',
    value: 0,
    desc: 'How detailed and thorough was this feedback?',
  },
};

export const countryCodes = [
  { id: 1, name: 'AF', value: 'AF' },
  { id: 2, name: 'AX', value: 'AX' },
  { id: 3, name: 'AL', value: 'AL' },
  { id: 4, name: 'DZ', value: 'DZ' },
  { id: 5, name: 'AS', value: 'AS' },
  { id: 6, name: 'AD', value: 'AD' },
  { id: 7, name: 'AO', value: 'AO' },
  { id: 8, name: 'AI', value: 'AI' },
  { id: 9, name: 'AQ', value: 'AQ' },
  { id: 10, name: 'AG', value: 'AG' },
  { id: 11, name: 'AR', value: 'AR' },
  { id: 12, name: 'AM', value: 'AM' },
  { id: 13, name: 'AW', value: 'AW' },
  { id: 14, name: 'AU', value: 'AU' },
  { id: 15, name: 'AT', value: 'AT' },
  { id: 16, name: 'AZ', value: 'AZ' },
  { id: 17, name: 'BS', value: 'BS' },
  { id: 18, name: 'BH', value: 'BH' },
  { id: 19, name: 'BD', value: 'BD' },
  { id: 20, name: 'BB', value: 'BB' },
  { id: 21, name: 'BY', value: 'BY' },
  { id: 22, name: 'BE', value: 'BE' },
  { id: 23, name: 'BZ', value: 'BZ' },
  { id: 24, name: 'BJ', value: 'BJ' },
  { id: 25, name: 'BM', value: 'BM' },
  { id: 26, name: 'BT', value: 'BT' },
  { id: 27, name: 'BO', value: 'BO' },
  { id: 28, name: 'BA', value: 'BA' },
  { id: 29, name: 'BW', value: 'BW' },
  { id: 30, name: 'BV', value: 'BV' },
  { id: 31, name: 'BR', value: 'BR' },
  { id: 32, name: 'IO', value: 'IO' },
  { id: 33, name: 'BN', value: 'BN' },
  { id: 34, name: 'BG', value: 'BG' },
  { id: 35, name: 'BF', value: 'BF' },
  { id: 36, name: 'BI', value: 'BI' },
  { id: 37, name: 'KH', value: 'KH' },
  { id: 38, name: 'CM', value: 'CM' },
  { id: 39, name: 'CA', value: 'CA' },
  { id: 40, name: 'CV', value: 'CV' },
  { id: 41, name: 'KY', value: 'KY' },
  { id: 42, name: 'CF', value: 'CF' },
  { id: 43, name: 'TD', value: 'TD' },
  { id: 44, name: 'CL', value: 'CL' },
  { id: 45, name: 'CN', value: 'CN' },
  { id: 46, name: 'CX', value: 'CX' },
  { id: 47, name: 'CC', value: 'CC' },
  { id: 48, name: 'CO', value: 'CO' },
  { id: 49, name: 'KM', value: 'KM' },
  { id: 50, name: 'CG', value: 'CG' },
  { id: 51, name: 'CD', value: 'CD' },
  { id: 52, name: 'CK', value: 'CK' },
  { id: 53, name: 'CR', value: 'CR' },
  { id: 54, name: 'CI', value: 'CI' },
  { id: 55, name: 'HR', value: 'HR' },
  { id: 56, name: 'CU', value: 'CU' },
  { id: 57, name: 'CY', value: 'CY' },
  { id: 58, name: 'CZ', value: 'CZ' },
  { id: 59, name: 'DK', value: 'DK' },
  { id: 60, name: 'DJ', value: 'DJ' },
  { id: 61, name: 'DM', value: 'DM' },
  { id: 62, name: 'DO', value: 'DO' },
  { id: 63, name: 'EC', value: 'EC' },
  { id: 64, name: 'EG', value: 'EG' },
  { id: 65, name: 'SV', value: 'SV' },
  { id: 66, name: 'GQ', value: 'GQ' },
  { id: 67, name: 'ER', value: 'ER' },
  { id: 68, name: 'EE', value: 'EE' },
  { id: 69, name: 'ET', value: 'ET' },
  { id: 70, name: 'FK', value: 'FK' },
  { id: 71, name: 'FO', value: 'FO' },
  { id: 72, name: 'FJ', value: 'FJ' },
  { id: 73, name: 'FI', value: 'FI' },
  { id: 74, name: 'FR', value: 'FR' },
  { id: 75, name: 'GF', value: 'GF' },
  { id: 76, name: 'PF', value: 'PF' },
  { id: 77, name: 'TF', value: 'TF' },
  { id: 78, name: 'GA', value: 'GA' },
  { id: 79, name: 'GM', value: 'GM' },
  { id: 80, name: 'GE', value: 'GE' },
  { id: 81, name: 'DE', value: 'DE' },
  { id: 82, name: 'GH', value: 'GH' },
  { id: 83, name: 'GI', value: 'GI' },
  { id: 84, name: 'GR', value: 'GR' },
  { id: 85, name: 'GL', value: 'GL' },
  { id: 86, name: 'GD', value: 'GD' },
  { id: 87, name: 'GP', value: 'GP' },
  { id: 88, name: 'GU', value: 'GU' },
  { id: 89, name: 'GT', value: 'GT' },
  { id: 90, name: 'GG', value: 'GG' },
  { id: 91, name: 'GN', value: 'GN' },
  { id: 92, name: 'GW', value: 'GW' },
  { id: 93, name: 'GY', value: 'GY' },
  { id: 94, name: 'HT', value: 'HT' },
  { id: 95, name: 'HM', value: 'HM' },
  { id: 96, name: 'VA', value: 'VA' },
  { id: 97, name: 'HN', value: 'HN' },
  { id: 98, name: 'HK', value: 'HK' },
  { id: 99, name: 'HU', value: 'HU' },
  { id: 100, name: 'IS', value: 'IS' },
  { id: 101, name: 'IN', value: 'IN' },
  { id: 102, name: 'ID', value: 'ID' },
  { id: 103, name: 'IR', value: 'IR' },
  { id: 104, name: 'IQ', value: 'IQ' },
  { id: 105, name: 'IE', value: 'IE' },
  { id: 106, name: 'IM', value: 'IM' },
  { id: 107, name: 'IL', value: 'IL' },
  { id: 108, name: 'IT', value: 'IT' },
  { id: 109, name: 'JM', value: 'JM' },
  { id: 110, name: 'JP', value: 'JP' },
  { id: 111, name: 'JE', value: 'JE' },
  { id: 112, name: 'JO', value: 'JO' },
  { id: 113, name: 'KZ', value: 'KZ' },
  { id: 114, name: 'KE', value: 'KE' },
  { id: 115, name: 'KI', value: 'KI' },
  { id: 116, name: 'KR', value: 'KR' },
  { id: 117, name: 'KW', value: 'KW' },
  { id: 118, name: 'KG', value: 'KG' },
  { id: 119, name: 'LA', value: 'LA' },
  { id: 120, name: 'LV', value: 'LV' },
  { id: 121, name: 'LB', value: 'LB' },
  { id: 122, name: 'LS', value: 'LS' },
  { id: 123, name: 'LR', value: 'LR' },
  { id: 124, name: 'LY', value: 'LY' },
  { id: 125, name: 'LI', value: 'LI' },
  { id: 126, name: 'LT', value: 'LT' },
  { id: 127, name: 'LU', value: 'LU' },
  { id: 128, name: 'MO', value: 'MO' },
  { id: 129, name: 'MK', value: 'MK' },
  { id: 130, name: 'MG', value: 'MG' },
  { id: 131, name: 'MW', value: 'MW' },
  { id: 132, name: 'MY', value: 'MY' },
  { id: 133, name: 'MV', value: 'MV' },
  { id: 134, name: 'ML', value: 'ML' },
  { id: 135, name: 'MT', value: 'MT' },
  { id: 136, name: 'MH', value: 'MH' },
  { id: 137, name: 'MQ', value: 'MQ' },
  { id: 138, name: 'MR', value: 'MR' },
  { id: 139, name: 'MU', value: 'MU' },
  { id: 140, name: 'YT', value: 'YT' },
  { id: 141, name: 'MX', value: 'MX' },
  { id: 142, name: 'FM', value: 'FM' },
  { id: 143, name: 'MD', value: 'MD' },
  { id: 144, name: 'MC', value: 'MC' },
  { id: 145, name: 'MN', value: 'MN' },
  { id: 146, name: 'ME', value: 'ME' },
  { id: 147, name: 'MS', value: 'MS' },
  { id: 148, name: 'MA', value: 'MA' },
  { id: 149, name: 'MZ', value: 'MZ' },
  { id: 150, name: 'MM', value: 'MM' },
  { id: 151, name: 'NA', value: 'NA' },
  { id: 152, name: 'NR', value: 'NR' },
  { id: 153, name: 'NP', value: 'NP' },
  { id: 154, name: 'NL', value: 'NL' },
  { id: 155, name: 'AN', value: 'AN' },
  { id: 156, name: 'NC', value: 'NC' },
  { id: 157, name: 'NZ', value: 'NZ' },
  { id: 158, name: 'NI', value: 'NI' },
  { id: 159, name: 'NE', value: 'NE' },
  { id: 160, name: 'NG', value: 'NG' },
  { id: 161, name: 'NU', value: 'NU' },
  { id: 162, name: 'NF', value: 'NF' },
  { id: 163, name: 'MP', value: 'MP' },
  { id: 164, name: 'NO', value: 'NO' },
  { id: 165, name: 'OM', value: 'OM' },
  { id: 166, name: 'PK', value: 'PK' },
  { id: 167, name: 'PW', value: 'PW' },
  { id: 168, name: 'PA', value: 'PA' },
  { id: 169, name: 'PG', value: 'PG' },
  { id: 170, name: 'PY', value: 'PY' },
  { id: 171, name: 'PE', value: 'PE' },
  { id: 172, name: 'PH', value: 'PH' },
  { id: 173, name: 'PN', value: 'PN' },
  { id: 174, name: 'PL', value: 'PL' },
  { id: 175, name: 'PT', value: 'PT' },
  { id: 176, name: 'PR', value: 'PR' },
  { id: 177, name: 'QA', value: 'QA' },
  { id: 178, name: 'RE', value: 'RE' },
  { id: 179, name: 'RO', value: 'RO' },
  { id: 180, name: 'RU', value: 'RU' },
  { id: 181, name: 'RW', value: 'RW' },
  { id: 182, name: 'BL', value: 'BL' },
  { id: 183, name: 'SH', value: 'SH' },
  { id: 184, name: 'KN', value: 'KN' },
  { id: 185, name: 'LC', value: 'LC' },
  { id: 186, name: 'MF', value: 'MF' },
  { id: 187, name: 'PM', value: 'PM' },
  { id: 188, name: 'VC', value: 'VC' },
  { id: 189, name: 'WS', value: 'WS' },
  { id: 190, name: 'SM', value: 'SM' },
  { id: 191, name: 'ST', value: 'ST' },
  { id: 192, name: 'SA', value: 'SA' },
  { id: 193, name: 'SN', value: 'SN' },
  { id: 194, name: 'RS', value: 'RS' },
  { id: 195, name: 'SC', value: 'SC' },
  { id: 196, name: 'SL', value: 'SL' },
  { id: 197, name: 'SG', value: 'SG' },
  { id: 198, name: 'SK', value: 'SK' },
  { id: 199, name: 'SI', value: 'SI' },
  { id: 200, name: 'SB', value: 'SB' },
  { id: 201, name: 'SO', value: 'SO' },
  { id: 202, name: 'ZA', value: 'ZA' },
  { id: 203, name: 'GS', value: 'GS' },
  { id: 204, name: 'ES', value: 'ES' },
  { id: 205, name: 'LK', value: 'LK' },
  { id: 206, name: 'SD', value: 'SD' },
  { id: 207, name: 'SR', value: 'SR' },
  { id: 208, name: 'SJ', value: 'SJ' },
  { id: 209, name: 'SZ', value: 'SZ' },
  { id: 210, name: 'SE', value: 'SE' },
  { id: 211, name: 'CH', value: 'CH' },
  { id: 212, name: 'SY', value: 'SY' },
  { id: 213, name: 'TW', value: 'TW' },
  { id: 214, name: 'TJ', value: 'TJ' },
  { id: 215, name: 'TZ', value: 'TZ' },
  { id: 216, name: 'TH', value: 'TH' },
  { id: 217, name: 'TL', value: 'TL' },
  { id: 218, name: 'TG', value: 'TG' },
  { id: 219, name: 'TK', value: 'TK' },
  { id: 220, name: 'TO', value: 'TO' },
  { id: 221, name: 'TT', value: 'TT' },
  { id: 222, name: 'TN', value: 'TN' },
  { id: 223, name: 'TR', value: 'TR' },
  { id: 224, name: 'TM', value: 'TM' },
  { id: 225, name: 'TC', value: 'TC' },
  { id: 226, name: 'TV', value: 'TV' },
  { id: 227, name: 'UG', value: 'UG' },
  { id: 228, name: 'UA', value: 'UA' },
  { id: 229, name: 'AE', value: 'AE' },
  { id: 230, name: 'GB', value: 'GB' },
  { id: 231, name: 'US', value: 'US' },
  { id: 232, name: 'UM', value: 'UM' },
  { id: 233, name: 'UY', value: 'UY' },
  { id: 234, name: 'UZ', value: 'UZ' },
  { id: 235, name: 'VU', value: 'VU' },
  { id: 236, name: 'VE', value: 'VE' },
  { id: 237, name: 'VN', value: 'VN' },
  { id: 238, name: 'VG', value: 'VG' },
  { id: 239, name: 'VI', value: 'VI' },
  { id: 240, name: 'WF', value: 'WF' },
  { id: 241, name: 'EH', value: 'EH' },
  { id: 242, name: 'YE', value: 'YE' },
  { id: 243, name: 'ZM', value: 'ZM' },
  { id: 244, name: 'ZW', value: 'ZW' },
];

export const countries = [
  { value: 'All', name: 'All', id: 0 },
  { value: 'Afghanistan', name: 'Afghanistan', id: 1 },
  { value: 'Åland Islands', name: 'Åland Islands', id: 2 },
  { value: 'Albania', name: 'Albania', id: 3 },
  { value: 'Algeria', name: 'Algeria', id: 4 },
  { value: 'American Samoa', name: 'American Samoa', id: 5 },
  { value: 'AndorrA', name: 'AndorrA', id: 6 },
  { value: 'Angola', name: 'Angola', id: 7 },
  { value: 'Anguilla', name: 'Anguilla', id: 8 },
  { value: 'Antarctica', name: 'Antarctica', id: 9 },
  { value: 'Antigua and Barbuda', name: 'Antigua and Barbuda', id: 10 },
  { value: 'Argentina', name: 'Argentina', id: 11 },
  { value: 'Armenia', name: 'Armenia', id: 12 },
  { value: 'Aruba', name: 'Aruba', id: 13 },
  { value: 'Australia', name: 'Australia', id: 14 },
  { value: 'Austria', name: 'Austria', id: 15 },
  { value: 'Azerbaijan', name: 'Azerbaijan', id: 16 },
  { value: 'Bahamas', name: 'Bahamas', id: 17 },
  { value: 'Bahrain', name: 'Bahrain', id: 18 },
  { value: 'Bangladesh', name: 'Bangladesh', id: 19 },
  { value: 'Barbados', name: 'Barbados', id: 20 },
  { value: 'Belarus', name: 'Belarus', id: 21 },
  { value: 'Belgium', name: 'Belgium', id: 22 },
  { value: 'Belize', name: 'Belize', id: 23 },
  { value: 'Benin', name: 'Benin', id: 24 },
  { value: 'Bermuda', name: 'Bermuda', id: 25 },
  { value: 'Bhutan', name: 'Bhutan', id: 26 },
  { value: 'Bolivia', name: 'Bolivia', id: 27 },
  { value: 'Bosnia and Herzegovina', name: 'Bosnia and Herzegovina', id: 28 },
  { value: 'Botswana', name: 'Botswana', id: 29 },
  { value: 'Bouvet Island', name: 'Bouvet Island', id: 30 },
  { value: 'Brazil', name: 'Brazil', id: 31 },
  {
    value: 'British Indian Ocean Territory',
    name: 'British Indian Ocean Territory',
    id: 32,
  },
  { value: 'Brunei Darussalam', name: 'Brunei Darussalam', id: 33 },
  { value: 'Bulgaria', name: 'Bulgaria', id: 34 },
  { value: 'Burkina Faso', name: 'Burkina Faso', id: 35 },
  { value: 'Burundi', name: 'Burundi', id: 36 },
  { value: 'Cambodia', name: 'Cambodia', id: 37 },
  { value: 'Cameroon', name: 'Cameroon', id: 38 },
  { value: 'Canada', name: 'Canada', id: 39 },
  { value: 'Cape Verde', name: 'Cape Verde', id: 40 },
  { value: 'Cayman Islands', name: 'Cayman Islands', id: 41 },
  { value: 'Central African Republic', name: 'Central African Republic', id: 42 },
  { value: 'Chad', name: 'Chad', id: 43 },
  { value: 'Chile', name: 'Chile', id: 44 },
  { value: 'China', name: 'China', id: 45 },
  { value: 'Christmas Island', name: 'Christmas Island', id: 46 },
  { value: 'Cocos (Keeling) Islands', name: 'Cocos (Keeling) Islands', id: 47 },
  { value: 'Colombia', name: 'Colombia', id: 48 },
  { value: 'Comoros', name: 'Comoros', id: 49 },
  { value: 'Congo', name: 'Congo', id: 50 },
  {
    value: 'Congo, The Democratic Republic of the',
    name: 'Congo, The Democratic Republic of the',
    id: 51,
  },
  { value: 'Cook Islands', name: 'Cook Islands', id: 52 },
  { value: 'Costa Rica', name: 'Costa Rica', id: 53 },
  { value: "Cote D'Ivoire", name: "Cote D'Ivoire", id: 54 },
  { value: 'Croatia', name: 'Croatia', id: 55 },
  { value: 'Cuba', name: 'Cuba', id: 56 },
  { value: 'Cyprus', name: 'Cyprus', id: 57 },
  { value: 'Czech Republic', name: 'Czech Republic', id: 58 },
  { value: 'Denmark', name: 'Denmark', id: 59 },
  { value: 'Djibouti', name: 'Djibouti', id: 60 },
  { value: 'Dominica', name: 'Dominica', id: 61 },
  { value: 'Dominican Repbulic', name: 'Dominican Republic', id: 62 },
  { value: 'Ecuador', name: 'Ecuador', id: 63 },
  { value: 'Egypt', name: 'Egypt', id: 64 },
  { value: 'El Salvador', name: 'El Salvador', id: 65 },
  { value: 'Equatorial Guinea', name: 'Equatorial Guinea', id: 66 },
  { value: 'Eritrea', name: 'Eritrea', id: 67 },
  { value: 'Estonia', name: 'Estonia', id: 68 },
  { value: 'Ethiopia', name: 'Ethiopia', id: 69 },
  { value: 'Falkland Islands (Malvinas)', name: 'Falkland Islands (Malvinas)', id: 70 },
  { value: 'Faroe Islands', name: 'Faroe Islands', id: 71 },
  { value: 'Fiji', name: 'Fiji', id: 72 },
  { value: 'Finland', name: 'Finland', id: 73 },
  { value: 'France', name: 'France', id: 74 },
  { value: 'French Guiana', name: 'French Guiana', id: 75 },
  { value: 'French Polynesia', name: 'French Polynesia', id: 76 },
  { value: 'French Southern Territories', name: 'French Southern Territories', id: 77 },
  { value: 'Gabon', name: 'Gabon', id: 78 },
  { value: 'Gambia', name: 'Gambia', id: 79 },
  { value: 'Georgia', name: 'Georgia', id: 80 },
  { value: 'Germany', name: 'Germany', id: 81 },
  { value: 'Ghana', name: 'Ghana', id: 82 },
  { value: 'Gibraltar', name: 'Gibraltar', id: 83 },
  { value: 'Greece', name: 'Greece', id: 84 },
  { value: 'Greenland', name: 'Greenland', id: 85 },
  { value: 'Grenada', name: 'Grenada', id: 86 },
  { value: 'Guadeloupe', name: 'Guadeloupe', id: 87 },
  { value: 'Guam', name: 'Guam', id: 88 },
  { value: 'Guatemala', name: 'Guatemala', id: 89 },
  { value: 'Guersney', name: 'Guernsey', id: 90 },
  { value: 'Guinea', name: 'Guinea', id: 91 },
  { value: 'Guinea-Bissau', name: 'Guinea-Bissau', id: 92 },
  { value: 'Guyana', name: 'Guyana', id: 93 },
  { value: 'Haiti', name: 'Haiti', id: 94 },
  {
    value: 'Heard Island and Mcdonald Islands',
    name: 'Heard Island and Mcdonald Islands',
    id: 95,
  },
  {
    value: 'Holy See (Vatican City State)',
    name: 'Holy See (Vatican City State)',
    id: 96,
  },
  { value: 'Honduras', name: 'Honduras', id: 97 },
  { value: 'Hong Kong', name: 'Hong Kong', id: 98 },
  { value: 'Hungary', name: 'Hungary', id: 99 },
  { value: 'Iceland', name: 'Iceland', id: 100 },
  { value: 'India', name: 'India', id: 101 },
  { value: 'Indonesia', name: 'Indonesia', id: 102 },
  { value: 'Iran, Islamic Republic Of', name: 'Iran, Islamic Republic Of', id: 103 },
  { value: 'Iraq', name: 'Iraq', id: 104 },
  { value: 'Ireland', name: 'Ireland', id: 105 },
  { value: 'Isle of Man', name: 'Isle of Man', id: 106 },
  { value: 'Israel', name: 'Israel', id: 107 },
  { value: 'Italy', name: 'Italy', id: 108 },
  { value: 'Jamaica', name: 'Jamaica', id: 109 },
  { value: 'Japan', name: 'Japan', id: 110 },
  { value: 'Jersey', name: 'Jersey', id: 111 },
  { value: 'Jordan', name: 'Jordan', id: 112 },
  { value: 'Kazakhstan', name: 'Kazakhstan', id: 113 },
  { value: 'Kenya', name: 'Kenya', id: 114 },
  { value: 'Kiribati', name: 'Kiribati', id: 115 },
  {
    value: "Korea, Democratic People'S Republic of",
    name: "Korea, Democratic People'S Republic of",
    id: 116,
  },
  { value: 'Korea, Republic of', name: 'Korea, Republic of', id: 117 },
  { value: 'Kuwait', name: 'Kuwait', id: 118 },
  { value: 'Kyrgyzstan', name: 'Kyrgyzstan', id: 119 },
  {
    value: "Lao People'S Democratic Republic",
    name: "Lao People'S Democratic Republic",
    id: 120,
  },
  { value: 'Latvia', name: 'Latvia', id: 121 },
  { value: 'Lebanon', name: 'Lebanon', id: 122 },
  { value: 'Lesotho', name: 'Lesotho', id: 123 },
  { value: 'Liberia', name: 'Liberia', id: 124 },
  { value: 'Libyan Arab Jamahiriya', name: 'Libyan Arab Jamahiriya', id: 125 },
  { value: 'Liechtenstein', name: 'Liechtenstein', id: 126 },
  { value: 'Lithuania', name: 'Lithuania', id: 127 },
  { value: 'Luxembourg', name: 'Luxembourg', id: 128 },
  { value: 'Macao', name: 'Macao', id: 129 },
  {
    value: 'Macedonia, The Former Yugoslav Republic of',
    name: 'Macedonia, The Former Yugoslav Republic of',
    id: 130,
  },
  { value: 'Madagascar', name: 'Madagascar', id: 131 },
  { value: 'Malawi', name: 'Malawi', id: 132 },
  { value: 'Malaysia', name: 'Malaysia', id: 133 },
  { value: 'Maldives', name: 'Maldives', id: 134 },
  { value: 'Mali', name: 'Mali', id: 135 },
  { value: 'Malta', name: 'Malta', id: 136 },
  { value: 'Marshall Islands', name: 'Marshall Islands', id: 137 },
  { value: 'Martinique', name: 'Martinique', id: 138 },
  { value: 'Mauritania', name: 'Mauritania', id: 139 },
  { value: 'Mauritius', name: 'Mauritius', id: 140 },
  { value: 'Mayotte', name: 'Mayotte', id: 141 },
  { value: 'Mexico', name: 'Mexico', id: 142 },
  {
    value: 'Micronesia, Federated States of',
    name: 'Micronesia, Federated States of',
    id: 143,
  },
  { value: 'Moldova, Republic of', name: 'Moldova, Republic of', id: 144 },
  { value: 'Monaco', name: 'Monaco', id: 145 },
  { value: 'Mongolia', name: 'Mongolia', id: 146 },
  { value: 'Montserrat', name: 'Montserrat', id: 147 },
  { value: 'Morocco', name: 'Morocco', id: 148 },
  { value: 'Mozambique', name: 'Mozambique', id: 149 },
  { value: 'Myanmar', name: 'Myanmar', id: 150 },
  { value: 'Namibia', name: 'Namibia', id: 151 },
  { value: 'Nauru', name: 'Nauru', id: 152 },
  { value: 'Nepal', name: 'Nepal', id: 153 },
  { value: 'Netherlands', name: 'Netherlands', id: 154 },
  { value: 'Netherlands Antilles', name: 'Netherlands Antilles', id: 155 },
  { value: 'New Caledonia', name: 'New Caledonia', id: 156 },
  { value: 'New Zealand', name: 'New Zealand', id: 157 },
  { value: 'Nicaragua', name: 'Nicaragua', id: 158 },
  { value: 'Niger', name: 'Niger', id: 159 },
  { value: 'Nigeria', name: 'Nigeria', id: 160 },
  { value: 'Niue', name: 'Niue', id: 161 },
  { value: 'Norfolk Island', name: 'Norfolk Island', id: 162 },
  { value: 'Northern Mariana Islands', name: 'Northern Mariana Islands', id: 163 },
  { value: 'Norway', name: 'Norway', id: 164 },
  { value: 'Oman', name: 'Oman', id: 165 },
  { value: 'Pakistan', name: 'Pakistan', id: 166 },
  { value: 'Palau', name: 'Palau', id: 167 },
  {
    value: 'Palestinian Territory, Occupied',
    name: 'Palestinian Territory, Occupied',
    id: 168,
  },
  { value: 'Panama', name: 'Panama', id: 169 },
  { value: 'Papua New Guinea', name: 'Papua New Guinea', id: 170 },
  { value: 'Paraguay', name: 'Paraguay', id: 171 },
  { value: 'Peru', name: 'Peru', id: 172 },
  { value: 'Philippines', name: 'Philippines', id: 173 },
  { value: 'Pitcairn', name: 'Pitcairn', id: 174 },
  { value: 'Poland', name: 'Poland', id: 175 },
  { value: 'Portugal', name: 'Portugal', id: 176 },
  { value: 'Puerto Rico', name: 'Puerto Rico', id: 177 },
  { value: 'Qatar', name: 'Qatar', id: 178 },
  { value: 'Reunion', name: 'Reunion', id: 179 },
  { value: 'Romania', name: 'Romania', id: 180 },
  { value: 'Russian Federation', name: 'Russian Federation', id: 181 },
  { value: 'RWANDA', name: 'RWANDA', id: 182 },
  { value: 'Saint Helena', name: 'Saint Helena', id: 183 },
  { value: 'Saint Kitts and Nevis', name: 'Saint Kitts and Nevis', id: 184 },
  { value: 'Saint Lucia', name: 'Saint Lucia', id: 185 },
  { value: 'Saint Pierre and Miquelon', name: 'Saint Pierre and Miquelon', id: 186 },
  {
    value: 'Saint Vincent and the Grenadines',
    name: 'Saint Vincent and the Grenadines',
    id: 187,
  },
  { value: 'Samoa', name: 'Samoa', id: 188 },
  { value: 'San Marino', name: 'San Marino', id: 189 },
  { value: 'Sao Tome and Principe', name: 'Sao Tome and Principe', id: 190 },
  { value: 'Saudi Arabia', name: 'Saudi Arabia', id: 191 },
  { value: 'Senegal', name: 'Senegal', id: 192 },
  { value: 'Serbia and Montenegro', name: 'Serbia and Montenegro', id: 193 },
  { value: 'Seychelles', name: 'Seychelles', id: 194 },
  { value: 'Sierra Leone', name: 'Sierra Leone', id: 195 },
  { value: 'Singapore', name: 'Singapore', id: 196 },
  { value: 'Slovakia', name: 'Slovakia', id: 197 },
  { value: 'Slovenia', name: 'Slovenia', id: 198 },
  { value: 'Solomon Islands', name: 'Solomon Islands', id: 199 },
  { value: 'Somalia', name: 'Somalia', id: 200 },
  { value: 'South Africa', name: 'South Africa', id: 201 },
  {
    value: 'South Georgia and the South Sandwich Islands',
    name: 'South Georgia and the South Sandwich Islands',
    id: 202,
  },
  { value: 'Spain', name: 'Spain', id: 203 },
  { value: 'Sri Lanka', name: 'Sri Lanka', id: 204 },
  { value: 'Sudan', name: 'Sudan', id: 205 },
  { value: 'Suriname', name: 'Suriname', id: 206 },
  { value: 'Svalbard and Jan Mayen', name: 'Svalbard and Jan Mayen', id: 207 },
  { value: 'Swaziland', name: 'Swaziland', id: 208 },
  { value: 'Sweden', name: 'Sweden', id: 209 },
  { value: 'Switzerland', name: 'Switzerland', id: 210 },
  { value: 'Syrian Arab Republic', name: 'Syrian Arab Republic', id: 211 },
  { value: 'Taiwan, Province of China', name: 'Taiwan, Province of China', id: 212 },
  { value: 'Tajikistan', name: 'Tajikistan', id: 213 },
  {
    value: 'Tanzania, United Republic of',
    name: 'Tanzania, United Republic of',
    id: 214,
  },
  { value: 'Thailand', name: 'Thailand', id: 215 },
  { value: 'Timor-Leste', name: 'Timor-Leste', id: 216 },
  { value: 'Togo', name: 'Togo', id: 217 },
  { value: 'Tokelau', name: 'Tokelau', id: 218 },
  { value: 'Tonga', name: 'Tonga', id: 219 },
  { value: 'Trinidad and Tobago', name: 'Trinidad and Tobago', id: 220 },
  { value: 'Tunisia', name: 'Tunisia', id: 221 },
  { value: 'Turkey', name: 'Turkey', id: 222 },
  { value: 'Turkmenistan', name: 'Turkmenistan', id: 223 },
  { value: 'Turks and Caicos Islands', name: 'Turks and Caicos Islands', id: 224 },
  { value: 'Tuvalu', name: 'Tuvalu', id: 225 },
  { value: 'Uganda', name: 'Uganda', id: 226 },
  { value: 'Ukraine', name: 'Ukraine', id: 227 },
  { value: 'United Arab Emirates', name: 'United Arab Emirates', id: 228 },
  { value: 'United Kingdom', name: 'United Kingdom', id: 229 },
  { value: 'United States', name: 'United States', id: 230 },
  {
    value: 'United States Minor Outlying Islands',
    name: 'United States Minor Outlying Islands',
    id: 231,
  },
  { value: 'Uruguay', name: 'Uruguay', id: 232 },
  { value: 'Uzbekistan', name: 'Uzbekistan', id: 233 },
  { value: 'Vanuatu', name: 'Vanuatu', id: 234 },
  { value: 'Venezuela', name: 'Venezuela', id: 235 },
  { value: 'Viet Nam', name: 'Viet Nam', id: 236 },
  { value: 'Virgin Islands, British', name: 'Virgin Islands, British', id: 237 },
  { value: 'Virgin Islands, U.S.', name: 'Virgin Islands, U.S.', id: 238 },
  { value: 'Wallis and Futuna', name: 'Wallis and Futuna', id: 239 },
  { value: 'Western Sahara', name: 'Western Sahara', id: 240 },
  { value: 'Yemen', name: 'Yemen', id: 241 },
  { value: 'Zambia', name: 'Zambia', id: 242 },
  { value: 'Zimbabwe', name: 'Zimbabwe', id: 243 },
];
