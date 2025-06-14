import { toPlainObject } from 'lodash';
import {
  IComment,
  IFetchProfileResponse,
  IFullProfile,
  IReaction,
  IReplyComment,
  IReview,
  IReviewer,
  ITag,
  ITeamComment,
  ITeamInvitiation,
  ITeamMember,
  ITeamMemberTeam,
  ITeamMessage,
  ITeamPost,
  ITestimonial,
} from '../../src/interfaces';
import { db } from './db';
import { faker } from '@faker-js/faker';

export function createTeamPosts(numberOfPosts: number) {
  const teamPosts: ITeamPost[] = [];

  for (let i = 0; i < numberOfPosts; i++) {
    const teamPost: ITeamPost = toPlainObject(db.teamPost.create());
    teamPosts.push(teamPost);
  }

  return teamPosts;
}

export function createTeamMember(numberOfTeamMembers: number) {
  const teamMembers: ITeamMember[] = [];

  for (let i = 0; i < numberOfTeamMembers; i++) {
    const teamMember = toPlainObject(db.teamMember.create());
    teamMembers.push(teamMember);
  }

  return teamMembers;
}

export function createTeamComments(numberOfComments: number) {
  const teamComments: ITeamComment[] = [];

  for (let i = 0; i < numberOfComments; i++) {
    const teamComment = toPlainObject(db.teamComment.create());
    teamComments.push(teamComment);
  }
  return teamComments;
}

export function createTeamMessages(numberOfMessages: number) {
  const teamMessages: ITeamMessage[] = [];

  for (let i = 0; i < numberOfMessages; i++) {
    const teamMessage: ITeamMessage = toPlainObject(db.teamMessage.create());
    teamMessages.push({ ...teamMessage, teamId: 1, userId: i + 1 });
  }
  return teamMessages;
}

export function createTeamInvitations(numberOfInvitations: number) {
  const teamInvitations: ITeamInvitiation[] = [];

  for (let i = 1; i < numberOfInvitations + 1; i++) {
    const invitation: ITeamInvitiation = toPlainObject(db.teamInvitation.create());
    teamInvitations.push({ ...invitation, receiverId: i, senderId: i + 1, teamId: i });
  }
  return teamInvitations;
}

export function createTeamMemberTeams(numberOfTeams: number) {
  const teams: ITeamMemberTeam[] = [];

  for (let i = 0; i < numberOfTeams; i++) {
    teams.push(toPlainObject(db.teamMemberTeam.create()));
  }

  return teams;
}

export function createReviewers(numberOfReviewers: number) {
  const reviewers: IReviewer[] = [];

  for (let i = 0; i < numberOfReviewers; i++) {
    reviewers.push(toPlainObject(db.reviewer.create()));
  }

  return reviewers;
}

export function createUserAndProfile(overrides = {}) {
  const user = db.user.create();
  const profileEntity = db.fullProfile.create();

  const userProfile = {
    id: 1,
    userId: user.id,
    role: 'REVIEWER',
    country: 'USA',
    abbreviation: 'JA',
    city: 'Market',
    ...overrides,
  };
  const profile: IFullProfile = { ...toPlainObject(profileEntity), userProfile };

  const data: IFetchProfileResponse = {
    message: 'success',
    data: profile,
  };

  return data;
}

export function createTestimonials(numberOfTestimonials: number) {
  const testimonials: ITestimonial[] = [];

  for (let i = 0; i < numberOfTestimonials; i++) {
    testimonials.push(toPlainObject(db.testimonial.create()));
  }
  return testimonials;
}

export function createReviews(numberOfReviews: number) {
  const reviews: IReview[] = [];

  for (let i = 0; i < numberOfReviews; i++) {
    reviews.push(toPlainObject(db.review.create()));
  }
  return reviews;
}

export function createTags(numberOfTags: number) {
  const tags: ITag[] = [];

  for (let i = 0; i < numberOfTags; i++) {
    tags.push(db.tag.create());
  }
  return tags;
}

export function createComments(numberOfComments: number) {
  const comments: IComment[] = [];

  for (let i = 0; i < numberOfComments; i++) {
    comments.push(toPlainObject(db.comment.create({ content: `comment-${numberOfComments}-${i}` })));
  }
  return comments;
}

export function createMinComment() {
  const minComment = db.minComment.create();

  return minComment;
}

export function createReplyComments(numberOfReplyComments: number) {
  const replyComments: IReplyComment[] = [];
  for (let i = 0; i < numberOfReplyComments; i++) {
    replyComments.push(toPlainObject(db.replyComment.create()));
  }
  return replyComments;
}

export function deleteManyUser(ids: number[]) {
  db.user.deleteMany({ where: { id: { in: ids } } });
}

export function createUser(overrides: any = {}) {
  if (overrides.hasOwnProperty('id')) {
    deleteManyUser([overrides.id]);
  }

  return db.user.create({ ...overrides });
}

export function createNotifications(numberOfNotifications: number, receiver?: any, sender?: any) {
  const notifications = Array.from({ length: numberOfNotifications }).map(() => {
    return db.notification.create({ receiverId: receiver, senderId: sender });
  });

  return notifications;
}

export function createMinProfiles(numberOfProfiles: number) {
  const minProfiles = Array.from({ length: numberOfProfiles }).map(() => {
    return db.minProfile.create();
  });
  return minProfiles;
}

export function createSaveComments(numberOfComments: number) {
  const user = db.user.create();

  const saveComments = Array.from({ length: numberOfComments }).map(() => {
    return db.saveComment.create({ userId: user });
  });

  return saveComments;
}

export function getSpecificTopicsWithTags(numberOfTopics: number = 10, query: string) {
  const topics = db.topic.findMany({ take: numberOfTopics });
  const filteredTopics = topics.filter((topic) => topic.title === query);

  return filteredTopics.slice(0, numberOfTopics);
}

export function getTopicWithTags(numberOfTopics: number = 10) {
  return db.topic.findMany({ take: numberOfTopics });
}

export function createReactions(numberOfReactions: number) {
  const reactions: IReaction[] = [];
  for (let i = 0; i < numberOfReactions; i++) {
    reactions.push(db.reaction.create());
  }
  return reactions;
}

export function createTopicWithTag(overrides = {}) {
  const tags: ITag[] = [];

  for (let i = 0; i < 2; i++) {
    tags.push(db.tag.create());
  }
  db.topic.create({ ...overrides, tags });
}

export function getTopicWithTag(id: number) {
  return db.topic.findFirst({ where: { id: { equals: id } } });
}

export function createTopicWithTags(numberOfTopics: number, numberOfTags: number, query?: string) {
  const tags: ITag[] = [];

  for (let i = 0; i < numberOfTags; i++) {
    if (query !== undefined && i % 2 === 0) {
      tags.push(db.tag.create({ name: query }));
    } else {
      tags.push(db.tag.create({}));
    }
  }

  for (let i = 0; i < numberOfTopics; i++) {
    if (query !== undefined) {
      db.topic.create({ tags, title: query });
    } else {
      db.topic.create({ tags });
    }
  }
}

export function createUserWithFullProfileAndRelations(userOverrides = {}) {
  const user = db.user.create({
    ...userOverrides,
  });

  const setting = db.setting.create({ userId: user });

  const packageItems = Array.from({ length: 3 }).map(() => db.packageItem.create());

  const languages = [db.language.create({ name: 'English' }), db.language.create({ name: 'French' })];

  const programmingLanguages = [
    db.programmingLanguage.create({ name: 'JavaScript' }),
    db.programmingLanguage.create({ name: 'Python' }),
  ];

  const qualifications = [db.qualification.create(), db.qualification.create()];

  const workExps = [db.workExperience.create(), db.workExperience.create()];

  const slots = [
    db.slot.create({
      startTime: faker.date.recent().toString(),
      endTime: faker.date.soon().toString(),
    }),
    db.slot.create({
      startTime: faker.date.recent().toString(),
      endTime: faker.date.soon().toString(),
    }),
  ];
  const availability = db.availability.create({
    day: faker.helpers.arrayElement(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday']),
    slots,
  });

  const fullProfile = db.fullProfile.create({
    profile: {
      userId: user,
      role: faker.helpers.arrayElement(['ADMIN', 'USER', 'REVIEWER']),
      country: faker.location.country(),
      city: faker.location.city(),
      abbreviation: `${faker.person.firstName()[0]}.${faker.person.lastName()}`,
    },
    profileSetup: {
      avatar: faker.image.avatar(),
      tagLine: faker.lorem.words(),
      bio: faker.lorem.paragraph(),
    },
    basicInfo: {
      fullName: faker.person.fullName(),
      userName: faker.internet.displayName(),
      email: faker.internet.email(),
      contactNumber: faker.phone.number(),
    },
    additionalInfo: {
      availability: [availability],
      moreInfo: faker.lorem.sentence(),
    },
    pckg: {
      basic: {
        id: 'package-1',
        name: 'Basic Package',
        price: faker.commerce.price(),
        description: faker.lorem.sentence(),
        items: packageItems,
      },
      standard: {
        id: 'package-2',
        name: 'Standard Package',
        price: faker.commerce.price(),
        description: faker.lorem.sentence(),
        items: packageItems,
      },
      pro: {
        id: 'package-3',
        name: 'Pro Package',
        price: faker.commerce.price(),
        description: faker.lorem.sentence(),
        items: packageItems,
      },
    },
    skills: {
      languages,
      programmingLanguages,
      qualifications,
    },
    workExp: {
      workExps,
    },
  });

  db.user.update({
    where: {
      id: { equals: user.id },
    },
    data: {
      profileId: fullProfile,
      settingId: setting,
    },
  });

  return {
    user,
    fullProfile,
    setting,
    availability,
    slots,
  };
}
export const createFullUser = () => {
  const profile = db.fullProfile.create();
  const setting = db.setting.create();

  const userEntity = db.user.create({
    profileId: profile,
    settingId: setting,
  });

  return userEntity;
};
