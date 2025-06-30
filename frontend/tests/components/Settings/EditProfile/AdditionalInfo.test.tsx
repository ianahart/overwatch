import { screen, render, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import userEvent from '@testing-library/user-event';
import { Mock } from 'vitest';

import { addTimeSlot, removeTimeSlot, updateMoreInfo } from '../../../../src/state/store';
import { getLoggedInUser } from '../../../utils';
import AdditionalInfo from '../../../../src/components/Settings/EditProfile/AdditionalInfo';
import { toPlainObject } from 'lodash';
import { db } from '../../../mocks/db';
import { IDayAvailability, ITimeSlot } from '../../../../src/interfaces';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('AdditionalInfo', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = () => {
    const slot: ITimeSlot = { ...toPlainObject(db.slot.create()), startTime: '10:00 AM', endTime: '12:00 PM' };
    const availabilityDay: IDayAvailability = {
      ...toPlainObject(db.availability.create()),
      slots: [slot],
    };

    const { wrapper } = getLoggedInUser(
      {},
      {
        additionalInfo: {
          availability: [availabilityDay],
        },
      }
    );

    render(<AdditionalInfo />, { wrapper });

    return {
      user: userEvent.setup(),
      availability: [availabilityDay],
    };
  };

  it('should render availability slots with remove button', () => {
    const { availability } = renderComponent();

    const [day] = availability;

    expect(screen.getByText(day.day)).toBeInTheDocument();
    expect(screen.getByText(`${day.slots[0].startTime} - ${day.slots[0].endTime}`)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /remove/i })).toBeInTheDocument();
  });

  it('should dispatch "removeTimeSlot" when "Remove" is clicked', async () => {
    const { user, availability } = renderComponent();

    await user.click(screen.getByRole('button', { name: /remove/i }));

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        removeTimeSlot({ day: availability[0].day, id: availability[0].slots[0].id })
      );
    });
  });

  it('should dispatch "addTimeSlot" when valid times are entered and "Add Slot" is clicked', async () => {
    const { user } = renderComponent();

    const startInput = screen.getByLabelText(/start time/i);
    const endInput = screen.getByLabelText(/end time/i);

    await user.type(startInput, '09:00');
    await user.type(endInput, '11:00');

    const addBtn = screen.getByRole('button', { name: /add slot/i });

    await user.click(addBtn);

    expect(mockDispatch).toHaveBeenCalledWith(expect.objectContaining({ type: addTimeSlot.type }));
  });
  it('should dispatch updateMoreInfo when textarea changes', async () => {
    const { user } = renderComponent();

    const textarea = screen.getByTestId('edit-profile-more-info-textarea');
    await user.type(textarea, 'Some info');

    expect(mockDispatch).toHaveBeenCalledWith(updateMoreInfo('Some info'));
  });
});
