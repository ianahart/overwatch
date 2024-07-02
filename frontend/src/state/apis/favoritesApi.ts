import { createApi } from '@reduxjs/toolkit/query/react';
import { IToggleFavoriteRequest, IToggleFavoriteResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const favoritesApi = createApi({
  reducerPath: 'favorites',
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      toggleFavorite: builder.mutation<IToggleFavoriteResponse, IToggleFavoriteRequest>({
        query: ({ profileId, userId, isFavorited, token }) => {
          if (userId === 0 || !token) {
            return '';
          }
          return {
            url: `/favorites`,
            method: 'POST',
            headers: {
              Authorization: `Bearer ${token}`,
            },
            body: { profileId, userId, isFavorited },
          };
        },
      }),
    };
  },
});

export const { useToggleFavoriteMutation } = favoritesApi;
export { favoritesApi };
