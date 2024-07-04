import { PayloadAction, createSlice } from '@reduxjs/toolkit';
import { IConnection, IMessage, IPinnedConnection } from '../../interfaces';
import { clearUser } from '../store';
import { connectionState } from '../../data';

interface IChatState {
  messages: IMessage[];
  connections: IConnection[];
  currentConnection: IConnection;
  pinnedConnections: IPinnedConnection[];
}

const initialState: IChatState = {
  messages: [],
  connections: [],
  currentConnection: connectionState,
  pinnedConnections: [],
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

    setPinnedConnections: (state, action: PayloadAction<IPinnedConnection[]>) => {
      state.pinnedConnections = [...state.pinnedConnections, ...action.payload];
    },

    removeConnection: (state, action: PayloadAction<IConnection>) => {
      state.connections = state.connections.filter((connection) => connection.id !== action.payload.id);
    },

    removePinnedConnection: (state, action: PayloadAction<number>) => {
      state.pinnedConnections = state.pinnedConnections.filter(
        (connection) => connection.connectionPinId !== action.payload
      );
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

    clearPinnedConnections: (state) => {
      state.pinnedConnections = [];
    },

    clearConnections: (state) => {
      state.connections = [];
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

export const {
  removeConnection,
  clearMessages,
  addMessage,
  setMessages,
  clearChat,
  setConnections,
  setCurrentConnection,
  setPinnedConnections,
  removePinnedConnection,
  clearPinnedConnections,
  clearConnections,
} = chatSlice.actions;

export const chatReducer = chatSlice.reducer;
