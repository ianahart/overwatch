import { screen, render } from '@testing-library/react';

import TopicDetailsCommentItemActions from '../../../src/components/TopicDetails/TopicDetailsCommentItemActions';
import { getLoggedInUser } from '../../utils';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import { db } from '../../mocks/db';
import { IComment, IUser } from '../../../src/interfaces';
import { getWrapper } from '../../RenderWithProviders';

describe('TopicDetailsCommentItemActions', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (curUser: IUser, overrides = {}) => {
    const commentEntity = db.comment.create();
    const comment: IComment = { ...toPlainObject(commentEntity), userId: curUser.id };

    return {
      curUserHasSaved: false,
      commentUserId: comment.userId,
      currentUserFullName: curUser.fullName,
      commentAuthorFullName: comment.fullName,
      handleSetIsEditing: vi.fn(),
      commentId: comment.id,
      content: comment.content,
      updateSavedComment: vi.fn(),
      updateCommentReaction: vi.fn(),
      removeCommentReaction: vi.fn(),
      ...overrides,
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(toPlainObject(curUser));

    render(<TopicDetailsCommentItemActions {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render edit and delete buttons for comment owner', () => {
    renderComponent();

    const editButton = screen.getByTestId('topic-detail-comment-edit');
    const deleteButton = screen.getByTestId('topic-detail-comment-delete');

    expect(editButton).toBeInTheDocument();
    expect(deleteButton).toBeInTheDocument();
  });

  it('should not render edit and delete buttons for non comment owner', () => {
    const curUser = {
      ...db.user.create(),
      id: 999,
      token: '',
    };

    const wrapper = getWrapper({
      user: {
        user: curUser,
        token: curUser.token,
      },
    } as any);

    const props = getProps(toPlainObject(curUser), { commentUserId: 1 });

    render(<TopicDetailsCommentItemActions {...props} />, { wrapper });

    const editButton = screen.queryByTestId('topic-detail-comment-edit');
    const deleteButton = screen.queryByTestId('topic-detail-comment-delete');

    expect(editButton).not.toBeInTheDocument();
    expect(deleteButton).not.toBeInTheDocument();
  });

  it('should save comment saved comment when save icon is clicked', async () => {
    const { user, props } = renderComponent();

    const saveButton = await screen.findByTestId('save-saved-comment');

    expect(saveButton).toBeInTheDocument();

    await user.click(saveButton);

    expect(props.updateSavedComment).toHaveBeenCalledWith(props.commentId, true);
  });

  it('should open emoji reation popover on click', async () => {
    const { user } = renderComponent();

    const popover = await screen.findByTestId('topic-emoji-popover');

    await user.click(popover);

    const emojis = await screen.findAllByTestId(/^emoji-/);

    expect(emojis.length).toBeGreaterThan(0);
  });

  it('should open reply modal and render modal content', async () => {
    const { user } = renderComponent();

    const trigger = await screen.findByTestId('reply-comment-modal-trigger');

    await user.click(trigger);

    expect(await screen.findByTestId('reply-modal-form')).toBeInTheDocument();
  });

  it('should open report modal and render modal content', async () => {
    const { user } = renderComponent();

    const trigger = await screen.findByTestId('report-comment-modal-trigger');

    await user.click(trigger);

    expect(await screen.findByRole('heading', { name: /report this comment/i })).toBeInTheDocument();
  });

  it('should delete a commment when delete icon is clicked', async () => {
    const { user } = renderComponent();

    const deleteIcon = await screen.findByTestId('topic-detail-comment-delete');

    await user.click(deleteIcon);
  });
});
