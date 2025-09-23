import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import FlaggedCommentListItem from '../../../../../../src/components/Dashboard/Routes/Admin/FlaggedComment/FlaggedCommentListItem';
import { AllProviders } from '../../../../../AllProviders';
import { IReportComment } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

describe('FlaggedCommentListItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const reportComment: IReportComment = { ...toPlainObject(db.reportComment.create()) };
    return {
      reportComment,
      updateStatus: vi.fn(),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    render(<FlaggedCommentListItem {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render reporter, date, and status', () => {
    const { props } = renderComponent();

    const { status, reportedBy, createdAt } = props.reportComment;

    expect(screen.getByText(reportedBy)).toBeInTheDocument();
    expect(screen.getByText(dayjs(createdAt).format('MM/D/YYYY'))).toBeInTheDocument();
    expect(screen.getByText(`status: ${status}`)).toBeInTheDocument();
  });

  it('should open the modal when clicking details', async () => {
    const { user } = renderComponent();

    await user.click(screen.getByRole('button', { name: /details/i }));

    expect(await screen.findByTestId('DetailsModal')).toBeInTheDocument();
  });

  it('should close the modal', async () => {
    const { user } = renderComponent();

    await user.click(screen.getByRole('button', { name: /details/i }));

    expect(await screen.findByTestId('DetailsModal')).toBeInTheDocument();

    await user.click(screen.getByTestId('details-modal-close-btn'));

    await waitFor(() => {
      expect(screen.queryByTestId('DetailsModal')).not.toBeInTheDocument();
    });
  });
});
