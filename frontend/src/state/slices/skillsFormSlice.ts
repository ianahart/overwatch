import { PayloadAction, createSlice, nanoid } from '@reduxjs/toolkit';
import { ILanguage, IProgrammingLanguage, IQualification, ISkillsResponse } from '../../interfaces';
import { clearUser } from '../store';

interface ISkillsState {
  languages: ILanguage[];
  programmingLanguages: IProgrammingLanguage[];
  qualifications: IQualification[];
}

const initialState: ISkillsState = {
  languages: [],
  programmingLanguages: [],
  qualifications: [],
};

const skillsFormSlice = createSlice({
  name: 'skills',
  initialState,
  reducers: {
    updateSkills: (state, action: PayloadAction<ISkillsResponse>) => {
      const { languages, programmingLanguages, qualifications } = action.payload;
      state.languages = languages || [];
      state.programmingLanguages = programmingLanguages || [];
      state.qualifications = qualifications || [];
    },

    addToList: (state, action: PayloadAction<{ listName: string; value: string }>) => {
      const { listName, value } = action.payload;
      const bubble = { id: nanoid(), name: value };
      switch (listName) {
        case 'languages':
          state.languages.push(bubble);
          break;
        case 'programmingLanguages':
          state.programmingLanguages.push(bubble);
          break;
        case 'qualifications':
          state.qualifications.push(bubble);
          break;
        default:
          break;
      }
    },
    removeFromList: (state, action: PayloadAction<{ listName: string; id: string }>) => {
      const { listName, id } = action.payload;
      switch (listName) {
        case 'languages':
          state.languages = state.languages.filter((language) => language.id !== id);
          break;
        case 'programmingLanguages':
          state.programmingLanguages = state.programmingLanguages.filter(
            (programmingLanguage) => programmingLanguage.id !== id
          );
          break;
        case 'qualifications':
          state.qualifications = state.qualifications.filter((qualification) => qualification.id !== id);
          break;
        default:
          break;
      }
    },

    clearSkills: () => {
      // might need to do state.languages = [] etc
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { updateSkills, clearSkills, addToList, removeFromList } = skillsFormSlice.actions;

export const skillsFormReducer = skillsFormSlice.reducer;
