import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import CardStartCustomField from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CustomField/CardStartCustomField';
import { getLoggedInUser } from '../../../../../../utils';

describe('CardStartCustomField', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    return {
      page: 1,
      todoCardId: 1,
      navigatePrevPage: vi.fn(),
      navigateNextPage: vi.fn(),
      handleCloseClickAway: vi.fn(),
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);
    const { wrapper } = getLoggedInUser();

    render(<CardStartCustomField {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
    };
  };

  it('should render list of custom fields when data is returned', async () => {
    renderComponent();

    const customFieldItems = await screen.findAllByTestId('CustomFieldItem');

    expect(customFieldItems.length).toBeGreaterThan(0);
  });

  it('should call navigateNextPage when clicking "New Field"', async () => {
    const { user, props } = renderComponent();

    await user.click(screen.getByTestId('new-field-btn'));

    await waitFor(() => {
      expect(props.navigateNextPage).toHaveBeenCalled();
    });
  });
});
