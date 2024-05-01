import { createSlice } from '@reduxjs/toolkit';

interface INavbarState {
  isMobileOpen: boolean;
}

const initialState: INavbarState = {
  isMobileOpen: false,
};

const navbarSlice = createSlice({
  name: 'navbar',
  initialState,
  reducers: {
    openMobile: (state) => {
      state.isMobileOpen = true;
    },
    closeMobile: (state) => {
      state.isMobileOpen = false;
    },
  },
});

export const { openMobile, closeMobile } = navbarSlice.actions;

export default navbarSlice.reducer;
