import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IPaginationState, ITeam } from '../../interfaces';
import { clearUser } from '../store';

interface ITeamState {
  currentTeam: number;
  adminTeams: ITeam[];
  adminTeamPagination: IPaginationState;
  teamMemberPagination: IPaginationState;
}

interface IPaginationPayload {
  pagination: IPaginationState;
  paginationType: string;
}

const paginationState = {
  page: 0,
  pageSize: 3,
  totalPages: 0,
  direction: 'next',
  totalElements: 0,
};

const initialState: ITeamState = {
  currentTeam: 0,
  adminTeams: [],
  adminTeamPagination: paginationState,
  teamMemberPagination: paginationState,
};

const teamSlice = createSlice({
  name: 'team',
  initialState,
  reducers: {
    setTeamPagination: (state, action: PayloadAction<IPaginationPayload>) => {
      if (action.payload.paginationType === 'admin') {
        state.adminTeamPagination = action.payload.pagination;
      }

      if (action.payload.paginationType === 'member') {
        state.teamMemberPagination = action.payload.pagination;
      }
    },

    setCurrentTeam: (state, action: PayloadAction<number>) => {
      state.currentTeam = action.payload;
    },

    setAdminTeams: (state, action: PayloadAction<ITeam[]>) => {
      state.adminTeams = [...state.adminTeams, ...action.payload];
    },
    clearTeamPagination: (state, action: PayloadAction<string>) => {
      const paginationState = { page: 0, pageSize: 3, totalPages: 0, direction: 'next', totalElements: 0 };

      if (action.payload === 'admin') {
        state.adminTeamPagination = paginationState;
      }

      if (action.payload === 'member') {
        state.teamMemberPagination = paginationState;
      }
    },
    clearAdminTeams: (state) => {
      state.adminTeams = [];
    },

    clearTeams: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { clearAdminTeams, clearTeamPagination, clearTeams, setAdminTeams, setCurrentTeam, setTeamPagination } =
  teamSlice.actions;

export const teamReducer = teamSlice.reducer;
