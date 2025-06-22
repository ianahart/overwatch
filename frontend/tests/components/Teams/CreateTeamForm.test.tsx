import { screen, render, waitFor } from '@testing-library/react';

import CreateTeamForm from '../../../src/components/Teams/CreateTeamForm';
import { getLoggedInUser } from '../../utils';
import userEvent from '@testing-library/user-event';

describe('CreateTeamForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      closeModal: vi.fn(),
    };
  };

  const getForm = () => {
    return {
      getInput: () => screen.getByLabelText(/team name/i),
      getTextarea: () => screen.getByLabelText(/team description/i),
      getCreateButton: () => screen.getByRole('button', { name: /create/i }),
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    const props = getProps();
    const form = getForm();

    render(<CreateTeamForm {...props} />, { wrapper });

    return {
      form,
      props,
      user: userEvent.setup(),
    };
  };

  it('should render form inputs and buttons', () => {
    const { form } = renderComponent();

    expect(form.getInput()).toBeInTheDocument();
    expect(form.getTextarea()).toBeInTheDocument();
    expect(form.getCreateButton()).toBeInTheDocument();
    expect(form.getCancelButton()).toBeInTheDocument();
  });

  it('should show validation error for empty submission', async () => {
    const { user, form } = renderComponent();

    await user.clear(form.getInput());
    await user.clear(form.getTextarea());

    await user.click(form.getCreateButton());

    expect(await screen.findByText('Name must be between 1 and 100 characters')).toBeInTheDocument();
    expect(await screen.findByText('Description must be between 1 and 200 characters')).toBeInTheDocument();
  });

  it('should show validation error for empty submission', async () => {
    const { user, form } = renderComponent();

    await user.clear(form.getInput());
    await user.clear(form.getTextarea());

    await user.type(form.getInput(), 'x'.repeat(101));
    await user.type(form.getTextarea(), 'x'.repeat(201));

    await user.click(form.getCreateButton());

    expect(await screen.findByText('Name must be between 1 and 100 characters')).toBeInTheDocument();
    expect(await screen.findByText('Description must be between 1 and 200 characters')).toBeInTheDocument();
  });

  it('should call "closeModal" on successful team creation', async () => {
    const { user, props, form } = renderComponent();

    await user.type(form.getInput(), 'team name');
    await user.type(form.getTextarea(), 'team description');

    await waitFor(() => {
      expect(form.getInput()).toHaveValue('team name');
      expect(form.getTextarea()).toHaveValue('team description');
    });

    await user.click(form.getCreateButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });
});
