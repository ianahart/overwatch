import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateUserRepositoryRequest, ICreateUserRepositoryResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const repositoriesApi = createApi({
  reducerPath: 'repository',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      createUserRepository: builder.mutation<ICreateUserRepositoryResponse, ICreateUserRepositoryRequest>({
        query: ({ payload, token }) => {
          return {
            url: `/repositories/user`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: payload,
          };
        },
      }),
    };
  },
});

export const {useCreateUserRepositoryMutation} = repositoriesApi;
export { repositoriesApi };
