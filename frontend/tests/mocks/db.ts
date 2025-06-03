import { factory, manyOf, oneOf, primaryKey } from '@mswjs/data';
import { faker } from '@faker-js/faker';
import { getFullName, getNameAbbreviation } from '../utils';
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

export const db = factory({
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
    abbreviation: () => getNameAbbreviation(),
    email: () => faker.internet.email(),
    firstName: () => getFullName()[0],
    lastName: () => getFullName()[1],
    fullName: () => getFullName(),
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
    description: () => faker.lorem.sentence(),
  },
});
