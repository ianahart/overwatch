import { screen, render } from '@testing-library/react';

import ReplyCommentList from '../../../src/components/ReplyComment/ReplyCommentList';
import { AllProviders } from '../../AllProviders';
import { createReplyComments } from '../../mocks/dbActions';

describe('ReplyCommentList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const replyComments = createReplyComments(2);

    const props = {
      replyComments,
      updateReplyComment: vi.fn(),
      deleteReplyComment: vi.fn(),
      parentCommentId: 1,
    };

    render(<ReplyCommentList {...props} />, { wrapper: AllProviders });

    return {
      replyComments,
    };
  };
  it('should render the correct number of reply comment items', () => {
    const { replyComments } = renderComponent();

    const replyCommentItems = screen.getAllByTestId('reply-comment-item');

    expect(replyCommentItems).toHaveLength(replyComments.length);
  });

  it('should render the reply comment content', () => {
    const { replyComments } = renderComponent();

    for (const replyComment of replyComments) {
      expect(screen.getByText(replyComment.content)).toBeInTheDocument();
    }
  });
});
