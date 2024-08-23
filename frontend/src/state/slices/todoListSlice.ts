import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { ITodoListState, ITodoList } from '../../interfaces';
import { clearUser, setWorkSpace } from '../store';

const initialState: ITodoListState = {
  todoList: [],
};

const todoListSlice = createSlice({
  name: 'todoList',
  initialState,
  reducers: {
    setTodoList: (state, action: PayloadAction<ITodoList[]>) => {
      state.todoList = action.payload;
    },

    addToTodoList: (state, action: PayloadAction<ITodoList>) => {
      state.todoList.push(action.payload);
    },

    clearTodoList: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
    builder.addCase(setWorkSpace, () => {
      return initialState;
    });
  },
});

export const { setTodoList, addToTodoList, clearTodoList } = todoListSlice.actions;

export const todoListReducer = todoListSlice.reducer;
