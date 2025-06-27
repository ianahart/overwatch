import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { vi, Mock } from 'vitest';

import NavigationLink from '../../../src/components/Settings/NavigationLink';
import { getLoggedInUser } from '../../utils';
import { IConnection } from '../../../src/interfaces';
import { db } from '../../mocks/db';
import { mockUserLocation } from '../../setup';
import { clearChat } from '../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('NavigationLink', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const data = { path: '/test-path', name: 'Test Path' };

    return {
      data,
    };
  };

  const renderComponent = () => {
    const connection: IConnection = { ...toPlainObject(db.connection.create()), senderId: 1, receiverId: 2 };

    const { wrapper } = getLoggedInUser(
      {},
      {
        chat: {
          connections: [connection],
        },
      }
    );
    const props = getProps();

    render(<NavigationLink {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render link with correct name and path', () => {
    const { props } = renderComponent();

    const link = screen.getByRole('link', { name: /test path/i });

    expect(link).toBeInTheDocument();
    expect(link).toHaveAttribute('href', props.data.path);
  });

  it('should apply active styles when location matches path', () => {
    mockUserLocation({ pathname: '/test-path' });

    const { props } = renderComponent();

    const listItem = screen.getByRole('link', { name: props.data.name }).closest('li');

    expect(listItem).toHaveClass('border-green-400');
    expect(listItem).toHaveClass('text-green-400');
  });

  it('should apply non-active styles when link is not active', () => {
    mockUserLocation({ pathname: '/' });
    const { props } = renderComponent();

    const listItem = screen.getByRole('link', { name: props.data.name }).closest('li');
    expect(listItem).toHaveClass('border-gray-400');
    expect(listItem).not.toHaveClass('text-green-400');
  });
  it('should dispatch clearChat if connections exist', async () => {
    const { user, props } = renderComponent();

    const listItem = screen.getByRole('link', { name: props.data.name }).closest('li')!;
    await user.click(listItem);

    expect(mockDispatch).toHaveBeenCalledWith(clearChat());
  });
});
