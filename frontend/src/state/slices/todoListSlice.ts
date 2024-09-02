import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { ITodoListsState, ITodoList, ITodoCard } from '../../interfaces';
import { clearUser, clearWorkSpace } from '../store';

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
    deleteSingleTodoList: (state, action: PayloadAction<number>) => {
      const idToDelete = action.payload;
      state.todoLists = state.todoLists.filter((todoList) => todoList.id !== idToDelete);

      state.todoLists = state.todoLists.map((todoList, index) => ({
        ...todoList,
        index,
      }));
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

    addCardToTodoList: (state, action: PayloadAction<ITodoCard>) => {
      const todoCard = action.payload;
      const todoListIndex = state.todoLists.findIndex((tl) => tl.id === todoCard.todoListId);
      state.todoLists[todoListIndex].cards.push(todoCard);
    },

    clearTodoLists: () => {
      return initialState;
    },

    updateTodoListTodoCard: (state, action: PayloadAction<ITodoCard>) => {
      const updatedTodoCard = action.payload;
      const todoListIndex = state.todoLists.findIndex((tl) => tl.id === updatedTodoCard.todoListId);
      const todoCardIndex = state.todoLists[todoListIndex].cards.findIndex((card) => card.id === updatedTodoCard.id);

      state.todoLists[todoListIndex].cards[todoCardIndex] = updatedTodoCard;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
    builder.addCase(clearWorkSpace, () => {
      return initialState;
    });
  },
});

export const {
  updateTodoListTodoCard,
  addCardToTodoList,
  deleteSingleTodoList,
  setTodoLists,
  addToTodoList,
  clearTodoLists,
  updateSingleTodoList,
} = todoListsSlice.actions;

export const todoListsReducer = todoListsSlice.reducer;
