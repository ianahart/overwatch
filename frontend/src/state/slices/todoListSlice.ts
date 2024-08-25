import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { ITodoListsState, ITodoList } from '../../interfaces';
import { clearUser, setWorkSpace } from '../store';

const initialState: ITodoListsState = {
  todoLists: [],
};

const todoListsSlice = createSlice({
  name: 'todoList',
  initialState,
  reducers: {
    setTodoLists: (state, action: PayloadAction<ITodoList[]>) => {
      state.todoLists = action.payload;
    },

    updateSingleTodoList: (state, action: PayloadAction<ITodoList>) => {
      const updatedTodoList = action.payload;

      state.todoLists = state.todoLists.map((todoList) => {
        if (todoList.id === updatedTodoList.id) {
          return { ...updatedTodoList };
        } else {
          return { ...todoList };
        }
      });
    },

    addToTodoList: (state, action: PayloadAction<ITodoList>) => {
      state.todoLists.push(action.payload);
    },

    clearTodoLists: () => {
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

export const { setTodoLists, addToTodoList, clearTodoLists, updateSingleTodoList } = todoListsSlice.actions;

export const todoListsReducer = todoListsSlice.reducer;
