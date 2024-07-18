import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { clearUser } from '../store';
import { IGitHubRepository, IGitHubTree } from '../../interfaces';
import { repositoryState } from '../../data';
import { ERepositoryView } from '../../enums';

interface IRepositoryFile {
  path: string;
  content: string;
  language: string;
}

interface IRepositoryReviewsState {
  repositoryNavView: ERepositoryView;
  repositoryTree: IGitHubTree[];
  repositoryLanguages: string[];
  repository: IGitHubRepository;
  repositoryPage: number;
  repositoryFile: IRepositoryFile;
}

const initialState: IRepositoryReviewsState = {
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

    setRepository: (state, action: PayloadAction<IGitHubRepository>) => {
      state.repository = action.payload;
    },

    setRepositoryFile: (state, action: PayloadAction<IRepositoryFile>) => {
      state.repositoryFile = action.payload;
    },

    setRepositoryTree: (state, action: PayloadAction<IGitHubTree[]>) => {
      state.repositoryTree = [...state.repositoryTree, ...action.payload];
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
    setRepositoryNavView,
  setRepositoryLanguages,
  setRepositoryFile,
  setRepositoryTree,
  setRepository,
  setRepositoryPage,
  clearRepositoryTree,
} = repositoryTreeSlice.actions;

export const repositoryTreeReducer = repositoryTreeSlice.reducer;
