import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IConnection, IMessage } from '../../interfaces';
import { clearUser } from '../store';
import { connectionState } from '../../data';

interface IChatState {
  messages: IMessage[];
  connections: IConnection[];
  currentConnection: IConnection;
}

const initialState: IChatState = {
  messages: [],
  connections: [],
  currentConnection: connectionState,
};

const chatSlice = createSlice({
  name: 'chat',
  initialState,
  reducers: {
    setCurrentConnection: (state, action: PayloadAction<IConnection>) => {
      state.currentConnection = action.payload;
      state.messages = [];
    },

    setConnections: (state, action: PayloadAction<IConnection[]>) => {
      state.connections = [...state.connections, ...action.payload];
    },

    setMessages: (state, action: PayloadAction<IMessage[]>) => {
      state.messages = [...state.messages, ...action.payload];
    },

    addMessage: (state, action: PayloadAction<IMessage>) => {
      state.messages = [action.payload, ...state.messages];
    },
    clearMessages: (state) => {
      state.messages = [];
    },

    clearChat: () => {
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(clearUser, () => {
      return initialState;
    });
  },
});

export const { clearMessages, addMessage, setMessages, clearChat, setConnections, setCurrentConnection } =
  chatSlice.actions;

export const chatReducer = chatSlice.reducer;
