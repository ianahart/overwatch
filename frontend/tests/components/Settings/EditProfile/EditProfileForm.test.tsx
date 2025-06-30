import { screen, render, waitFor } from '@testing-library/react';
import { toast } from 'react-toastify';

import EditProfileForm from '../../../../src/components/Settings/EditProfile/EditProfileForm';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { getLoggedInUser } from '../../../utils';
import {
  updateBasicInfo,
  updateProfileSetup,
  updateSkills,
  updateWorkExp,
  updatePackages,
  updateAdditionalInfo,
} from '../../../../src/state/store';
import userEvent from '@testing-library/user-event';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

vi.mock('react-toastify', async () => {
  const actual = await vi.importActual('react-toastify');
  return {
    ...actual,
    toast: {
      success: vi.fn(),
    },
    ToastContainer: () => <div data-testid="mock-toast-container" />,
  };
});

describe('EditProfileForm', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser(
      { token: 'token', role: 'REVIEWER', profileId: 1, slug: 'reviewer-slug' },
      {
        additionalInfo: { availability: [], moreInfo: '' },
        profileSetup: {
          bio: { value: '', name: '', error: '', type: '' },
          tagLine: { value: '', name: '', error: '', type: '' },
          avatar: { name: 'avatar', value: '', error: '', type: 'file' },
        },
        basicInfo: {
          fullName: { value: 'John', name: 'fullName', error: '', type: '' },
          userName: { value: 'doej', name: 'userName', error: '', type: '' },
          email: { value: 'john@example.com', name: 'email', error: '', type: '' },
          contactNumber: { value: '1234567890', name: 'contactNumber', error: '', type: '' },
        },
        skills: { languages: [], programmingLanguages: [], qualifications: [] },
        workExp: { workExps: [] },
        package: {
          basic: { price: '', description: '', items: [] },
          standard: { price: '', description: '', items: [] },
          pro: { price: '', description: '', items: [] },
        },
      }
    );
    render(<EditProfileForm />, { wrapper });

    return {
      user: userEvent.setup(),
    };
  };
  it('should render the first step for reviewers', () => {
    renderComponent();
    expect(screen.getByText(/basic info/i)).toBeInTheDocument();
  });

  it('should navigate through form steps when clicking Next/Back', async () => {
    const { user } = renderComponent();

    const next = screen.getByRole('button', { name: /next/i });
    await user.click(next);
    expect(screen.getByText(/profile setup/i)).toBeInTheDocument();

    const back = screen.getByRole('button', { name: /back/i });
    await user.click(back);
    expect(screen.getByText(/basic info/i)).toBeInTheDocument();
  });

  it('should call dispatch when populated data is fetched', async () => {
    renderComponent();
    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(updateBasicInfo(expect.any(Object)));
      expect(mockDispatch).toHaveBeenCalledWith(updateProfileSetup(expect.any(Object)));
      expect(mockDispatch).toHaveBeenCalledWith(updateSkills(expect.any(Object)));
      expect(mockDispatch).toHaveBeenCalledWith(updateWorkExp(expect.any(Object)));
      expect(mockDispatch).toHaveBeenCalledWith(updatePackages(expect.any(Object)));
      expect(mockDispatch).toHaveBeenCalledWith(updateAdditionalInfo(expect.any(Object)));
    });
  });

  it('should call updateProfile and show toast on submit', async () => {
    const { user } = renderComponent();

    for (let i = 0; i < 5; i++) {
      await user.click(screen.getByRole('button', { name: /next/i }));
    }

    const updateButton = screen.getByRole('button', { name: /update/i });
    await user.click(updateButton);

    await waitFor(() => {
      expect(toast.success).toHaveBeenCalledWith(
        'Your profile was successfully updated!',
        expect.objectContaining({
          position: 'bottom-center',
          onClose: expect.any(Function),
        })
      );
    });
  });
});
