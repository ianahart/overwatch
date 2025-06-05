import { screen, render, waitFor } from '@testing-library/react';
import { toast } from 'react-toastify';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { server } from '../../../mocks/server';
import { HttpResponse, http } from 'msw';

import { getLoggedInUser } from '../../../utils';
import { baseURL } from '../../../../src/util';
import AddTeamMemberSearchBar from '../../../../src/components/Teams/AddTeamMember/AddTeamMemberSearchBar';

vi.mock('react-toastify', async (importOriginal) => {
  const actual = (await importOriginal()) as object;
  return {
    ...actual,
    toast: {
      success: vi.fn(),
    },
  };
});

describe('AddTeamMemberSearchBar', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const lookupReviewer = async (user: UserEvent, name: string) => {
    const { getInput } = getProps();

    await user.type(getInput(), name);
  };

  const sendInvitation = async (user: UserEvent) => {
    await lookupReviewer(user, 'john');

    const reviewer = await screen.findByText('john doe');
    const container = reviewer.closest('div');

    await user.click(container!);
  };

  const getProps = () => {
    return {
      getInput: () => screen.getByRole('textbox'),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    render(<AddTeamMemberSearchBar />, { wrapper });

    const props = getProps();

    return {
      user: userEvent.setup(),
      curUser,
      props,
    };
  };

  it('should render input and label', () => {
    const { props } = renderComponent();

    expect(props.getInput()).toBeInTheDocument();
    expect(screen.getByLabelText(/lookup a reviewer/i)).toBeInTheDocument();
  });

  it('should show reviewer results when typing into the input', async () => {
    const { user, props } = renderComponent();

    await user.type(props.getInput(), 'john');

    // john doe comes from handerls/user.ts
    expect(await screen.findByText('john doe')).toBeInTheDocument();
    const avatars = await screen.findAllByRole('img');

    expect(avatars[0]).toBeInTheDocument();
    expect(avatars.length).toBe(2);
  });

  it('should send an invitation when a reviewer is cliked and shows toast', async () => {
    const { user } = renderComponent();

    await sendInvitation(user);

    await waitFor(() => {
      expect(toast.success).toHaveBeenCalledWith(
        expect.stringContaining('You sent a team invitation to john doe'),
        expect.any(Object)
      );
    });
  });

  it('should show API error if team invitation send fails', async () => {
    server.use(
      http.post(`${baseURL}/teams/:teamId/invitations`, () => {
        return HttpResponse.json(
          {
            message: 'error sending invitation',
          },
          { status: 400 }
        );
      })
    );

    const { user } = renderComponent();

    await sendInvitation(user);

    expect(screen.getByText('error sending invitation')).toBeInTheDocument();
  });

  it('should load more reviewers on "See more..." click', async () => {
    const { user } = renderComponent();

    await lookupReviewer(user, 'john');

    const paginateButton = await screen.findByRole('button', { name: /see more/i });

    expect(paginateButton).toBeInTheDocument();

    await user.click(paginateButton);

    const reviewers = await screen.findAllByTestId('searchbar-reviewer');

    expect(reviewers.length).toBe(4);
  });
});
