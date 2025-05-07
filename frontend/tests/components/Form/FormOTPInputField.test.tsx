import { fireEvent, render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import FormOTPInputField, { IFormOPTInputFieldProps } from '../../../src/components/Form/FormOTPInputField';

describe('FormOTPInputField', () => {
  const mockSetPassCode = vi.fn();
  const defaultProps: IFormOPTInputFieldProps = {
    numOfInputs: 4,
    passCode: ['', '', '', ''],
    setPassCode: mockSetPassCode,
  };

  const renderComponent = (props = {}) => {
    mockSetPassCode.mockClear();
    render(<FormOTPInputField {...defaultProps} {...props} />);

    return {
      user: userEvent.setup(),
      inputs: screen.getAllByRole('textbox'),
    };
  };

  it('renders correct number of input fields', () => {
    const { inputs } = renderComponent();

    expect(inputs).toHaveLength(4);
  });

  it('focuses next input after typing a digit', async () => {
    const { user, inputs } = renderComponent();

    await user.type(inputs[0], '1');

    expect(mockSetPassCode).toHaveBeenCalledWith(expect.any(Function));
    expect(document.activeElement).toBe(inputs[1]);
  });

  it('removes current digit and focuses previous on backspace', async () => {
    const { user, inputs } = renderComponent({
      passCode: ['1', '', '', ''],
    });

    inputs[1].focus();
    await user.keyboard('[Backspace]');

    expect(mockSetPassCode).toHaveBeenCalledWith(expect.any(Function));
    expect(document.activeElement).toBe(inputs[0]);
  });

  it('pastes OTP and updates all fields', async () => {
    const { inputs } = renderComponent();

    fireEvent.paste(inputs[0], {
      clipboardData: {
        getData: () => '1234',
      },
    });

    expect(mockSetPassCode).toHaveBeenCalledWith(['1', '2', '3', '4']);
    expect(document.activeElement).toBe(inputs[3]);
  });

  it('does not paste if length is incorrect', async () => {
    const { inputs } = renderComponent();
    fireEvent.paste(inputs[0], {
      clipboardData: {
        getData: () => '1',
      },
    });

    expect(mockSetPassCode).not.toHaveBeenCalled();
  });

  it('ignores non-digit input', async () => {
    const { inputs, user } = renderComponent();

    await user.type(inputs[0], 'a');

    expect(mockSetPassCode).not.toHaveBeenCalled();
  });

  it('shows correct value for each input', () => {
    const { inputs } = renderComponent({ passCode: ['1', '2', '', ''] });

    expect(inputs[0]).toHaveValue('1');
    expect(inputs[1]).toHaveValue('2');
    expect(inputs[2]).toHaveValue('');
  });
});
