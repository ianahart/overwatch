import { ILanguageMap } from '../interfaces';

const baseUrlEnv = import.meta.env.VITE_CLIENT_URL;
export const baseURL = `${baseUrlEnv}/api/v1`;

export const languageMap: ILanguageMap = {
  js: 'javascript',
  jsx: 'jsx',
  ts: 'typescript',
  tsx: 'tsx',
  java: 'java',
  css: 'css',
  py: 'python',
  rb: 'ruby',
  html: 'html',
  php: 'php',
};

export const retrieveTokens = () => {
  const storage = localStorage.getItem('tokens');
  let tokens;
  if (storage) {
    tokens = JSON.parse(storage);
  }
  return tokens;
};

export const authHeaders = () => {
  return { Authorization: `Bearer ${retrieveTokens().token}` };
};

export const maskEmail = (email: string) => {
  let mask = '';
  const symIndex = email.split('').findIndex((v) => v === '@');

  email.split('').forEach((letter, index) => {
    mask += index === 0 || index >= symIndex ? letter : '*';
  });
  return mask;
};

export const shortenString = (str: string, size: number) => {
  if (str.length === 0) {
    return '';
  }
  return str.split(' ').slice(0, size).join(' ') + '...';
};

export const addDays = (date: Date, days: number) => {
  const newDate = new Date(date);
  newDate.setDate(date.getDate() + days);
  return newDate;
};

export const addMonths = (date: Date, months: number) => {
  const newDate = new Date(date);
  newDate.setMonth(date.getMonth() + months);
  return newDate;
};

export const initializeName = (firstName: string, lastName: string): string => {
  if (!firstName || !lastName) {
    return '?.?';
  }
  return firstName.slice(0, 1).toUpperCase() + '.' + lastName.slice(0, 1).toUpperCase();
};

export const convertCentsToDollars = (cents: number): number => {
  return cents / 100.0;
};
