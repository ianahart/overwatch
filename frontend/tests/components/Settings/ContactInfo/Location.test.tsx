import { screen, render, waitFor } from '@testing-library/react';
import { HttpResponse, http } from 'msw';
import userEvent from '@testing-library/user-event';

import Location from '../../../../src/components/Settings/ContactInfo/Location';
import { getLoggedInUser } from '../../../utils';
import { server } from '../../../mocks/server';
import { baseURL } from '../../../../src/util';

describe('Location', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getIcons = () => {
    return {
      getLocationOpenIcon: () => screen.getByTestId('location-chevron-left'),
      getLocationCloseIcon: () => screen.getByTestId('location-chevron-down'),
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<Location />, { wrapper });

    return {
      user: userEvent.setup(),
      icons: getIcons(),
    };
  };

  it('should render heading and form', async () => {
    renderComponent();
    expect(await screen.findByRole('heading', { level: 3, name: /location/i })).toBeInTheDocument();
  });

  it('should open and close form when toggled', async () => {
    const { user, icons } = renderComponent();

    await user.click(icons.getLocationOpenIcon());

    expect(await screen.findByTestId('location-form')).toBeInTheDocument();

    await user.click(icons.getLocationCloseIcon());

    await waitFor(() => {
      expect(screen.queryByTestId('location-form')).not.toBeInTheDocument();
    });
  });

  it('should populate form when data is fetched', async () => {
    const { user, icons } = renderComponent();

    await user.click(icons.getLocationOpenIcon());

    expect(await screen.findByDisplayValue(/123 Main St/)).toBeInTheDocument();
    expect(await screen.findByDisplayValue(/Springfield/)).toBeInTheDocument();
  });

  it('should submit form with valid data', async () => {
    const { user, icons } = renderComponent();

    await user.click(icons.getLocationOpenIcon());

    await user.type(screen.getByLabelText(/Address/i), '456 Elm St');
    await user.type(screen.getByLabelText(/City/i), 'Denver');
    await user.type(screen.getByLabelText(/State/i), 'CO');
    await user.type(screen.getByPlaceholderText(/ZIP/i), '80202');
    await user.type(screen.getByLabelText(/Phone/i), '123-456-7890');

    await user.click(screen.getByRole('button', { name: /Update/i }));

    await waitFor(() => {
      expect(screen.queryByTestId('location-form')).not.toBeInTheDocument();
    });
  });

  it('should show validation errors if required fields are empty', async () => {
    server.use(
      http.post(`${baseURL}/users/:userId/locations`, () => {
        return HttpResponse.json(
          {
            message: 'this is an error',
          },
          { status: 400 }
        );
      })
    );

    const { user, icons } = renderComponent();

    await user.click(icons.getLocationOpenIcon());

    await user.click(screen.getByRole('button', { name: /Update/i }));

    expect(await screen.findByText('this is an error')).toBeInTheDocument();
  });
});
