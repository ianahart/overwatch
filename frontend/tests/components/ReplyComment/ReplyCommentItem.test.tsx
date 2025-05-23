import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import ReplyCommentItem from '../../../src/components/ReplyComment/ReplyCommentItem';
import { getLoggedInUser } from '../../utils';
import { db } from '../../mocks/db';
import userEvent from '@testing-library/user-event';
import { IComment } from '../../../src/interfaces';

describe('ReplyCommentItem', () => {
  beforeEach(() => {
    db.comment.delete({ where: { id: { equals: 1 } } });

    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const { wrapper, curUser } = getLoggedInUser();
    const comment = toPlainObject(db.comment.create({ userId: curUser })) as IComment;

    const props = {
      mockDeleteReplyComment: vi.fn(),
      mockUpdateReplyComment: vi.fn(),
      parentCommentId: 1,
      comment: { ...comment, userId: curUser.id },
    };

    render(<ReplyCommentItem dataTestId="reply-comment-item" {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      getTextarea: () => screen.getByRole('textbox'),
      getUpdateButton: () => screen.getByRole('button', { name: /update/i }),
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
      getEditIcon: () => screen.getByTestId('reply-comment-edit'),
      getDeleteIcon: () => screen.getByTestId('reply-comment-delete'),
      props,
    };
  };
  it('should render the reply comment in view mode', () => {
    const { props } = renderComponent();
    const { comment } = props;

    expect(screen.getByText(comment.content)).toBeInTheDocument();
    expect(screen.queryByRole('textbox')).not.toBeInTheDocument();
  });

  it('should enter the edit mode and show textarea with existing content', async () => {
    const { getTextarea, props, user, getEditIcon } = renderComponent();
    const { comment } = props;

    await user.click(getEditIcon());

    await waitFor(() => {
      expect(getTextarea()).toHaveValue(comment.content);
    });
  });
});
