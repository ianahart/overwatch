import { screen, render } from '@testing-library/react';

import ReplyCommentActions from '../../../src/components/ReplyComment/ReplyCommentActions';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('ReplyCommentActions', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const props = {
      handleSetIsEditing: vi.fn(),
      handleDeleteReplyComment: vi.fn(),
    };
    render(<ReplyCommentActions {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      getEditButton: () => screen.getByTestId('reply-comment-edit'),
      getDeleteButton: () => screen.getByTestId('reply-comment-delete'),
      getEditIcon: () => screen.getByRole('edit-reply-comment-icon'),
      getDeleteIcon: () => screen.getByRole('delete-reply-comment-icon'),
      props,
    };
  };
  it('should render edit and delete icon with tooltips', () => {
    const { getEditIcon, getDeleteIcon } = renderComponent();

    expect(getEditIcon()).toBeInTheDocument();
    expect(getDeleteIcon()).toBeInTheDocument();
  });

  it('should call "handleSetIsEditing" when edit icon is clicked', async () => {
    const { user, props, getEditButton } = renderComponent();

    await user.click(getEditButton());

    expect(props.handleSetIsEditing).toHaveBeenCalledWith(true);
  });

  it('should call "handleDeleteReplyComment" when the delete icon is clicked', async () => {
    const { user, props, getDeleteButton } = renderComponent();

    await user.click(getDeleteButton());

    expect(props.handleDeleteReplyComment).toHaveBeenCalled();
  });
});
