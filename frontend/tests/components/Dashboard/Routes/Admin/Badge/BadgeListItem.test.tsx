import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { IAdminBadge } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import BadgeListItem from '../../../../../../src/components/Dashboard/Routes/Admin/Badge/BadgeListItem';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';

describe('BadgeListItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const badge: IAdminBadge = { ...toPlainObject(db.adminBadge.create()) };
    return { badge };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<BadgeListItem {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      getUpdateBtn: () => screen.getByRole('button', { name: /update/i }),
      getDeleteBtn: () => screen.getByRole('button', { name: /delete/i }),
    };
  };

  it('should render badge information', () => {
    const { props } = renderComponent();

    const { title, description, imageUrl } = props.badge;

    const badgeImage = screen.getByRole('img');
    expect(badgeImage).toHaveAttribute('src', imageUrl);
    expect(screen.getByRole('heading', { name: title })).toBeInTheDocument();
    expect(screen.getByText(description)).toBeInTheDocument();
  });

  it('should open modal when "update" is clicked', async () => {
    const { user, getUpdateBtn } = renderComponent();

    await user.click(getUpdateBtn());

    expect(await screen.findByTestId('BadgeForm')).toBeInTheDocument();
  });

  it('should close modal when close button inside form is clicked', async () => {
    const { user, getUpdateBtn } = renderComponent();

    await user.click(getUpdateBtn());

    expect(await screen.findByTestId('BadgeForm')).toBeInTheDocument();

    await user.click(await screen.findByTestId('details-modal-close-btn'));

    await waitFor(() => {
      expect(screen.queryByTestId('BadgeForm')).not.toBeInTheDocument();
    });
  });
});
