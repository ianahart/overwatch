import { factory, manyOf, oneOf, primaryKey } from '@mswjs/data';
import { faker } from '@faker-js/faker';
import { getFullName } from '../utils';
import { NotificationRole, NotificationType } from '../../src/enums';
let compatibleProgrammingLanguageIdCounter = 1;
let userIdCounter = 1;
let profileIdCounter = 1;
let packageItemIdCounter = 1;
let skillIdCounter = 1;
let workExpIdCounter = 1;
let availabilityIdCounter = 1;
let slotIdCounter = 1;
let settingIdCounter = 1;
let minCommentIdCounter = 1;
let replyCommentIdCounter = 1;
let saveCommentIdCounter = 1;
let topicIdCounter = 1;
let tagIdCounter = 1;
let tokenIdCounter = 1;
let minProfileIdCounter = 1;
let notificationIdCounter = 1;
let commentIdCounter = 1;
let reactionIdCounter = 1;
let reviewIdCounter = 1;
let testimonialIdCounter = 1;
let reviewerIdCounter = 1;
let teamInvitationIdCounter = 1;
let teamIdCounter = 1;
let teamMemberTeamIdCounter = 1;
let teamMessageIdCounter = 1;
let teamMemberIdCounter = 1;
let teamCommentIdCounter = 1;
let teamPostIdCounter = 1;
let teamPinnedMessageIdCounter = 1;
let connectionIdCounter = 1;
let phoneIdCounter = 1;
let blockedUserIdCounter = 1;
let repositoryIdCounter = 1;
let locationIdCounter = 1;
let pinnedConnectionIdCounter = 1;
let messageIdCounter = 1;
let paymentIntentIdCounter = 1;
let suggestionIdCounter = 1;
let gitHubRepositoryPreviewIdCounter = 1;
let appTestimonialIdCounter = 1;
let minAppTestimonialIdCounter = 1;
let reviewFeedbackIdCounter = 1;
let badgeIdCounter = 1;
let gitHubRepositoryIdCounter = 1;
let statisticTopRequestersIdCounter = 1;
let statisticAvgRatingIdCounter = 1;
let statisticStatusTypeIdCounter = 1;
let statisticMainLanguageIdCounter = 1;
let statisticReviewTypesCompletedIdCounter = 1;
let statisticReviewsCompletedIdCounter = 1;
let statisticAvgReviewTimeIdCounter = 1;
let todoListIdCounter = 1;
let workSpaceIdCounter = 1;
let todoCardIdCounter = 1;
let checkListIdCounter = 1;
let checkListItemIdCounter = 1;
let activeLabelIdCounter = 1;
let labelIdCounter = 1;
let activityIdCounter = 1;
let customFieldIdCounter = 1;
let dropDownOptionIdCounter = 1;
let viewUserIdCounter = 1;
let adminAppTestimonialIdCounter = 1;
const fullName = getFullName();

export const db = factory({
  adminAppTestimonial: {
    id: primaryKey(() => adminAppTestimonialIdCounter++),
    isSelected: () => faker.datatype.boolean(),
    firstName: () => faker.person.firstName(),
    developerType: () => faker.helpers.arrayElement(['backend', 'frontend', 'software developer']),
    createdAt: () => faker.date.recent().toString(),
    content: () => faker.lorem.paragraph(3),
    avatarUrl: () => faker.image.avatar(),
  },
  viewUser: {
    id: primaryKey(() => viewUserIdCounter++),
    firstName: () => faker.person.firstName(),
    lastName: () => faker.person.lastName(),
    avatarUrl: () => faker.image.avatar(),
    role: () => faker.helpers.arrayElement(['ADMIN', 'USER', 'REVIEWER']),
    email: () => faker.internet.email(),
    createdAt: () => faker.date.recent().toString(),
  },
  dropDownOption: {
    id: primaryKey(() => dropDownOptionIdCounter++),
    customFieldId: oneOf('customField'),
    optionValue: () => faker.helpers.arrayElement(['low', 'medium', 'high']),
  },

  customField: {
    id: primaryKey(() => customFieldIdCounter++),
    userId: oneOf('user'),
    todoCardId: oneOf('todoCard'),
    fieldType: () => 'CHECKBOX',
    fieldName: () => 'checkbox',
    selectedValue: () => 'low',
    isActive: () => faker.datatype.boolean(),
    dropDownOptions: manyOf('dropDownOption'),
  },

  activity: {
    id: primaryKey(() => activityIdCounter++),
    userId: oneOf('user'),
    todoCardId: oneOf('todoCard'),
    text: () => faker.lorem.sentence(20),
    createdAt: () => faker.date.recent().toString(),
    avatarUrl: () => faker.image.avatar(),
  },

  label: {
    id: primaryKey(() => labelIdCounter++),
    userId: oneOf('user'),
    workSpaceId: oneOf('workSpace'),
    createdAt: () => faker.date.recent().toString(),
    isChecked: () => faker.datatype.boolean(),
    title: () => faker.lorem.word(8),
    color: () => faker.color.human(),
  },

  activeLabel: {
    id: primaryKey(() => activeLabelIdCounter++),
    todoCardId: oneOf('todoCard'),
    labelId: oneOf('label'),
    color: () => faker.color.human(),
    title: () => faker.lorem.word(8),
  },

  checkListItem: {
    id: primaryKey(() => checkListItemIdCounter++),
    userId: oneOf('user'),
    checkListId: oneOf('checkList'),
    title: () => faker.lorem.word(8),
    isCompleted: () => faker.datatype.boolean(),
  },

  checkList: {
    id: primaryKey(() => checkListIdCounter++),
    userId: oneOf('user'),
    todoCardId: oneOf('todoCard'),
    createdAt: () => faker.date.recent().toString(),
    isCompleted: () => faker.datatype.boolean(),
    title: () => faker.lorem.word(8),
    checkListItems: manyOf('checkListItem'),
  },

  todoCard: {
    todoListId: oneOf('todoList'),
    userId: oneOf('user'),
    id: primaryKey(() => todoCardIdCounter++),
    createdAt: () => faker.date.recent().toString(),
    label: () => faker.lorem.word(8),
    title: () => faker.lorem.word(8),
    color: () => faker.color.human(),
    index: () => faker.number.int({ min: 0, max: 5 }),
    details: () => faker.lorem.paragraph(2),
    startDate: () => faker.date.recent().toString(),
    endDate: () => faker.date.future(),
    photo: () => faker.image.url(),
    todoListTitle: () => faker.lorem.word(8),
    uploadPhotoUrl: () => faker.image.url(),
  },

  workSpace: {
    id: primaryKey(() => workSpaceIdCounter++),
    createdAt: () => faker.date.recent().toString(),
    userId: oneOf('user'),
    title: () => faker.lorem.word(8),
    backgroundColor: () => faker.color.human(),
  },

  todoList: {
    id: primaryKey(() => todoListIdCounter++),
    userId: oneOf('user'),
    workSpaceId: oneOf('workSpace'),
    title: () => faker.lorem.word(8),
    index: () => faker.number.int({ min: 0, max: 5 }),
    createdAt: () => faker.date.recent().toString(),
    cards: manyOf('todoCard'),
  },

  statisticTopRequesters: {
    id: primaryKey(() => statisticTopRequestersIdCounter++),
    fullName: () => faker.person.fullName(),
    count: () => faker.number.int({ min: 1, max: 10 }),
  },

  statisticStatusType: {
    id: primaryKey(() => statisticStatusTypeIdCounter++),
    status: () => faker.helpers.arrayElement(['incomplete', 'complete', 'paid']),
    count: () => faker.number.int({ min: 1, max: 10 }),
  },

  statisticReviewsCompleted: {
    id: primaryKey(() => statisticReviewsCompletedIdCounter++),

    day: () => faker.helpers.arrayElement(['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']),
    reviewsCompleted: () => faker.number.int({ min: 1, max: 10 }),
  },

  statisticReviewTypesCompleted: {
    id: primaryKey(() => statisticReviewTypesCompletedIdCounter++),
    reviewType: () => faker.helpers.arrayElement(['Bug-Fix', 'Optimization', 'Performance']),
    count: () => faker.number.int({ min: 1, max: 10 }),
  },

  statisticMainLanguage: {
    id: primaryKey(() => statisticMainLanguageIdCounter++),
    lanugage: () => faker.helpers.arrayElement(['JavaScript', 'RUBY', 'PYTHON', 'PHP']),
    count: () => faker.number.int({ min: 1, max: 10 }),
  },

  statisticAvgReviewTime: {
    id: primaryKey(() => statisticAvgReviewTimeIdCounter++),
    month: () =>
      faker.helpers.arrayElement(['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']),
    avgReviewTime: () => faker.date.past().toString(),
  },

  statisticAvgRating: {
    id: primaryKey(() => statisticAvgRatingIdCounter++),
    name: () => faker.helpers.arrayElement(['one', 'two', 'three', 'four', 'five']),
    average: () => faker.number.int({ min: 1, max: 5 }),
  },

  gitHubRepository: {
    id: primaryKey(() => gitHubRepositoryIdCounter++),
    comment: () => faker.lorem.sentence(10),
    createdAt: () => faker.date.recent().toString(),
    updatedAt: () => faker.date.recent().toString(),
    feedback: () => faker.lorem.paragraph(3),
    language: () => 'JavaScript',
    ownerId: oneOf('user'),
    repoName: () => faker.lorem.word(10),
    repoUrl: () => faker.internet.domainName(),
    reviewerId: oneOf('user'),
    status: () => faker.helpers.arrayElement(['COMPLETED', 'INCOMPLETE', 'INPROGRESS', 'PAID']),
    reviewStartTime: () => faker.date.recent().toString(),
    reviewEndTime: () => faker.date.recent().toString(),
    reviewDuration: () => faker.date.future().toString(),
    reviewType: () => faker.helpers.arrayElement(['BUG', 'FEATURE', 'OPTIMIZATION']),
    avatarUrl: () => faker.image.avatar(),
  },

  badge: {
    id: primaryKey(() => badgeIdCounter++),
    badgeId: () => faker.number.int({ min: 1, max: 20 }),
    reviewerId: oneOf('user'),
    createdAt: () => faker.date.recent().toString(),
    title: () => faker.lorem.word(8),
    description: () => faker.lorem.paragraph(1),
    imageUrl: () => faker.image.url(),
  },

  reviewFeedback: {
    id: primaryKey(() => reviewFeedbackIdCounter++),
    clarity: () => faker.number.int({ min: 1, max: 5 }),
    helpfulness: () => faker.number.int({ min: 1, max: 5 }),
    thoroughness: () => faker.number.int({ min: 1, max: 5 }),
    responseTime: () => faker.number.int({ min: 1, max: 5 }),
    repositoryId: oneOf('repository'),
    reviewerId: oneOf('user'),
    createdAt: () => faker.date.recent().toString(),
  },

  minAppTestimonial: {
    id: primaryKey(() => minAppTestimonialIdCounter++),
    developerType: () =>
      faker.helpers.arrayElements(['backend developer', 'frontend developer', 'software engineer', 'devops'], 1)[0],
    content: () => faker.lorem.paragraph(2),
  },

  appTestimonial: {
    id: primaryKey(() => appTestimonialIdCounter++),
    firstName: () => faker.person.firstName(),
    developerType: () =>
      faker.helpers.arrayElements(['backend developer', 'frontend developer', 'software engineer', 'devops'], 1),
    content: () => faker.lorem.paragraph(2),
    avatarUrl: () => faker.image.avatar(),
  },

  gitHubRepositoryPreview: {
    id: primaryKey(() => gitHubRepositoryPreviewIdCounter++),
    fullName: () => faker.person.fullName(),
    avatarUrl: () => faker.image.avatar(),
    htmlUrl: () => faker.internet.domainName(),
    language: () => faker.helpers.arrayElements(['Java', 'JavaScript', 'Python', 'Ruby'], 1),
    stargazersCount: () => faker.number.int({ min: 1, max: 1000 }),
  },
  suggestion: {
    id: primaryKey(() => suggestionIdCounter++),
    createdAt: () => faker.date.recent().toString(),
    title: () => faker.lorem.word(8),
    description: () => faker.lorem.paragraph(2),
    contact: () => faker.internet.email(),
    fileUrl: () => faker.internet.domainName(),
    feedbackType: () =>
      faker.helpers.arrayElements(['BUG_REPORT', 'FEATURE_REQUEST', 'GENERAL_FEEDBACK', 'SUGGESTION']),
    priorityLevel: () => faker.helpers.arrayElements(['LOW', 'MEDIUM', 'HIGH']),
    feedbackStatus: () => faker.helpers.arrayElements(['ACCEPTED', 'PENDING', 'REJECTED']),
    fullName: () => faker.person.fullName(),
    avatarUrl: () => faker.image.avatar(),
  },

  paymentIntent: {
    id: primaryKey(() => paymentIntentIdCounter++),
    amount: () => faker.number.int({ min: 1000, max: 10000 }),
    currency: () => 'USD',
    fullName: () => faker.person.fullName(),
    reviewerId: oneOf('user'),
    avatarUrl: () => faker.image.avatar(),
    createdAt: () => faker.date.recent().toString(),
    status: () => faker.helpers.arrayElement(['REFUNDED', 'PAID']),
  },
  message: {
    firstName: () => faker.person.firstName(),
    lastName: () => faker.person.lastName(),
    createdAt: () => faker.date.recent().toString(),
    text: () => faker.lorem.paragraph(1),
    avatarUrl: () => faker.image.avatar(),
    id: primaryKey(() => messageIdCounter++),
    connectionId: oneOf('connection'),
    userId: oneOf('user'),
  },

  location: {
    id: primaryKey(() => locationIdCounter++),
    address: () => faker.location.streetAddress(),
    addressTwo: () => faker.location.streetAddress(),
    city: () => faker.location.city(),
    country: () => faker.location.country(),
    state: () => faker.location.state(),
    zipCode: () => faker.location.zipCode(),
    phoneNumber: () => faker.phone.number(),
  },
  repository: {
    id: primaryKey(() => repositoryIdCounter++),
    avatarUrl: () => faker.image.avatar(),
    firstName: () => faker.person.firstName(),
    language: () => 'JavaScript',
    lastName: () => faker.person.lastName(),
    ownerId: oneOf('user'),
    profileUrl: () => faker.internet.domainName(),
    repoName: () => faker.lorem.word(10),
    repoUrl: () => faker.internet.domainName(),
    reviewerId: oneOf('user'),
    status: () => faker.helpers.arrayElement(['COMPLETED', 'INCOMPLETE', 'INPROGRESS', 'PAID']),
    reviewStartTime: () => faker.date.recent().toString(),
    reviewEndTime: () => faker.date.recent().toString(),
    feedback: () => faker.lorem.paragraph(3),
    paymentPrice: () => faker.number.int({ min: 1, max: 100 }),
  },

  blockedUser: {
    createdAt: () => faker.date.recent().toString(),
    fullName: () => faker.person.fullName(),
    avatarUrl: () => faker.image.avatar(),
    id: primaryKey(() => blockedUserIdCounter++),
    blockedUserId: oneOf('user'),
  },

  phone: {
    id: primaryKey(() => phoneIdCounter++),
    isVerified: () => faker.datatype.boolean(),
    createdAt: () => faker.date.recent().toString(),
    phoneNumber: () => faker.phone.number().toString(),
  },

  pinnedConnection: {
    senderId: oneOf('user'),
    receiverId: oneOf('user'),
    phoneNumber: () => faker.phone.number(),
    lastName: () => faker.person.lastName(),
    id: primaryKey(() => pinnedConnectionIdCounter++),
    firstName: () => faker.person.firstName(),
    email: () => faker.internet.email(),
    country: () => faker.location.country(),
    city: () => faker.location.city(),
    bio: () => faker.lorem.paragraph(3),
    avatarUrl: () => faker.image.avatar(),
    lastMessage: () => faker.lorem.sentence(10),
    connectionPinId: () => faker.number.int({ min: 1, max: 100 }),
  },

  connection: {
    senderId: oneOf('user'),
    receiverId: oneOf('user'),
    phoneNumber: () => faker.phone.number(),
    lastName: () => faker.person.lastName(),
    id: primaryKey(() => connectionIdCounter++),
    firstName: () => faker.person.firstName(),
    email: () => faker.internet.email(),
    country: () => faker.location.country(),
    city: () => faker.location.city(),
    bio: () => faker.lorem.paragraph(3),
    avatarUrl: () => faker.image.avatar(),
    lastMessage: () => faker.lorem.sentence(10),
  },
  teamPost: {
    id: primaryKey(() => teamPostIdCounter++),
    teamId: oneOf('team'),
    createdAt: () => faker.date.recent.toString(),
    userId: oneOf('user'),
    code: () => faker.lorem.sentence(8),
    isEdited: () => faker.datatype.boolean(),
    fullName: () => faker.person.fullName(),
    avatarUrl: () => faker.image.avatar(),
    language: () => faker.lorem.word(8),
    hasComments: () => faker.datatype.boolean(),
  },

  teamComment: {
    id: primaryKey(() => teamCommentIdCounter++),
    userId: oneOf('user'),
    content: () => faker.lorem.paragraph(1),
    createdAt: () => faker.date.recent.toString(),
    fullName: () => faker.person.fullName(),
    avatarUrl: () => faker.image.avatar(),
    teamPostId: oneOf('teamPost'),
    isEdited: () => faker.datatype.boolean(),
    tag: () => faker.lorem.word(8),
  },
  teamMember: {
    id: primaryKey(() => teamMemberIdCounter++),
    userId: oneOf('user'),
    teamId: oneOf('team'),
    avatarUrl: () => faker.image.avatar(),
    fullName: () => faker.person.fullName(),
    createdAt: () => faker.date.recent().toString(),
    profileId: oneOf('fullProfile'),
  },

  teamPinnedMessage: {
    id: primaryKey(() => teamPinnedMessageIdCounter++),
    userId: oneOf('user'),
    fullName: () => faker.person.fullName(),
    avatarUrl: () => faker.image.avatar(),
    createdAt: () => faker.date.recent.toString(),
    message: () => faker.lorem.sentence(10),
    isEdited: () => faker.datatype.boolean(),
    updatedAt: () => faker.date.recent.toString(),
  },

  teamMessage: {
    id: primaryKey(() => teamMessageIdCounter++),
    fullName: () => faker.person.fullName(),
    createdAt: () => faker.date.recent.toString(),
    text: () => faker.lorem.paragraph(2),
    avatarUrl: () => faker.image.avatar(),
    userId: oneOf('user'),
    teamId: oneOf('team'),
  },
  teamMemberTeam: {
    id: primaryKey(() => teamMemberTeamIdCounter++),
    userId: oneOf('user'),
    teamId: oneOf('team'),
    teamName: () => faker.lorem.word(10),
  },
  team: {
    id: primaryKey(() => teamIdCounter++),
    userId: oneOf('user'),
    totalTeams: () => faker.number.int({ min: 1, max: 10 }),
    teamName: () => faker.lorem.word(10),
    teamDescription: () => faker.lorem.sentence(4),
  },

  teamInvitation: {
    id: primaryKey(() => teamInvitationIdCounter++),
    senderId: oneOf('user'),
    receiverId: oneOf('user'),
    teamId: oneOf('team'),
    status: () => faker.lorem.word(8),
    senderAvatarUrl: () => faker.image.avatar(),
    senderFullName: () => faker.person.fullName(),
    createdAt: () => faker.date.recent().toString(),
    teamName: () => faker.lorem.word(10),
  },
  reviewer: {
    id: primaryKey(() => reviewerIdCounter++),
    fullName: () => faker.person.fullName(),
    avatarUrl: () => faker.image.avatar(),
  },

  testimonial: {
    id: primaryKey(() => testimonialIdCounter++),
    userId: oneOf('user'),
    name: () => getFullName(),
    text: () => faker.lorem.paragraph(3),
    createdAt: () => faker.date.recent().toString(),
  },

  review: {
    id: primaryKey(() => reviewIdCounter++),
    authorId: oneOf('user'),
    avatarUrl: () => faker.image.avatar(),
    rating: () => faker.number.int({ min: 1, max: 5 }),
    review: () => faker.lorem.paragraph(5),
    createdAt: () => faker.date.recent().toString(),
    isEdited: () => faker.datatype.boolean(),
    name: () => getFullName(),
  },

  reaction: {
    id: primaryKey(() => reactionIdCounter++),
    emoji: () => faker.internet.emoji(),
    count: () => faker.number.int(5),
  },

  comment: {
    id: primaryKey(() => commentIdCounter++),
    content: () => faker.lorem.paragraph(3),
    userId: oneOf('user'),
    createdAt: () => faker.date.recent().toString(),
    avatarUrl: () => faker.image.avatar(),
    fullName: () => getFullName(),
    isEdited: () => faker.datatype.boolean(),
    voteDifference: () => faker.number.int(10),
    curUserVoteType: () => faker.lorem.word(8),
    curUserHasVoted: () => faker.datatype.boolean(),
    curUserHasSaved: () => faker.datatype.boolean(),
    replyCommentsCount: () => faker.number.int(10),
    reactions: manyOf('reaction'),
  },
  notification: {
    id: primaryKey(() => notificationIdCounter++),
    createdAt: () => faker.date.recent().toString(),
    text: () => faker.lorem.sentence(),
    receiverId: oneOf('user'),
    senderId: oneOf('user'),
    avatarUrl: () => faker.image.avatar(),
    notificationType: () => NotificationType.REVIEW_COMPLETED,
    notificationRole: () => NotificationRole.SENDER,
    link: () => faker.internet.url(),
  },

  compatibleProgrammingLanguage: {
    id: primaryKey(() => compatibleProgrammingLanguageIdCounter++),
    name: () => faker.lorem.word(10),
    isCompatible: () => faker.datatype.boolean(),
  },

  minProfile: {
    id: primaryKey(() => minProfileIdCounter++),
    availability: manyOf('availability'),
    programmingLanguages: manyOf('compatibleProgrammingLanguage'),
    avatarUrl: () => faker.image.url(),
    country: () => faker.location.country(),
    fullName: () => faker.person.fullName(),
    basic: {
      id: () => 'package-1',
      name: () => 'Basic Package',
      price: () => faker.commerce.price(),
      description: () => faker.lorem.sentence(),
      items: manyOf('packageItem'),
    },
    userId: oneOf('user'),
    numOfReviews: () => faker.number.int({ min: 0, max: 10 }),
    createdAt: () => faker.date.recent.toString(),
    weekendsAvailable: () => faker.datatype.boolean(),
    reviewAvgRating: () => faker.number.int({ min: 0, max: 5 }),
    isFavorited: () => faker.datatype.boolean(),
    lastActive: () => faker.date.recent.toString(),
    lastActiveReadable: () => faker.lorem.word(8),
  },

  token: {
    id: primaryKey(() => tokenIdCounter++),
    token: () => faker.lorem.word(20),
    refreshToken: () => faker.lorem.word(20),
  },

  tag: {
    id: primaryKey(() => tagIdCounter++),
    name: () => faker.lorem.word(10),
  },

  topic: {
    id: primaryKey(() => topicIdCounter++),
    title: () => faker.lorem.word(15),
    description: () => faker.lorem.paragraph(2),
    tags: manyOf('tag'),
    totalCommentCount: () => faker.number.int(10),
  },

  saveComment: {
    id: primaryKey(() => saveCommentIdCounter++),
    userId: oneOf('user'),
    content: () => faker.lorem.sentence(30),
    createdAt: () => faker.date.recent.toString(),
    avatarUrl: () => faker.image.url(),
    fullName: () => getFullName(),
  },

  replyComment: {
    id: primaryKey(() => replyCommentIdCounter++),
    userId: oneOf('user'),
    content: () => faker.lorem.sentence(30),
    createdAt: () => faker.date.recent.toString(),
    avatarUrl: () => faker.image.url(),
    fullName: () => getFullName(),
  },

  minComment: {
    id: primaryKey(() => minCommentIdCounter++),
    userId: oneOf('user'),
    content: () => faker.lorem.sentence(10),
    createdAt: () => faker.date.recent.toString(),
    avatarUrl: () => faker.image.url(),
    fullName: () => getFullName(),
  },

  setting: {
    id: primaryKey(() => settingIdCounter++),
    userId: oneOf('user'),
    mfaEnabled: () => faker.datatype.boolean(),
    createdAt: () => faker.date.soon().toISOString(),
    requestAcceptedNotifOn: () => faker.datatype.boolean(),
    requestPendingNotifOn: () => faker.datatype.boolean(),
    paymentAcknowledgementNotifOn: () => faker.datatype.boolean(),
    reviewInProgressNotifOn: () => faker.datatype.boolean(),
    reviewInCompleteNotifOn: () => faker.datatype.boolean(),
    reviewCompleteNotifOn: () => faker.datatype.boolean(),
    commentReplyOn: () => faker.datatype.boolean(),
    emailOn: () => faker.datatype.boolean(),
  },

  user: {
    id: primaryKey(() => userIdCounter++),
    avatarUrl: () => faker.image.url(),
    abbreviation: () => fullName.split(' ')[0][0] + '.' + fullName.split(' ')[1][0],
    email: () => faker.internet.email(),
    firstName: () => fullName.split(' ')[0],
    lastName: () => fullName.split(' ')[1],
    fullName: () => fullName,
    loggedIn: () => faker.datatype.boolean(),
    profileId: oneOf('fullProfile'),
    role: () => 'REVIEWER',
    settingId: oneOf('setting'),
    slug: () => getFullName().split(' ').join('-'),
  },

  fullProfile: {
    id: primaryKey(() => profileIdCounter++),
    profile: {
      id: () => profileIdCounter.toString(),
      userId: oneOf('user'),
      role: () => faker.helpers.arrayElement(['ADMIN', 'USER', 'REVIEWER']),
      country: () => faker.location.country(),
      abbreviation: () => `${faker.person.firstName()[0]}.${faker.person.lastName()}`,
      city: () => faker.location.city(),
    },
    basicInfo: {
      fullName: () => getFullName(),
      userName: () => faker.internet.displayName(),
      email: () => faker.internet.email(),
      contactNumber: () => faker.phone.number(),
    },
    additionalInfo: {
      availability: manyOf('availability'),
      moreInfo: () => faker.lorem.sentence(),
    },
    pckg: {
      basic: {
        id: () => 'package-1',
        name: () => 'Basic Package',
        price: () => faker.commerce.price(),
        description: () => faker.lorem.sentence(),
        items: manyOf('packageItem'),
      },
      standard: {
        id: () => 'package-2',
        name: () => 'Standard Package',
        price: () => faker.commerce.price(),
        description: () => faker.lorem.sentence(),
        items: manyOf('packageItem'),
      },
      pro: {
        id: () => 'package-3',
        name: () => 'Pro Package',
        price: () => faker.commerce.price(),
        description: () => faker.lorem.sentence(),
        items: manyOf('packageItem'),
      },
    },
    profileSetup: {
      avatar: () => faker.image.avatar(),
      tagLine: () => faker.lorem.words(),
      bio: () => faker.lorem.paragraph(),
    },
    skills: {
      languages: manyOf('language'),
      programmingLanguages: manyOf('programmingLanguage'),
      qualifications: manyOf('qualification'),
    },
    workExp: {
      workExps: manyOf('workExperience'),
    },
  },

  availability: {
    id: primaryKey(() => availabilityIdCounter++),
    day: () => faker.helpers.arrayElement(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday']),
    slots: manyOf('slot'),
  },

  slot: {
    id: primaryKey(() => slotIdCounter++),
    startTime: () => faker.date.recent().toString(),
    endTime: () => faker.date.recent().toString(),
  },

  packageItem: {
    id: primaryKey(() => packageItemIdCounter++),
    name: () => faker.commerce.productName(),
    price: () => faker.commerce.price(),
    isEditing: () => faker.datatype.boolean(),
  },

  language: {
    id: primaryKey(() => skillIdCounter++),
    name: () => faker.helpers.arrayElement(['English', 'Spanish', 'French', 'German']),
  },

  programmingLanguage: {
    id: primaryKey(() => skillIdCounter++),
    name: () => faker.helpers.arrayElement(['JavaScript', 'Python', 'Java', 'Go']),
  },

  qualification: {
    id: primaryKey(() => skillIdCounter++),
    name: () =>
      faker.helpers.arrayElement(['BSc in Computer Science', 'MSc in Software Engineering', 'PhD in Data Science']),
  },

  workExperience: {
    id: primaryKey(() => workExpIdCounter++),
    title: () => faker.person.jobTitle(),
    company: () => faker.company.name(),
    startDate: () => faker.date.past(),
    endDate: () => faker.date.recent(),
    desc: () => faker.lorem.sentence(),
  },
});
