import { screen, render } from '@testing-library/react';
import { initializeName } from '../../../src/util';
import { db } from '../../mocks/db';
import CommentHeader from '../../../src/components/Comment/CommentHeader';
import { IMinComment } from '../../../src/interfaces';
import { createFullUser } from '../../mocks/dbActions';
import dayjs from 'dayjs';

vi.mock('../../../src/util', () => ({
  ...vi.importActual('../../../src/util'),
  initializeName: vi.fn(),
}));

describe('CommentHeader', () => {
  const renderComponent = () => {
    const user = createFullUser();

    const commentEntity = db.minComment.create({ userId: user });

    const comment: IMinComment = {
      ...commentEntity,
      id: commentEntity.id,
      userId: user.id,
    };

    render(<CommentHeader comment={comment} />);

    return { user, comment };
  };

  beforeEach(() => {
    const initializeName = vi.fn();
    initializeName.mockReturnValue('JD');
  });

  it('should render full name and formatted date', () => {
    const { comment } = renderComponent();

    expect(screen.getByText(comment.fullName)).toBeInTheDocument();
    expect(screen.getByText(dayjs(comment.createdAt).format('MM/D/YYYY'))).toBeInTheDocument();
  });

  it('should call initializeName with correct first and last name', () => {
    const { comment } = renderComponent();
    const [first, last] = comment.fullName.split(' ');

    expect(initializeName).toHaveBeenCalledWith(first, last);
  });

  it('should pass avatarUrl to Avatar component', () => {
    const { comment } = renderComponent();

    const avatarImg = screen.getByRole('img') as HTMLImageElement;
    expect(avatarImg).toHaveAttribute('src', comment.avatarUrl);
  });

  it('should handle names with one word safely', () => {
    const { comment } = renderComponent();
    const singleNameComment = {
      ...comment,
      fullName: 'Prince',
    };
    render(<CommentHeader comment={singleNameComment} />);
    expect(initializeName).toHaveBeenCalledWith('Prince', undefined);
  });
});
