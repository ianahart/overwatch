import { Draft, PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IWorkSpaceState } from '../../interfaces';
import { clearUser } from '../store';

export interface IUpdatePropertyPayload<T> {
  value: T;
  property: keyof IWorkSpaceState['workSpace'];
}

const initialState: IWorkSpaceState = {
  workSpace: {
    title: '',
    backgroundColor: '',
    todoLists: [],
  },
};

const workSpaceSlice = createSlice({
  name: 'workSpace',
  initialState,
  reducers: {
    updateWorkSpaceProperty: <T>(state: Draft<IWorkSpaceState>, action: PayloadAction<IUpdatePropertyPayload<T>>) => {
      const { value, property } = action.payload;
      switch (property) {
        case 'title':
          state.workSpace.title = value as string;
          break;
        case 'backgroundColor':
          state.workSpace.backgroundColor = value as string;
          break;
        case 'todoLists':
          state.workSpace.todoLists = value as string[];
          break;
        default:
          break;
      }
    },

    clearWorkSpace: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { clearWorkSpace, updateWorkSpaceProperty } = workSpaceSlice.actions;

export const workSpaceReducer = workSpaceSlice.reducer;
