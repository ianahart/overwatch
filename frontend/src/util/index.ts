export const baseURL = 'http://localhost:5173/api/v1';

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
    mask += index === 0 || index > symIndex ? letter : '*';
  });
  return mask;
};

export const shortenString = (str: string) => {
  if (str.length === 0) {
    return '';
  }
  return str.split(' ').slice(0, 5).join(' ') + '...';
};
