import { createApi } from '@reduxjs/toolkit/query/react';
import { ICreateWorkSpaceResponse, ICreateWorkSpaceRequest } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const workSpacesApi = createApi({
  reducerPath: 'workSpaces',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      createWorkSpace: builder.mutation<ICreateWorkSpaceResponse, ICreateWorkSpaceRequest>({
        query: ({ userId, token, workSpace }) => {
          return {
            url: `/workspaces`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: {
              title: workSpace.title,
              backgroundColor: workSpace.backgroundColor,
              userId,
            },
          };
        },
      }),
    };
  },
});

export const { useCreateWorkSpaceMutation } = workSpacesApi;
export { workSpacesApi };
