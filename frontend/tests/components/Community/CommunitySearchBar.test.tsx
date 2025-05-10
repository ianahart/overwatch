import { screen, render, waitFor } from '@testing-library/react';
import CommunitySearchBar from '../../../src/components/Community/CommunitySearchBar';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';
import { db } from '../../mocks/db';

describe('CommunitySearchBar', () => {
  const renderComponent = () => {
    const btnText = 'Search';

    render(<CommunitySearchBar btnText={btnText} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      searchButton: screen.getByRole('button', { name: /search/i }),
      searchInput: screen.getByPlaceholderText(/search a topic/i),
    };
  };

  beforeEach(() => {
    db.topic.deleteMany({ where: { title: { equals: 'Test Topic' } } });
  });

  it('should render the search input and submit button', () => {
    const { searchButton, searchInput } = renderComponent();

    expect(searchInput).toBeInTheDocument();
    expect(searchButton).toBeInTheDocument();
  });

  it('should update the search query on input change', async () => {
    const { searchInput, user } = renderComponent();

    await user.type(searchInput, 'test');
    expect(searchInput).toHaveValue('test');
  });

  it('should fetch topics and show search result when query is valid', async () => {
    const { searchInput, searchButton, user } = renderComponent();

    await user.type(searchInput, 'test');
    await user.click(searchButton);

    expect(await screen.findByText('Test Topic')).toBeInTheDocument();
  });

  it('should show no results when the query does not match the input', async () => {
    const { searchInput, searchButton, user } = renderComponent();

    await user.type(searchInput, 'not a match');
    await user.click(searchButton);

    expect(screen.queryByText('Test Topic')).not.toBeInTheDocument();
  });

  it('should close the search results when clicking away', async () => {
    const { searchInput, searchButton, user } = renderComponent();

    await user.type(searchInput, 'test');
    await user.click(searchButton);

    await waitFor(() => {
      expect(screen.getByText('Test Topic')).toBeInTheDocument();
    });

    const clickAwayDiv = screen.getByText('Test Topic').closest('div');
    if (clickAwayDiv) {
      await user.click(document.body);
    }

    await waitFor(() => {
      expect(screen.queryByText('Test Topic')).not.toBeInTheDocument();
    });
  });

  it('should not show the search results if query is empty', async () => {
    const { searchInput, searchButton, user } = renderComponent();

    await user.clear(searchInput);
    await user.click(searchButton);

    await waitFor(() => {
      expect(screen.queryByText('Test Topic')).not.toBeInTheDocument();
    });
  });
});
