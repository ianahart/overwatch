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

export const paginate = <T>(page: number, pageSize: number, direction: string, data: T[]) => {
  let currentPage = 0;
  if (direction === 'next') {
    currentPage = page + 1;
  } else if (direction === 'prev' && page > 0) {
    currentPage = page - 1;
  } else {
    currentPage = page;
  }

  const totalElements = 20;
  const totalPages = Math.ceil(totalElements / pageSize);

  const startIndex = currentPage * pageSize;
  const endIndex = Math.min(startIndex + pageSize, totalElements);

  const items = data.slice(startIndex, endIndex);

  return {
    page: currentPage,
    pageSize,
    direction,
    items,
    totalPages,
  };
};
