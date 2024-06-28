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
    },

    setConnections: (state, action: PayloadAction<IConnection[]>) => {
      state.connections = [...state.connections, ...action.payload];
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

export const { clearChat, setConnections, setCurrentConnection } = chatSlice.actions;

export const chatReducer = chatSlice.reducer;
