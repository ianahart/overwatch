import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import * as toastify from 'react-toastify';
import PhoneNumber from '../../../../src/components/Settings/Security/PhoneNumber';
import { getLoggedInUser } from '../../../utils';
import { server } from '../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../src/util';
import { Mock } from 'vitest';

vi.mock('react-toastify', async () => {
  const actual = await vi.importActual<typeof import('react-toastify')>('react-toastify');
  return {
    ...actual,
    toast: {
      success: vi.fn(),
    },
    ToastContainer: actual.ToastContainer,
  };
});

describe('PhoneNumber', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();
    const user = userEvent.setup();

    render(<PhoneNumber />, { wrapper });

    return {
      user,
      getInput: () => screen.getByLabelText(/your phone number/i),
      getSaveBtn: () => screen.getByRole('button', { name: /save/i }),
      getDeleteBtn: () => screen.queryByRole('button', { name: /delete/i }),
    };
  };

  it('should render input and save button', () => {
    const { getInput, getSaveBtn } = renderComponent();

    expect(getInput()).toBeInTheDocument();
    expect(getSaveBtn()).toBeInTheDocument();
  });

  it('should prefill the phone number if data is fetched', async () => {
    server.use(
      http.get(`${baseURL}/phones`, () =>
        HttpResponse.json({
          data: {
            id: 123,
            createdAt: new Date().toISOString(),
            isVerified: true,
            phoneNumber: '5555555555',
          },
        })
      )
    );

    const { getInput } = renderComponent();

    await waitFor(() => {
      expect(getInput()).toHaveValue('5555555555');
    });
  });

  it('should create phone number successfully and show toast', async () => {
    server.use(
      http.get(`${baseURL}/phones/user/:userId`, () => HttpResponse.json({ data: null })),
      http.post(`${baseURL}/phones`, () => HttpResponse.json({ message: 'success' }))
    );

    const toastSpy = toastify.toast.success as Mock;

    const { getInput, getSaveBtn, user } = renderComponent();

    await waitFor(() => {
      expect(getInput()).toBeInTheDocument();
    });

    await user.clear(getInput());
    await user.type(getInput(), '5554445555');
    await user.click(getSaveBtn());

    await waitFor(() => {
      expect(toastSpy).toHaveBeenCalledWith(
        'Your phone number was successfully added to our system!',
        expect.objectContaining({
          position: 'bottom-center',
          autoClose: 5000,
          theme: 'dark',
        })
      );
    });
  });
  it('should delete phone number and reset state', async () => {
    server.use(
      http.get(`${baseURL}/phones`, () =>
        HttpResponse.json({
          data: {
            id: 99,
            createdAt: new Date().toISOString(),
            isVerified: true,
            phoneNumber: '5554445555',
          },
        })
      ),
      http.delete(`${baseURL}/phones/99`, () => HttpResponse.json({ message: 'Deleted' }))
    );

    const { user, getDeleteBtn, getInput } = renderComponent();

    await waitFor(() => {
      expect(getDeleteBtn()).toBeInTheDocument();
    });

    await user.click(getDeleteBtn()!);

    await waitFor(() => {
      expect(getInput()).toHaveValue('');
      expect(getDeleteBtn()).not.toBeInTheDocument();
    });
  });
});
