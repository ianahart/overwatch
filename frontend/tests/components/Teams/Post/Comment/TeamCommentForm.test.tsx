import { screen, render, waitFor } from '@testing-library/react';

import TeamCommentForm from '../../../../../src/components/Teams/Post/Comment/TeamCommentForm';
import { getLoggedInUser } from '../../../../utils';
import userEvent from '@testing-library/user-event';
import { setMockParams } from '../../../../setup';

describe('TeamCommentForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    setMockParams({ teamId: '1' });
  });

  const getProps = (overrides = {}) => {
    return {
      teamCommentId: 1,
      teamPostId: 1,
      formType: 'create',
      closeModal: vi.fn(),
      handleResetComments: vi.fn(),
      updateTeamComment: vi.fn(),
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(overrides);

    render(<TeamCommentForm {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
      getTextarea: () => screen.getByRole('textbox'),
      props,
    };
  };

  it('should render the form correctly in create mode', () => {
    renderComponent();

    expect(screen.getByText('Create Comment')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /create/i })).toBeInTheDocument();
  });

  it('should render the form correctly in edit mode', () => {
    renderComponent({ formType: 'edit' });

    expect(screen.getByText('Edit Comment')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /update/i })).toBeInTheDocument();
  });

  it('should tag a team member after typing "@" and searching', async () => {
    const { user, getTextarea } = renderComponent();

    await user.type(getTextarea(), '@John');

    const searchInput = screen.getByPlaceholderText(/enter team member/i);

    await waitFor(() => {
      expect(searchInput).toBeInTheDocument();
    });

    await user.type(searchInput, 'John');

    await waitFor(() => {
      expect(screen.getByText(/load more/i)).toBeInTheDocument();
    });
  });

  it('should submit successfully in create mode with content only', async () => {
    const { props, user, getTextarea } = renderComponent();

    await user.type(getTextarea(), 'This is a comment');

    await user.click(screen.getByRole('button', { name: /create/i }));

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
      expect(props.handleResetComments).toHaveBeenCalled();
    });
  });

  it('should pre-fill and submit in edit mode', async () => {
    const { user, props, getTextarea } = renderComponent({ formType: 'edit' });

    await waitFor(() => {
      expect(getTextarea()).toHaveValue('original content');
    });

    await user.clear(getTextarea());
    await user.type(getTextarea(), 'updated content'),
      await user.click(screen.getByRole('button', { name: /update/i }));

    await waitFor(() => {
      expect(getTextarea()).toHaveValue('updated content');
      expect(props.updateTeamComment).toHaveBeenCalledWith(1, 'updated content', expect.any(String));
      expect(props.closeModal).toHaveBeenCalled();
    });
  });
  it('should cancel when "Cancel" button is clicked', async () => {
    const { user, props } = renderComponent();

    await user.click(screen.getByRole('button', { name: /cancel/i }));

    expect(props.closeModal).toHaveBeenCalled();
  });
});
