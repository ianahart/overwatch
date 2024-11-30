import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IPaginationState, ITeam, ITeamInvitiation, ITeamMemberTeam, ITeamMessage, ITeamPost } from '../../interfaces';
import { clearUser } from '../store';

interface IUpdateTeamPostTeam {
  reset: boolean;
  posts: ITeamPost[];
}

interface IUpdatTeamMemberTeam {
  reset: boolean;
  team: ITeamMemberTeam[];
}

interface IUpdateTeamInvitation {
  reset: boolean;
  invitations: ITeamInvitiation[];
}

interface ITeamState {
  currentTeam: number;
  adminTeams: ITeam[];
  teamMessages: ITeamMessage[];
  teamPosts: ITeamPost[];
  teamInvitations: ITeamInvitiation[];
  teamMemberTeams: ITeamMemberTeam[];
  adminTeamPagination: IPaginationState;
  teamMemberTeamPagination: IPaginationState;
  teamInvitationPagination: IPaginationState;
  teamPostPagination: IPaginationState;
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
  teamMessages: [],
  teamInvitations: [],
  teamMemberTeams: [],
  teamPosts: [],
  teamInvitationPagination: paginationState,
  adminTeamPagination: paginationState,
  teamMemberTeamPagination: paginationState,
  teamPostPagination: paginationState,
};

const teamSlice = createSlice({
  name: 'team',
  initialState,
  reducers: {
    setTeamPagination: (state, action: PayloadAction<IPaginationPayload>) => {
      switch (action.payload.paginationType) {
        case 'admin':
          state.adminTeamPagination = action.payload.pagination;
          break;
        case 'member':
          state.teamMemberTeamPagination = action.payload.pagination;
          break;
        case 'invitation':
          state.teamInvitationPagination = action.payload.pagination;
          break;
        case 'post':
          state.teamPostPagination = action.payload.pagination;
          break;
        default:
          break;
      }
    },

    setCurrentTeam: (state, action: PayloadAction<number>) => {
      state.currentTeam = action.payload;
      state.teamMessages = [];
      state.teamPosts = [];
    },

    addTeamMessage: (state, action: PayloadAction<ITeamMessage>) => {
      state.teamMessages = [action.payload, ...state.teamMessages];
    },

    setTeamPosts: (state, action: PayloadAction<IUpdateTeamPostTeam>) => {
      if (action.payload.reset) {
        state.teamPosts = [];
        state.teamPostPagination = paginationState;
      } else {
        state.teamPosts = [...state.teamPosts, ...action.payload.posts];
      }
    },

    setTeamMessages: (state, action: PayloadAction<ITeamMessage[]>) => {
      state.teamMessages = [];
      state.teamMessages = action.payload;
    },

    removeTeamPost: (state, action: PayloadAction<number>) => {
      state.teamPosts = state.teamPosts.filter((teamPost) => teamPost.id !== action.payload);
    },

    removeTeamInvitation: (state, action: PayloadAction<number>) => {
      state.teamInvitations = state.teamInvitations.filter((teamInvitation) => teamInvitation.id !== action.payload);
    },

    setTeamInvitations: (state, action: PayloadAction<IUpdateTeamInvitation>) => {
      if (action.payload.reset) {
        state.teamInvitations = [];
        state.teamInvitationPagination = paginationState;
      } else {
        state.teamInvitations = [...state.teamInvitations, ...action.payload.invitations];
      }
    },

    setTeamMemberTeams: (state, action: PayloadAction<IUpdatTeamMemberTeam>) => {
      if (action.payload.reset) {
        state.teamMemberTeams = [];
        state.teamMemberTeams = action.payload.team;
        return;
      }
      state.teamMemberTeams = [...state.teamMemberTeams, ...action.payload.team];
    },

    setAdminTeams: (state, action: PayloadAction<ITeam[]>) => {
      state.adminTeams = [...state.adminTeams, ...action.payload];
    },

    clearTeamPagination: (state, action: PayloadAction<string>) => {
      const paginationState = { page: 0, pageSize: 3, totalPages: 0, direction: 'next', totalElements: 0 };
      switch (action.payload) {
        case 'admin':
          state.adminTeamPagination = paginationState;
          break;
        case 'member':
          state.teamMemberTeamPagination = paginationState;
          break;
        case 'invitation':
          state.teamInvitationPagination = paginationState;
          break;
        case 'post':
          state.teamPostPagination = paginationState;
          break;
        default:
          break;
      }
    },
    clearAdminTeams: (state) => {
      state.adminTeams = [];
    },

    clearTeamMemberTeams: (state) => {
      state.teamMemberTeams = [];
    },

    clearTeamInvitations: (state) => {
      state.teamInvitations = [];
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

export const {
  setTeamPosts,
  addTeamMessage,
  setTeamMessages,
  clearTeamMemberTeams,
  setTeamMemberTeams,
  clearAdminTeams,
  clearTeamPagination,
  clearTeams,
  setAdminTeams,
  setCurrentTeam,
  setTeamPagination,
  clearTeamInvitations,
  setTeamInvitations,
  removeTeamInvitation,
  removeTeamPost,
} = teamSlice.actions;

export const teamReducer = teamSlice.reducer;
