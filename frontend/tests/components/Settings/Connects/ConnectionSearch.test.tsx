import { screen, render } from '@testing-library/react';

import ConnectionSearch from '../../../../src/components/Settings/Connects/ConnectionSearch';
import { AllProviders } from '../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('ConnectionSearch', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      changeConnection: vi.fn(),
      connectionId: 1,
    };
  };

  const getForm = () => {
    return {
      getSearchInput: () => screen.getByPlaceholderText(/search/i),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<ConnectionSearch {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      form: getForm(),
    };
  };

  it('should render search input', () => {
    const { form } = renderComponent();

    expect(form.getSearchInput()).toBeInTheDocument();
  });

  it('calls API and renders results when typing in input', async () => {
    const { user, form } = renderComponent();

    await user.type(form.getSearchInput(), 'Alice');

    const searches = await screen.findAllByTestId('connection-search-item');

    expect(searches.length).toBe(2);
  });

  it('sould render "See more button when more pages are available"', async () => {
    const { user, form } = renderComponent();

    await user.type(form.getSearchInput(), 'test');

    expect(await screen.findByRole('button', { name: /see more/i })).toBeInTheDocument();
  });
});
