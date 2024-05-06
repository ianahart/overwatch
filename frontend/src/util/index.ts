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
