import { createApi } from '@reduxjs/toolkit/query/react';
import { IFetchStatisticRequest, IFetchStatisticResponse } from '../../interfaces';
import { baseQueryWithReauth } from '../util';

const statisticsApi = createApi({
  reducerPath: 'statistics',
  tagTypes: ['Statistic'],
  baseQuery: baseQueryWithReauth,
  endpoints(builder) {
    return {
      fetchStatistics: builder.query<IFetchStatisticResponse, IFetchStatisticRequest>({
        query: ({ token, reviewerId }) => {
          if (token === '' || reviewerId === 0) {
            return '';
          }
          return {
            url: `/statistics?reviewerId=${reviewerId}`,
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          };
        },
      }),
    };
  },
});

export const { useFetchStatisticsQuery } = statisticsApi;
export { statisticsApi };
