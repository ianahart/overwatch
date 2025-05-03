import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { clearUser } from '../actions/globalActions';
import { IPaginationState, IRepositoryReview } from '../../interfaces';
import { repositoryPaginationState } from '../../data';

interface IRepositoryReviewsState {
  sortFilter: string;
  statusFilter: string;
  languageFilter: string;
  pagination: IPaginationState;
  repositoryReviews: IRepositoryReview[];
}

const initialState: IRepositoryReviewsState = {
  sortFilter: 'desc',
  statusFilter: 'INCOMPLETE',
  languageFilter: 'All',
  pagination: repositoryPaginationState,
  repositoryReviews: [],
};

const repositoryReviewsSlice = createSlice({
  name: 'repositoryReviews',
  initialState,
  reducers: {
    setRepositoryReviews: (state, action: PayloadAction<IRepositoryReview[]>) => {
      state.repositoryReviews = action.payload;
    },

    setRepositoryPagination: (state, action: PayloadAction<IPaginationState>) => {
      state.pagination = action.payload;
    },

    setRepositorySearchFilter: (state, action: PayloadAction<{ name: string; value: string }>) => {
      const { name, value } = action.payload;
      switch (name) {
        case 'sort':
          state.sortFilter = value;
          break;
        case 'status':
          state.statusFilter = value;
          break;
        case 'language':
          state.languageFilter = value;
          break;
        default:
      }
    },

    clearRepositoryReviews: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { setRepositoryReviews, setRepositoryPagination, clearRepositoryReviews, setRepositorySearchFilter } =
  repositoryReviewsSlice.actions;

export const repositoryReviewsReducer = repositoryReviewsSlice.reducer;
