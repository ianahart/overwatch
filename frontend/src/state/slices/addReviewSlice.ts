import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IConnection, ISelectedReviewer } from '../../interfaces';
import { clearUser } from '../store';
import { connectionState } from '../../data';

interface IAddReviewState {
  reviewers: IConnection[];
  selectedReviewer: IConnection;
}

const initialState: IAddReviewState = {
  reviewers: [],
  selectedReviewer: connectionState,
};

const addReviewSlice = createSlice({
  name: 'addReview',
  initialState,
  reducers: {
    setSelectedReviewer: (state, action: PayloadAction<ISelectedReviewer>) => {
      state.selectedReviewer = action.payload.reviewer;
      localStorage.setItem('selected_reviewer', action.payload.reviewer.receiverId.toString());
    },

    clearSelectedReviewer: (state) => {
      state.selectedReviewer = connectionState;
      localStorage.removeItem('selected_reviewer');
    },

    setReviewers: (state, action: PayloadAction<IConnection[]>) => {
      state.reviewers = [...state.reviewers, ...action.payload];
    },

    clearReviewers: (state) => {
      state.reviewers = [];
    },

    clearAddReview: () => {
      localStorage.removeItem('selected_reviewer');
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { setSelectedReviewer, setReviewers, clearReviewers, clearAddReview, clearSelectedReviewer } =
  addReviewSlice.actions;

export const addReviewReducer = addReviewSlice.reducer;
