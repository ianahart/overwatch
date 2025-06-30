import { render, screen } from '@testing-library/react';
import { fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { vi, Mock } from 'vitest';

import BasicInfo from '../../../../src/components/Settings/EditProfile/BasicInfo';
import { getLoggedInUser } from '../../../utils';
import { updateBasicInfoFormField } from '../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('BasicInfo', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser({
      basicInfo: {
        fullName: {
          name: 'fullName',
          value: '',
          error: '',
          type: 'text',
        },
        userName: {
          name: 'userName',
          value: '',
          error: '',
          type: 'text',
        },
        email: {
          name: 'email',
          value: '',
          error: '',
          type: 'email',
        },
        contactNumber: {
          name: 'contactNumber',
          value: '',
          error: '',
          type: 'text',
        },
      },
    });

    const result = render(<BasicInfo />, { wrapper });
    return {
      curUser,
      user: userEvent.setup(),
      ...result,
    };
  };

  it('should render all basic input fields and LocationPhoneInput', () => {
    renderComponent();

    expect(screen.getByLabelText(/full name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/phone/i)).toBeInTheDocument();
  });

  it('should dispatch on full name input change', async () => {
    const { user } = renderComponent();
    const fullNameInput = screen.getByLabelText(/full name/i);

    await user.clear(fullNameInput);
    await user.type(fullNameInput, 'John Doe');

    expect(mockDispatch).toHaveBeenLastCalledWith(
      updateBasicInfoFormField({
        name: 'fullName',
        value: 'e',
        attribute: 'value',
      })
    );
  });

  it('should dispatch on phone input change', async () => {
    const { container } = renderComponent();

    const input = container.querySelector('input[placeholder="Enter phone number"]')!;
    expect(input).toBeInTheDocument();

    // Simulate user entering a complete valid number
    fireEvent.change(input, { target: { value: '+1234567890' } });

    expect(mockDispatch).toHaveBeenLastCalledWith(
      updateBasicInfoFormField({
        name: 'contactNumber',
        value: '+1234567890',
        attribute: 'value',
      })
    );
  });
});
