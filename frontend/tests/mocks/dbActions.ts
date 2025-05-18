import { ITag } from '../../src/interfaces';
import { db } from './db';
import { faker } from '@faker-js/faker';

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
      startTime: faker.date.recent(),
      endTime: faker.date.soon(),
    }),
    db.slot.create({
      startTime: faker.date.recent(),
      endTime: faker.date.soon(),
    }),
  ];
  const availability = db.availability.create({
    day: faker.helpers.arrayElement(['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday']),
    slots,
  });

  const fullProfile = db.fullProfile.create({
    profile: {
      userId: user,
      role: faker.helpers.arrayElement(['admin', 'user', 'reviewer']),
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
