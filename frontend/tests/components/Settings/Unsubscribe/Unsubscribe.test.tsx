import { screen, render, waitFor } from '@testing-library/react';
import { HttpResponse, http } from 'msw';

import Unsubscribe from '../../../../src/components/Settings/Unsubscribe/Unsubscribe';
import { mockUserSearchParams } from '../../../setup';
import { server } from '../../../mocks/server';
import { IUnsubscribeSettingsResponse } from '../../../../src/interfaces';
import { baseURL } from '../../../../src/util';
import { AllProviders } from '../../../AllProviders';

describe('Unsubscribe', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockUserSearchParams({ email: 'test@example.com' });
  });

  const renderComponent = () => {
    render(<Unsubscribe />, { wrapper: AllProviders });
  };

  it('should fire unsubscribe query and renders confirmation', async () => {
    let called = false;

    server.use(
      http.get(`${baseURL}/settings/unsubscribe`, ({ request }) => {
        const url = new URL(request.url);

        const email = url.searchParams.get('email') ?? null;

        if (email === 'test@example.com') {
          called = true;
          return HttpResponse.json<IUnsubscribeSettingsResponse>(
            {
              message: 'success',
            },
            { status: 200 }
          );
        }
      })
    );

    renderComponent();

    await waitFor(() => {
      expect(called).toBeTruthy();
      expect(screen.getByText(/you have been successfully unsubscribed/i)).toBeInTheDocument();
    });
  });
});
