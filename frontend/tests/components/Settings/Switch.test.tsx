import { screen, render } from '@testing-library/react';

import Switch, { ISwitchProps } from '../../../src/components/Settings/Switch';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('Switch', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides: Partial<ISwitchProps> = {}) => {
    return {
      switchToggled: false,
      handleSwitchToggled: vi.fn(),
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    render(<Switch {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should be checked when switchToggled is true', () => {
    renderComponent({ switchToggled: true });

    const checkbox = screen.getByRole('checkbox') as HTMLInputElement;

    expect(checkbox.checked).toBe(true);
  });

  it('should be unchecked when switchToggled is false', () => {
    renderComponent({ switchToggled: false });

    const checkbox = screen.getByRole('checkbox') as HTMLInputElement;

    expect(checkbox.checked).toBe(false);
  });

  it('should call "handleSwitchToggled" with true when toggled on', async () => {
    const { user, props } = renderComponent({ switchToggled: false });

    const checkbox = screen.getByRole('checkbox');

    await user.click(checkbox);

    expect(props.handleSwitchToggled).toHaveBeenCalledWith(true);
  });

  it('should call "handleSwitchToggled" with false when toggled off', async () => {
    const { user, props } = renderComponent({ switchToggled: true });

    const checkbox = screen.getByRole('checkbox');

    await user.click(checkbox);

    expect(props.handleSwitchToggled).toHaveBeenCalledWith(false);
  });
});
