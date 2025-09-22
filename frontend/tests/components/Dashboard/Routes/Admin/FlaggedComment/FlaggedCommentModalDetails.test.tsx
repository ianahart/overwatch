import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';

import { IReportComment } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import FlaggedCommentModalDetails from '../../../../../../src/components/Dashboard/Routes/Admin/FlaggedComment/FlaggedCommentModalDetails';
import { getLoggedInUser } from '../../../../../utils';
import dayjs from 'dayjs';

describe('FlaggedCommentModalDetails', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides: Partial<IReportComment> = {}) => {
    const reportComment: IReportComment = { ...toPlainObject(db.reportComment.create()), ...overrides };
    return {
      reportComment,
      updateStatus: vi.fn(),
      handleCloseModal: vi.fn(),
    };
  };

  const renderComponent = (overrides: Partial<IReportComment> = {}) => {
    const props = getProps(overrides);

    const { wrapper } = getLoggedInUser();

    render(<FlaggedCommentModalDetails {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render report comment details', () => {
    const { props } = renderComponent();

    const { topicTitle, reportedBy, createdAt, content, details, commentAvatarUrl, reason } = props.reportComment;

    const avatar = screen.getByRole('img');
    expect(avatar).toHaveAttribute('src', commentAvatarUrl);
    expect(screen.getByText(new RegExp(`${topicTitle}`, 'i'))).toBeInTheDocument();
    expect(screen.getByText(`reported by ${reportedBy}`)).toBeInTheDocument();
    expect(screen.getByText(new RegExp(`${content}`, 'i'))).toBeInTheDocument();
    expect(screen.getByText(details)).toBeInTheDocument();
    expect(screen.getByText(`${dayjs(createdAt).format('MM/D/YYYY')}`)).toBeInTheDocument();
    expect(screen.getByText(reason)).toBeInTheDocument();
  });

  it('should render active buttons when status is not ACTIVE', () => {
    renderComponent({ status: 'PENDING' });

    expect(screen.queryByRole('button', { name: /delete/i })).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /cancel/i })).not.toBeInTheDocument();
  });

  it('should call "deleteReportComment" and update state on delete', async () => {
    const { user, props } = renderComponent({ status: 'ACTIVE' });

    await user.click(screen.getByRole('button', { name: /delete/i }));

    await waitFor(() => {
      expect(props.updateStatus).toHaveBeenCalledWith(props.reportComment.id, 'DELETED');
    });
  });
  it('should call "handleCloseModal" when cancel is clicked', async () => {
    const { user, props } = renderComponent({ status: 'ACTIVE' });

    await user.click(screen.getByRole('button', { name: /cancel/i }));

    await waitFor(() => {
      expect(props.handleCloseModal).toHaveBeenCalled();
    });
  });
});
