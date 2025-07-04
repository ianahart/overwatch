import '@testing-library/jest-dom/vitest';
import ResizeObserver from 'resize-observer-polyfill';
import { vi } from 'vitest';
import { server } from './mocks/server';

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

global.ResizeObserver = ResizeObserver;
window.HTMLElement.prototype.scrollIntoView = vi.fn();
window.HTMLElement.prototype.hasPointerCapture = vi.fn();
window.HTMLElement.prototype.releasePointerCapture = vi.fn();

global.Range.prototype.getClientRects = () => {
  return {
    length: 0,
    item: () => null,
    [Symbol.iterator]: function* () {},
  } as any;
};

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
});

export const mockNavigate = vi.fn();
export const mockDispatch = vi.fn();
export const mockLocation = vi.fn().mockReturnValue({
  pathname: '/',
  state: {},
});

let params: Record<string, string> = {};
let searchParams: Record<string, string> = {};

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useParams: () => params,
    useNavigate: () => mockNavigate,
    useLocation: () => mockLocation(),
    useSearchParams: () => [new URLSearchParams(Object.entries(searchParams)), vi.fn()],
  };
});

export function mockUserSearchParams(newParams: Record<string, string>) {
  searchParams = newParams;
}

export function setMockParams(newParams: Record<string, string>) {
  params = newParams;
}

vi.mock('../src/util', async () => {
  const actual = await vi.importActual('../src/util');
  return {
    ...actual,
    retrieveTokens: vi.fn().mockReturnValue({ token: 'mocked-token' }),
  };
});

vi.mock('../src/util/WebSocketService', () => ({
  connectWebSocket: vi.fn(),
  disconnectWebSocket: vi.fn(),
  subscribeToTopic: vi.fn(),
  sendMessage: vi.fn(),
}));
