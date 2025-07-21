import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { clearUser } from '../actions/globalActions';
import { IGitHubRepository, IGitHubTree } from '../../interfaces';
import { repositoryState } from '../../data';
import { ERepositoryView } from '../../enums';

interface IRepositoryFile {
  path: string;
  content: string;
  language: string;
}

export interface IRepositoryReviewsState {
  repositoryNavView: ERepositoryView;
  repositoryTree: IGitHubTree[];
  repositoryLanguages: string[];
  repository: IGitHubRepository;
  repositoryPage: number;
  repositoryFile: IRepositoryFile;
  searchingCodeQuery: string;
}

const initialState: IRepositoryReviewsState = {
  searchingCodeQuery: '',
  repositoryNavView: ERepositoryView.DETAILS,
  repositoryLanguages: [],
  repositoryTree: [],
  repository: repositoryState,
  repositoryPage: 0,
  repositoryFile: { path: '', content: '', language: '' },
};

const repositoryTreeSlice = createSlice({
  name: 'repositoryTree',
  initialState,
  reducers: {
    setRepositoryNavView: (state, action: PayloadAction<ERepositoryView>) => {
      state.repositoryNavView = action.payload;
    },
    setRepositoryLanguages: (state, action: PayloadAction<string[]>) => {
      state.repositoryLanguages = action.payload;
    },

    updateRepository: (state, action: PayloadAction<{ status: string; feedback: string }>) => {
      const { feedback, status } = action.payload;
      state.repository.feedback = feedback;
      state.repository.status = status;
      localStorage.setItem('content', feedback);
    },

    setRepository: (state, action: PayloadAction<IGitHubRepository>) => {
      state.repository = action.payload;
    },

    setRepositoryFile: (state, action: PayloadAction<IRepositoryFile>) => {
      state.repositoryFile = action.payload;
    },

    setSearchingCodeQuery: (state, action: PayloadAction<string>) => {
      state.searchingCodeQuery = action.payload;
    },

    setRepositoryTree: (state, action: PayloadAction<IGitHubTree[]>) => {
      state.repositoryTree = [...state.repositoryTree, ...action.payload];
    },

    setInitialRepositoryTree: (state, action: PayloadAction<IGitHubTree[]>) => {
      if (action.payload && action.payload.length > 0) {
        state.repositoryTree = action.payload;
      }
    },

    setRepositoryPage: (state, action: PayloadAction<number>) => {
      state.repositoryPage = action.payload;
    },

    clearRepositoryTree: () => {
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
  setSearchingCodeQuery,
  setInitialRepositoryTree,
  setRepositoryNavView,
  setRepositoryLanguages,
  setRepositoryFile,
  setRepositoryTree,
  setRepository,
  setRepositoryPage,
  clearRepositoryTree,
  updateRepository,
} = repositoryTreeSlice.actions;

export const repositoryTreeReducer = repositoryTreeSlice.reducer;
