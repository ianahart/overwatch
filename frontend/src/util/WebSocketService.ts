import SockJS from 'sockjs-client';
import { over, Client, Frame } from 'stompjs';

let stompClient: Client | null = null;

type OnConnectedCallback = () => void;
type OnErrorCallback = (err: any) => void;
type MessageCallback = (message: Frame) => void;

export const connectWebSocket = (onConnected: OnConnectedCallback, onError: OnErrorCallback) => {
  if (!stompClient || !stompClient.connected) {
    const Sock = new SockJS(import.meta.env.VITE_WEBSOCKET_ENDPOINT);
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
  }
};

export const disconnectWebSocket = () => {
  if (stompClient && stompClient.connected) {
    stompClient.disconnect(() => {
      console.log('WebSocket disconnected');
    });
  }
};

export const sendMessage = (destination: string, body: string) => {
  if (stompClient && stompClient.connected) {
    stompClient.send(destination, {}, body);
  } else {
    console.warn('WebSocket is not connected. Message not sent.');
  }
};

export const subscribeToTopic = (topic: string, callback: MessageCallback) => {
  if (stompClient && stompClient.connected) {
    return stompClient.subscribe(topic, callback);
  } else {
    console.warn('WebSocket is not connected. Subscription not created.');
    return null;
  }
};
