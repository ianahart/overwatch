import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { clearUser } from '../store';
import { IPackage, IPckgResponse } from '../../interfaces';
import { nanoid } from 'nanoid';

interface IItem {
  name: string;
  id: string;
  isEditing: number;
  pckg: string;
}

interface IPackageFormState {
  [key: string]: IPackage;
  basic: IPackage;
  standard: IPackage;
  pro: IPackage;
}

const initialState: IPackageFormState = {
  basic: { description: '', items: [] },
  standard: { description: '', items: [] },
  pro: { description: '', items: [] },
};

const packageFormSlice = createSlice({
  name: 'package',
  initialState,
  reducers: {
    updatePackages: (state, action: PayloadAction<IPckgResponse>) => {
      const { basic, standard, pro } = action.payload;
      state.basic = basic || { description: '', items: [] };
      state.standard = standard || { description: '', items: [] };
      state.pro = pro || { description: '', items: [] };
    },
    addPackageItem: (state, action: PayloadAction<{ name: string; value: string }>) => {
      const { name, value } = action.payload;
      switch (name) {
        case 'basic':
          state.basic.items.push({ id: nanoid(), name: value, isEditing: 0 });
          break;
        case 'standard':
          state.standard.items.push({ id: nanoid(), name: value, isEditing: 0 });
          break;
        case 'pro':
          state.pro.items.push({ id: nanoid(), name: value, isEditing: 0 });
          break;
        default:
          break;
      }
    },
    updatePackageDesc: (state, action: PayloadAction<{ name: string; value: string }>) => {
      const { name, value } = action.payload;
      switch (name) {
        case 'basic':
          state.basic.description = value;
          break;
        case 'standard':
          state.standard.description = value;
          break;
        case 'pro':
          state.pro.description = value;
          break;
        default:
          break;
      }
    },

    updatePackageItem: (state, action: PayloadAction<IItem>) => {
      const { pckg, id, name, isEditing } = action.payload;
      const index = state[pckg].items.findIndex((item) => item.id === id);
      state[pckg].items[index] = { id, name, isEditing };
    },

    removePackageItem: (state, action: PayloadAction<{ id: string; pckg: string }>) => {
      const { id, pckg } = action.payload;
      state[pckg].items = state[pckg].items.filter((item) => item.id !== id);
    },

    removePackageDesc: (state, action: PayloadAction<{ pckg: string }>) => {
      const { pckg } = action.payload;
      state[pckg].description = '';
    },

    clearPackageForm: () => {
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
  updatePackages,
  removePackageDesc,
  removePackageItem,
  addPackageItem,
  clearPackageForm,
  updatePackageDesc,
  updatePackageItem,
} = packageFormSlice.actions;

export const packageFormReducer = packageFormSlice.reducer;
