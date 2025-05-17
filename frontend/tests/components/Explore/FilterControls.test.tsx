import { screen, render } from '@testing-library/react';

import FilterControls, { IFilterControlsProps } from '../../../src/components/Explore/FilterControls';
import userEvent from '@testing-library/user-event';
import { AllProviders } from '../../AllProviders';

describe('FilterControls', async () => {
  const getFilters = () => {
    return [
      {
        id: 1,
        name: 'Most Recent',
        value: 'most-recent',
        desc: 'Browse Reviewers that have just signed up and our new to the platform.',
      },
      { id: 2, name: 'US Only', value: 'domestic', desc: 'Find Reviewers who are local to the US who speak English.' },
      { id: 3, name: 'Saved', value: 'saved', desc: 'Filter through Reviewers who you have saved to your favorites.' },
      {
        id: 4,
        name: 'Most Relevant',
        value: 'most-relevant',
        desc: 'Sort through the Reviewers that match your programming languages.',
      },
    ];
  };

  const renderComponent = (overrides?: Partial<IFilterControlsProps>) => {
    const handleSetFilter = vi.fn();
    const fetchReviewers = vi.fn(() => Promise.resolve());
    const filter = { value: 'most-recent', desc: '' };

    const props: IFilterControlsProps = {
      handleSetFilter,
      fetchReviewers,
      filter,
      ...overrides,
    };

    render(<FilterControls {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      handleSetFilter,
      fetchReviewers,
      filter,
    };
  };

  it('should render all filter items with correct names', () => {
    renderComponent();
    const filters = getFilters();

    filters.forEach(({ name }) => expect(screen.getByText(name)).toBeInTheDocument());
  });

  it('should apply active-link class to currently selected filter', async () => {
    const { user } = renderComponent();
    const filters = getFilters();
    const [firstFilter] = filters.map(({ name }) => screen.getByText(name));

    await user.click(firstFilter);

    expect(firstFilter).toHaveClass('active-link');
  });

  it('should not apply active-link class to unselected filters', () => {
    renderComponent({ filter: { value: 'most-recent', desc: '' } });

    const inactiveFilter = screen.getByText(/us only/i);

    expect(inactiveFilter).not.toHaveClass('active-link');
  });

  it('should call handleSetFilter and fetchReviewers when a filter is clicked', async () => {
    const { user, handleSetFilter, fetchReviewers } = renderComponent();

    const [targetFilter] = getFilters();

    await user.click(screen.getByText(targetFilter.name));

    expect(handleSetFilter).toHaveBeenCalledWith(targetFilter.value, targetFilter.desc);
    expect(fetchReviewers).toHaveBeenCalledWith(false, targetFilter.value);
  });
});
