import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import LocationPhoneInput from '../../../../src/components/Settings/ContactInfo/LocationPhoneInput';

vi.mock('react-phone-number-input', () => ({
  __esModule: true,
  default: ({ onChange, value }: any) => (
    <input
      aria-label="Phone Number"
      value={value}
      onChange={(e) => onChange(e.target.value)}
      placeholder="Enter phone number"
    />
  ),
}));

describe('LocationPhoneInput', () => {
  it('calls updateField when phone number changes', async () => {
    const mockUpdateField = vi.fn();
    const user = userEvent.setup();

    render(<LocationPhoneInput name="phoneNumber" value="" updateField={mockUpdateField} />);

    const input = screen.getByLabelText(/phone number/i);
    await user.type(input, '1234567890');

    expect(mockUpdateField).toHaveBeenCalledWith('phoneNumber', expect.any(String));
    expect(mockUpdateField.mock.calls).toContainEqual(['phoneNumber', '0']);
  });
});
