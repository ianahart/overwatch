import { screen, render, waitFor } from '@testing-library/react';
import * as toastify from 'react-toastify';
import userEvent from '@testing-library/user-event';

import GetPaidCompleteRepositoryList from '../../../../src/components/Settings/GetPaid/GetPaidCompleteRepositoryList';
import { getLoggedInUser } from '../../../utils';

describe('GetPaidCompleteRepositoryList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<GetPaidCompleteRepositoryList />, { wrapper });

    return {
      user: userEvent.setup(),
    };
  };

  it('should render repository list', async () => {
    renderComponent();

    const completedRepositories = await screen.findAllByTestId('complete-repository-list-item');

    expect(completedRepositories.length).toBe(2);
  });

  it('should show toast and remove repository on successful "Get paid"', async () => {
    const toastSpy = vi.spyOn(toastify.toast, 'success');

    const { user } = renderComponent();

    const getPaidBtn = await screen.findAllByRole('button', { name: /get paid/i });

    await user.click(getPaidBtn[0]);

    await waitFor(() => {
      expect(toastSpy).toHaveBeenCalledWith(
        expect.stringMatching(/congratulations/i),
        expect.objectContaining({ position: 'bottom-center' })
      );
    });
  });

  it('should show pagination controls when multiple pages exist', async () => {
    renderComponent();

    expect(await screen.findByRole('button', { name: /next/i })).toBeInTheDocument();
  });
});
