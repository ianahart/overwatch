import { screen, render, waitFor } from '@testing-library/react';

import TopicDetailsReplyModalContent from '../../../src/components/TopicDetails/TopicDetailsReplyModalContent';
import { getLoggedInUser } from '../../utils';
import { db } from '../../mocks/db';
import userEvent from '@testing-library/user-event';

describe('TopicDetailsReplyModalContent', () => {
  beforeEach(() => {
    db.comment.delete({ where: { id: { equals: 1 } } });
    vi.clearAllMocks();
  });

  const getProps = (user: any, overrides = {}) => {
    const comment = db.comment.create({ id: 1, userId: user });

    return {
      commentUserId: comment.userId!.id,
      currentUserAvatarUrl: user.avatarUrl,
      commentAuthorFullName: comment.fullName,
      currentUserFullName: user.fullName,
      currentUserId: user.id,
      commentId: comment.id,
      comment: comment.content,
      closeModal: vi.fn(),
      ...overrides,
    };
  };

  const getForm = () => {
    return {
      getTextarea: () => screen.getByLabelText(/reply comment/i),
      getReplyButton: () => screen.getByRole('button', { name: /reply/i }),
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps(curUser);

    render(<TopicDetailsReplyModalContent {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      form: () => getForm(),
    };
  };

  it('should render the comment and reply textarea', () => {
    const { props, form } = renderComponent();

    expect(screen.getByText(props.comment)).toBeInTheDocument();
    expect(form().getTextarea()).toBeInTheDocument();
  });

  it('should show an error if submitting empty content', async () => {
    const { user, form } = renderComponent();

    await user.click(form().getReplyButton());

    expect(await screen.findByText('Comment must be between 1 and 400 characters')).toBeInTheDocument();
  });

  it('should submit valid reply and close modal', async () => {
    const { user, form, props } = renderComponent();

    await user.type(form().getTextarea(), 'this is a reply');
    await user.click(form().getReplyButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });

  it('should handle API Error correctly', async () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps(curUser, { currentUserId: 0, commentUserId: 0 });

    render(<TopicDetailsReplyModalContent {...props} />, { wrapper });

    const form = getForm();
    const user = userEvent.setup();

    await user.type(form.getTextarea(), 'some reply');
    await user.click(form.getReplyButton());

    expect(await screen.findByText('Missing userId or content')).toBeInTheDocument();
  });

  it('should limit characters to 400 character count', async () => {
    const { user, form } = renderComponent();

    await user.type(form().getTextarea(), 'a'.repeat(402));
    await user.click(form().getReplyButton());

    expect(await screen.findByText('Comment must be between 1 and 400 characters')).toBeInTheDocument();
  });
});
