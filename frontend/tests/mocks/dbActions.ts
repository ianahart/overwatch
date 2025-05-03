import { db } from './db';
import { faker } from '@faker-js/faker';

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
