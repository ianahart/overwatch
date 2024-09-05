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

    removeTodoListTodoCard: (state, action: PayloadAction<{ id: number; todoListId: number }>) => {
      const { id, todoListId } = action.payload;

      const todoListIndex = state.todoLists.findIndex((tl) => tl.id === todoListId);
      const todoCardIndex = state.todoLists[todoListIndex].cards.findIndex((card) => card.id === id);

      state.todoLists[todoListIndex].cards.splice(todoCardIndex, 1);
    },

    reorderTodoCards: (state, action: PayloadAction<{ listId: number; oldIndex: number; newIndex: number }>) => {
      const { listId, oldIndex, newIndex } = action.payload;
      const todoList = state.todoLists.find((tl) => tl.id === listId);

      if (todoList) {
        const [movedCard] = todoList.cards.splice(oldIndex, 1);
        todoList.cards.splice(newIndex, 0, movedCard);
      }
    },

    moveTodoCard: (
      state,
      action: PayloadAction<{ sourceListId: number; destinationListId: number; cardId: number; newIndex: number }>
    ) => {
      const { sourceListId, destinationListId, cardId, newIndex } = action.payload;
      const sourceTodoList = state.todoLists.find((tl) => tl.id === sourceListId);
      const destinationList = state.todoLists.find((tl) => tl.id === destinationListId);

      if (sourceTodoList && destinationList) {
        const cardIndex = sourceTodoList.cards.findIndex((card) => card.id === cardId);

        if (cardIndex > -1) {
          const [movedCard] = sourceTodoList.cards.splice(cardIndex, 1);
          destinationList.cards.splice(newIndex, 0, movedCard);
          destinationList.cards[newIndex].todoListId = destinationList.id;
        }
      }
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
  reorderTodoCards,
  moveTodoCard,
  removeTodoListTodoCard,
  updateTodoListTodoCard,
  addCardToTodoList,
  deleteSingleTodoList,
  setTodoLists,
  addToTodoList,
  clearTodoLists,
  updateSingleTodoList,
} = todoListsSlice.actions;

export const todoListsReducer = todoListsSlice.reducer;
