import { screen, render, waitFor } from '@testing-library/react';

import CreateTeamBtn from '../../../src/components/Teams/CreateTeamBtn';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('CreateTeamBtn', () => {
  const renderComponent = () => {
    render(<CreateTeamBtn />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getCreateButton: () => screen.getByRole('button', { name: /create team/i }),
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  it('should render the create team button', () => {
    const { getCreateButton } = renderComponent();

    expect(getCreateButton()).toBeInTheDocument();
  });

  it('should open modal when "Create Team" button is clicked', async () => {
    const { user, getCreateButton } = renderComponent();

    await user.click(getCreateButton());

    await waitFor(() => {
      expect(screen.getByTestId('create-team-form')).toBeInTheDocument();
    });
  });

  it('should close modal when cancel button is clicked', async () => {
    const { user, getCreateButton, getCancelButton } = renderComponent();

    await user.click(getCreateButton());

    expect(await screen.findByTestId('create-team-form')).toBeInTheDocument();

    await user.click(getCancelButton());

    await waitFor(() => {
      expect(screen.queryByTestId('create-team-form')).not.toBeInTheDocument();
    });
  });
});
