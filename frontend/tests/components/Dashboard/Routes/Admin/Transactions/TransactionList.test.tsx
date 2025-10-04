import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { getLoggedInUser } from '../../../../../utils';
import TransactionList from '../../../../../../src/components/Dashboard/Routes/Admin/Transactions/TransactionList';

beforeAll(() => {
  globalThis.URL.createObjectURL = () => 'blob:http://localhost/fake-url';
  globalThis.URL.revokeObjectURL = () => {};

  HTMLAnchorElement.prototype.click = () => {};
});

describe('TransactionList exports', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();
    render(<TransactionList />, { wrapper });
    return {
      user: userEvent.setup(),
    };
  };

  it('should render transactions from API and total revenue', async () => {
    renderComponent();
    const cells = await screen.findAllByTestId('transaction-cell');
    expect(cells.length).toBeGreaterThan(0);
  });

  it('should trigger a CSV export successfully', async () => {
    const { user } = renderComponent();
    const csvBtn = await screen.findByRole('button', { name: /generate csv/i });

    await user.click(csvBtn);

    await waitFor(() => {
      expect(screen.queryByText(/failed to download/i)).not.toBeInTheDocument();
    });
  });

  it('should trigger a PDF export successfully', async () => {
    const { user } = renderComponent();
    const pdfBtn = await screen.findByRole('button', { name: /generate pdf/i });

    await user.click(pdfBtn);

    await waitFor(() => {
      expect(screen.queryByText(/failed to download/i)).not.toBeInTheDocument();
    });
  });
});
