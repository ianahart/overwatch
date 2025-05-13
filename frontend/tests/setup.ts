import '@testing-library/jest-dom/vitest'; // Import necessary testing utilities
import ResizeObserver from 'resize-observer-polyfill';
import { vi } from 'vitest';
import { server } from './mocks/server'; // If you're using MSW

// Ensure mocks and server setup are initialized properly
beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

// Global mocks for window properties (for elements like scrollIntoView)
global.ResizeObserver = ResizeObserver;
window.HTMLElement.prototype.scrollIntoView = vi.fn();
window.HTMLElement.prototype.hasPointerCapture = vi.fn();
window.HTMLElement.prototype.releasePointerCapture = vi.fn();

// Mock matchMedia
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
export const mockSearchParams = vi.fn();

let searchParams: Record<string, string> = {};

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useSearchParams: () => [new URLSearchParams(Object.entries(searchParams)), vi.fn()],
  };
});

export function mockUserSearchParams(params: Record<string, string>) {
  searchParams = params;
}
