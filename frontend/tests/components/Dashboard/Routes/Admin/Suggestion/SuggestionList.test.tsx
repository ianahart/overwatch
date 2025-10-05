import { screen, render, waitFor } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../utils';
import SuggestionList from '../../../../../../src/components/Dashboard/Routes/Admin/Suggestion/SuggestionList';
import userEvent from '@testing-library/user-event';

describe('SuggestionList', () => {
  const getElements = () => {
    return {
      getSelect: () => screen.getByRole('combobox'),
      getPrevBtn: () => screen.findByRole('button', { name: /prev/i }),
      getNextBtn: () => screen.findByRole('button', { name: /next/i }),
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<SuggestionList />, { wrapper });

    return {
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render the component with initial pending query', async () => {
    const { elements } = renderComponent();

    const { getSelect } = elements;

    expect(getSelect()).toHaveValue('PENDING');

    await waitFor(() => {
      expect(screen.getByRole('table')).toBeInTheDocument();
    });
  });

  it('should fetch and render suggestions from the API', async () => {
    renderComponent();

    const suggestions = await screen.findAllByTestId(/^suggestion-row-/);
    expect(suggestions.length).toBeGreaterThan(0);
  });

  it('should change query when a new option is selected and refetches', async () => {
    const { user, elements } = renderComponent();
    const { getSelect } = elements;

    await user.selectOptions(getSelect(), 'IMPLEMENTED');

    expect(getSelect()).toHaveValue('IMPLEMENTED');

    await waitFor(() => {
      expect(screen.getByRole('table')).toBeInTheDocument();
    });
  });

  it('should display pagination controls correctly', async () => {
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText('1')).toBeInTheDocument();
      expect(screen.queryByText(/prev/i)).not.toBeInTheDocument();
    });
  });

  it('should call "paginateSuggestions" when clicking next or prev buttons', async () => {
    const { user, elements } = renderComponent();
    const { getNextBtn } = elements;

    await user.click(await getNextBtn());

    expect(await screen.findByText('2')).toBeInTheDocument();
    expect(await screen.findByRole('button', { name: /prev/i })).toBeInTheDocument();
  });
});
