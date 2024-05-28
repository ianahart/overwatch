import { PayloadAction, createSlice, nanoid } from '@reduxjs/toolkit';
import { IAdditionalInfoResponse, IDayAvailability } from '../../interfaces';
import { clearUser } from '../store';

interface IAdditionalInfoFormState {
  availability: IDayAvailability[];
  moreInfo: string;
}

interface ITime {
  day: string;
  startTime: string;
  endTime: string;
}

const initialState: IAdditionalInfoFormState = {
  moreInfo: '',
  availability: [
    { day: 'Monday', slots: [] },
    { day: 'Tuesday', slots: [] },
    { day: 'Wednesday', slots: [] },
    { day: 'Thursday', slots: [] },
    { day: 'Friday', slots: [] },
    { day: 'Saturday', slots: [] },
    { day: 'Sunday', slots: [] },
  ],
};

const additionalInfoFormSlice = createSlice({
  name: 'additionalInfo',
  initialState,
  reducers: {
    updateAdditionalInfo: (state, action: PayloadAction<IAdditionalInfoResponse>) => {
      const { moreInfo, availability } = action.payload;
      state.moreInfo = moreInfo || '';
      state.availability = availability || initialState.availability;
    },
    addTimeSlot: (state, action: PayloadAction<ITime>) => {
      const { day, startTime, endTime } = action.payload;
      const timeSlotIndex = state.availability.findIndex((av) => av.day === day);
      state.availability[timeSlotIndex].slots.push({ id: nanoid(), startTime, endTime });
    },

    removeTimeSlot: (state, action: PayloadAction<{ day: string; id: string }>) => {
      const { day, id } = action.payload;
      const timeSlotIndex = state.availability.findIndex((av) => av.day === day);
      state.availability[timeSlotIndex].slots = state.availability[timeSlotIndex].slots.filter(
        (slot) => slot.id !== id
      );
    },

    updateMoreInfo: (state, action: PayloadAction<string>) => {
      state.moreInfo = action.payload;
    },

    clearAdditionalInfoForm: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { updateAdditionalInfo, updateMoreInfo, clearAdditionalInfoForm, addTimeSlot, removeTimeSlot } =
  additionalInfoFormSlice.actions;

export const additionalInfoFormReducer = additionalInfoFormSlice.reducer;
