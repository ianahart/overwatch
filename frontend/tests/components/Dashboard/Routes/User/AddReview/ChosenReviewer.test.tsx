import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import ChosenReviewer from '../../../../../../src/components/Dashboard/Routes/User/AddReview/ChosenReviewer';
import { mockNavigate } from '../../../../../setup';
import { getLoggedInUser } from '../../../../../utils';
import { db } from '../../../../../mocks/db';
import { IConnection } from '../../../../../../src/interfaces';
import userEvent from '@testing-library/user-event';
import { server } from '../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../src/util';

describe('ChosenReviewer', () => {
  const renderComponent = (overrides: Partial<IConnection> = {}) => {
    const connectionEntity = db.connection.create();

    const selectedReviewer: IConnection = {
      ...toPlainObject(connectionEntity),
      receiverId: 2,
      senderId: 1,
      ...overrides,
    };

    const { curUser, wrapper } = getLoggedInUser(
      {},
      {
        addReview: {
          selectedReviewer,
        },
      }
    );

    render(<ChosenReviewer />, { wrapper });

    return {
      user: userEvent.setup(),
      selectedReviewer,
      curUser,
    };
  };

  it('should show info message when no reviewer is selected', () => {
    renderComponent({ id: 0 });

    expect(screen.getByText(/you have not chosen a reviewer to review your code yet/i));
  });

  it('should render the selected reviewers information', async () => {
    const { selectedReviewer } = renderComponent();

    expect(await screen.findByText(/you have selected/i)).toBeInTheDocument();
    const avatar = await screen.findByRole('img');
    expect(avatar).toHaveAttribute('src', selectedReviewer.avatarUrl);
    expect(await screen.findByText(`${selectedReviewer.firstName} ${selectedReviewer.lastName}`)).toBeInTheDocument();
  });

  it('should redirect to billing if the user has no payment method', async () => {
    server.use(
      http.get(`${baseURL}/users/:userId/payment-methods`, () => {
        return HttpResponse.json({ message: 'error: payment method does not exist' }, { status: 404 });
      })
    );
    const { curUser } = renderComponent();

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(`/settings/${curUser.slug}/billing?toast=show`);
    });
  });
});
