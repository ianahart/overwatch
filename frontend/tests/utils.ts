import { faker } from '@faker-js/faker';

export interface IName {
  firstName: string;
  lastName: string;
}

export const getFullName = (name?: IName) => {
  if (name !== undefined) {
    return faker.person.fullName(name);
  }
  return faker.person.fullName();
};

const splitName = (name?: IName) => {
  const [first, last] = faker.person.fullName(name).split(' ').slice(0, 2);
  return { first, last };
};

export const getNameAbbreviation = (name?: IName) => {
  const { first, last } = splitName(name);
  return `${first[0]}.${last[0]}`;
};
