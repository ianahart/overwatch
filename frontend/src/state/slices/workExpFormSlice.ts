import { PayloadAction, createSlice, nanoid } from '@reduxjs/toolkit';
import { clearUser } from '../actions/globalActions';
import { IWorkExpResponse, IWorkExperience } from '../../interfaces';

interface IWorkExperienceState {
  workExps: IWorkExperience[];
}

const initialState: IWorkExperienceState = {
  workExps: [],
};

const workExpFormSlice = createSlice({
  name: 'workExp',
  initialState,
  reducers: {
    updateWorkExp: (state, action: PayloadAction<IWorkExpResponse>) => {
      state.workExps = action.payload.workExps || [];
    },
    addWorkExpToList: (state, action: PayloadAction<{ title: string; desc: string }>) => {
      const { title, desc } = action.payload;
      state.workExps.push({ id: nanoid(), title, desc });
    },

    removeWorkExpFromList: (state, action: PayloadAction<string>) => {
      state.workExps = state.workExps.filter((workExp) => workExp.id !== action.payload);
    },

    clearWorkExpForm: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { updateWorkExp, clearWorkExpForm, addWorkExpToList, removeWorkExpFromList } = workExpFormSlice.actions;

export const workExpFormReducer = workExpFormSlice.reducer;
