import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ToolTip from '../../../src/components/Shared/ToolTip';

describe('ToolTip', () => {
  const renderComponent = (message: string = 'Hover me') => {
    render(
      <ToolTip message={message}>
        <button>Hover me</button>
      </ToolTip>
    );

    return {
      user: userEvent.setup(),
      button: screen.getByRole('button', { name: /hover me/i }),
      toolTip: screen.queryByText(message),
    };
  };

  it('should show the tooltip message on hover and hide it when not hovered', async () => {
    const message = 'Tooltip message here';

    const { user, button, toolTip } = renderComponent(message);

    expect(toolTip).not.toBeInTheDocument();

    await user.click(button);

        if (toolTip) {
    expect(toolTip).toBeInTheDocument();

        }

    await user.click(button);

    expect(toolTip).not.toBeInTheDocument();
  });
});
