import { useEffect, useState } from 'react';
import { nanoid } from 'nanoid';
import { useDispatch, useSelector } from 'react-redux';

import { reviewsFilterStatusOptions, reviewsFilterSortingOptions } from '../../../../../data';
import {
  TRootState,
  setRepositoryPagination,
  setRepositoryReviews,
  useFetchDistinctRepositoryLanguagesQuery,
  useFetchRepositoriesQuery,
} from '../../../../../state/store';
import { retrieveTokens } from '../../../../../util';
import ReviewsFilter from './ReviewsFilter';

export interface ILanguageOption {
  id: string;
  value: string;
  name: string;
}

const ReviewsFilters = () => {
  const dispatch = useDispatch();
  const { sortFilter, statusFilter, languageFilter } = useSelector((store: TRootState) => store.repositoryReviews);
  const token = retrieveTokens()?.token;
  const [languageOptions, setLanguageOptions] = useState<ILanguageOption[]>([]);
  const { data: languageData } = useFetchDistinctRepositoryLanguagesQuery({ token });
  const { data: repositoriesData } = useFetchRepositoriesQuery({
    token: retrieveTokens()?.token,
    page: -1,
    pageSize: 5,
    direction: 'next',
    sortFilter,
    statusFilter,
    languageFilter,
  });

  useEffect(() => {
    if (repositoriesData !== undefined) {
      const { direction, items, page, pageSize, totalElements, totalPages } = repositoriesData.data;
      const pagination = { direction, page, pageSize, totalElements, totalPages };
      dispatch(setRepositoryReviews(items));
      dispatch(setRepositoryPagination(pagination));
    }
  }, [repositoriesData, dispatch]);

  useEffect(() => {
    if (languageData !== undefined) {
      setLanguageOptions(languageData.data.map((value) => ({ id: nanoid(), value, name: value })));
    }
  }, [languageData]);

  return (
    <div className="reviews-filter-container">
      <ReviewsFilter value={sortFilter} id="sort" data={reviewsFilterSortingOptions} />
      <ReviewsFilter value={statusFilter} id="status" data={reviewsFilterStatusOptions} />
      <ReviewsFilter value={languageFilter} id="language" data={languageOptions} />
    </div>
  );
};

export default ReviewsFilters;
