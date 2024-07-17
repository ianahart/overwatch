import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { clearUser } from '../store';
import { IGitHubRepository, IGitHubTree } from '../../interfaces';
import { repositoryState } from '../../data';

interface IRepositoryFile {
  path: string;
  content: string;
  language: string;
}

interface IRepositoryReviewsState {
  repositoryTree: IGitHubTree[];
  repository: IGitHubRepository;
  repositoryPage: number;
  repositoryFile: IRepositoryFile;
}

const initialState: IRepositoryReviewsState = {
  repositoryTree: [],
  repository: repositoryState,
  repositoryPage: 0,
  repositoryFile: { path: '', content: '', language: '' },
};

const repositoryTreeSlice = createSlice({
  name: 'repositoryTree',
  initialState,
  reducers: {
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

export const { setRepositoryFile, setRepositoryTree, setRepository, setRepositoryPage, clearRepositoryTree } =
  repositoryTreeSlice.actions;

export const repositoryTreeReducer = repositoryTreeSlice.reducer;
