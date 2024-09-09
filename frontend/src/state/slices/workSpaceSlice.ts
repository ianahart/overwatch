import { Draft, PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IWorkSpaceState, IWorkSpaceEntity, ILabel } from '../../interfaces';
import { clearUser } from '../store';

export interface IUpdatePropertyPayload<T> {
  value: T;
  property: keyof IWorkSpaceState['workSpace'];
}

const initialState: IWorkSpaceState = {
  workSpace: {
    userId: 0,
    id: 0,
    createdAt: '',
    title: '',
    backgroundColor: '',
  },
  labels: [],
};

const workSpaceSlice = createSlice({
  name: 'workSpace',
  initialState,
  reducers: {
    setWorkSpace: (state, action: PayloadAction<IWorkSpaceEntity>) => {
      state.workSpace = action.payload;
    },
    setLabels: (state, action: PayloadAction<ILabel[]>) => {
      state.labels = action.payload;
    },

    toggleLabel: (state, action: PayloadAction<{ id: number; isChecked: boolean }>) => {
      const { id, isChecked } = action.payload;
      state.labels = state.labels.map((label) => {
        if (label.id === id) {
          return { ...label, isChecked };
        }
        return { ...label };
      });
    },
    updateWorkSpaceProperty: <T>(state: Draft<IWorkSpaceState>, action: PayloadAction<IUpdatePropertyPayload<T>>) => {
      const { value, property } = action.payload;
      switch (property) {
        case 'title':
          state.workSpace.title = value as string;
          break;
        case 'backgroundColor':
          state.workSpace.backgroundColor = value as string;
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

export const { toggleLabel, setLabels, clearWorkSpace, updateWorkSpaceProperty, setWorkSpace } = workSpaceSlice.actions;

export const workSpaceReducer = workSpaceSlice.reducer;
