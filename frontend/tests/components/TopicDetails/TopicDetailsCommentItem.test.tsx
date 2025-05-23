import { screen, render } from '@testing-library/react';

import TopicDetailsCommentItem from '../../../src/components/TopicDetails/TopicDetailsCommentItem';
import { getLoggedInUser } from '../../utils';
import { db } from '../../mocks/db';
import { IComment, IUser } from '../../../src/interfaces';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import { mockNavigate } from '../../setup';
import { getWrapper } from '../../RenderWithProviders';

describe('TopicDetailsCommentItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });
  const getProps = (curUser: IUser) => {
    const commentEntity = db.comment.create({ isEdited: true, replyCommentsCount: 2 });

    const updateCommentVote = vi.fn();
    const updateSavedComment = vi.fn();
    const updateCommentReaction = vi.fn();
    const removeCommentReaction = vi.fn();

    const comment: IComment = { ...commentEntity, userId: curUser.id };

    return {
      comment,
      updateCommentVote,
      updateSavedComment,
      updateCommentReaction,
      removeCommentReaction,
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(toPlainObject(curUser));

    render(<TopicDetailsCommentItem {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      navigate: mockNavigate,
    };
  };

  it('should render comment content and edited tag', () => {
    const { props } = renderComponent();
    const { comment } = props;

    expect(screen.getByText(comment.content)).toBeInTheDocument();
    expect(screen.getByText('(edited)')).toBeInTheDocument();
  });

  it('should redirect to signin on vote when user is not logged in', async () => {
    const curUser = {
      ...db.user.create(),
      id: 0,
      token: '',
    };

    const wrapper = getWrapper({
      user: {
        user: curUser,
        token: curUser.token,
      },
    } as any);

    const props = getProps(toPlainObject(curUser));
    render(<TopicDetailsCommentItem {...props} />, { wrapper });

    const voteButton = screen.getByTestId('comment-upvote');

    const user = userEvent.setup();

    await user.click(voteButton);

    expect(mockNavigate).toHaveBeenCalledWith('/signin');
  });

  it('should show reply button and trigger more reply comments on click', async () => {
    const { user } = renderComponent();

    await user.click(screen.getByRole('button', { name: /reply comments/i }));

    const replyComments = await screen.findAllByTestId('reply-comment-item');

    expect(replyComments.length).toBeGreaterThan(0);
  });

  it('should load more replies when "Load more replies" button is clicked', async () => {
    const { user } = renderComponent();

    await user.click(await screen.findByRole('button', { name: /^reply comments/i }));
    await user.click(await screen.findByRole('button', { name: /load more replies/i }));

    const replyComments = await screen.findAllByTestId('reply-comment-item');

    expect(replyComments.length).toBe(4);
  });

  it('should show a form when the edit button is clicked', async () => {
    const { user } = renderComponent();

    const editButton = await screen.findByTestId('topic-detail-comment-edit');

    await user.click(editButton);

    expect(await screen.findByTestId('update-comment-form')).toBeInTheDocument();
  });
});
