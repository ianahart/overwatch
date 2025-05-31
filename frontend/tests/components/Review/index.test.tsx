import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { toPlainObject } from 'lodash';

import Reviews from '../../../src/components/Review';
import { getLoggedInUser } from '../../utils';
import { IUser } from '../../../src/interfaces';
import { Role } from '../../../src/enums';
import { mockNavigate } from '../../setup';

describe('Reviews', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (user: IUser, overrides = {}) => {
    return {
      userId: user.id,
      fullName: user.fullName,
      avatarUrl: user.avatarUrl,
      ...overrides,
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser({ role: Role.USER });

    const props = getProps(toPlainObject(curUser));

    render(<Reviews {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      getTitle: () => screen.getByRole('heading', { name: /reviews/i }),
      getCreateButton: () => screen.getByRole('button', { name: /write review/i }),
      reviewerId: curUser.id,
      authorId: curUser.id,
    };
  };

  it('should render the title correctly', () => {
    const { getTitle } = renderComponent();

    expect(getTitle()).toBeInTheDocument();
  });

  it('should show the "Write review" button if the user has a role of type "USER"', () => {
    const { getCreateButton } = renderComponent();

    expect(getCreateButton()).toBeInTheDocument();
  });

  it('should render reviews correctly', async () => {
    renderComponent();
    const pageSize = 2;

    const reviews = await screen.findAllByTestId('review-display-item');

    expect(reviews).toHaveLength(pageSize);
    expect(await screen.findByText('1 of 10')).toBeInTheDocument();
  });

  it('should navigate to /reviews/create on write review click', async () => {
    const { user, props, getCreateButton, reviewerId, authorId } = renderComponent();
    const { fullName, avatarUrl } = props;

    await user.click(getCreateButton());

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/reviews/create', {
        state: { fullName, avatarUrl, reviewerId, authorId },
      });
    });
  });
});
