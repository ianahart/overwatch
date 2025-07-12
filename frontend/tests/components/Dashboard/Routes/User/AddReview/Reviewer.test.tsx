import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import Reviewer from '../../../../../../src/components/Dashboard/Routes/User/AddReview/Reviewer';
import { IConnection, IPinnedConnection } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../utils';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import userEvent from '@testing-library/user-event';
import { setSelectedReviewer } from '../../../../../../src/state/store';

type TConnection = IConnection | IPinnedConnection;

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('Reviewer', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const connection: TConnection = {
      ...toPlainObject(db.connection.create()),
      senderId: 1,
      receiverId: 2,
      connectionPinId: 1,
    };

    return { data: connection };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps();

    const selectedReviewer = { ...props.data, ...overrides };

    const { wrapper } = getLoggedInUser(
      {},
      {
        addReview: {
          selectedReviewer,
        },
      }
    );

    render(<Reviewer {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      getSelectBtn: () => screen.getByRole('button', { name: /select/i }),
    };
  };

  it('should render reviewer details correctly', () => {
    const { props } = renderComponent();

    const { firstName, lastName, avatarUrl, city, country } = props.data;

    const avatarImage = screen.getByRole('img');

    expect(avatarImage).toHaveAttribute('src', avatarUrl);
    expect(screen.getByText(`${firstName} ${lastName}`)).toBeInTheDocument();
    expect(screen.getByText(`${city}, ${country}`)).toBeInTheDocument();
    //            expect(mockDispatch).toHaveBeenCalledWith(clearChat());
  });

  it('should dispatch "setSelectedReviewer" when select is clicked', async () => {
    const { user, props, getSelectBtn } = renderComponent({ id: 0 });

    await user.click(getSelectBtn());

    expect(mockDispatch).toHaveBeenCalledWith(setSelectedReviewer({ reviewer: props.data }));
  });

  it('should show the lightning icon to be gray if reviewer is not selected', async () => {
    renderComponent({ id: 0 });

    const lightning = screen.getByTestId('lightning-icon');

    expect(lightning).toHaveClass('text-gray-400');
  });

  it('should show the lightning icon to be green if reviewer is selected', async () => {
    renderComponent();

    const lightning = screen.getByTestId('lightning-icon');

    expect(lightning).toHaveClass('text-green-400');
  });
});
