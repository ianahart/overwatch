import { screen, render } from '@testing-library/react';
import CurrentWorkSpaces from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CurrentWorkSpaces';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CurrentWorkSpaceDropdown', () => ({
  __esModule: true,
  default: vi.fn(({ onClickAway, handleSetOpen }) => (
    <div data-testid="mock-dropdown">
      <button onClick={onClickAway}>ClickAway</button>
      <button onClick={() => handleSetOpen(false)}>SetOpenFalse</button>
    </div>
  )),
}));

describe('CurrentWorkSpaces', () => {
  const getElements = () => {
    return {
      getChevronDown: () => screen.getByTestId('currentworkspace-chevron-down'),
      getChevronUp: () => screen.getByTestId('currentworkspace-chevron-up'),
      getHeading: () => screen.getByRole('heading', { name: /your workspaces/i }),
      getTrigger: () => screen.getByTestId('currentworkspace-trigger'),
    };
  };

  const renderComponent = () => {
    render(<CurrentWorkSpaces />, { wrapper: AllProviders });

    return {
      elements: getElements(),
      user: userEvent.setup(),
    };
  };

  it('should render closed by default with up chevron', () => {
    const { elements } = renderComponent();

    const { getChevronUp, getHeading } = elements;

    expect(getHeading()).toBeInTheDocument();
    expect(screen.queryByTestId('mock-dropdown')).not.toBeInTheDocument();
    expect(getChevronUp()).toBeInTheDocument();
  });

  it('should toggle dropdown open/closed when clicking header', async () => {
    const { user, elements } = renderComponent();

    const { getTrigger } = elements;

    await user.click(getTrigger());
    expect(await screen.findByTestId('mock-dropdown')).toBeInTheDocument();

    await user.click(getTrigger());
    expect(screen.queryByTestId('mock-dropdown')).not.toBeInTheDocument();
  });

  it('should close when "onClickAway" is called from dropdown', async () => {
    const { user, elements } = renderComponent();

    const { getTrigger } = elements;

    await user.click(getTrigger());
    expect(screen.getByTestId('mock-dropdown')).toBeInTheDocument();

    await user.click(screen.getByText('ClickAway'));
    expect(screen.queryByTestId('mock-dropdown')).not.toBeInTheDocument();
  });

  it('should close when handleSetOpen(false) is called from dropdown', async () => {
    const { user, elements } = renderComponent();

    const { getTrigger } = elements;
    await user.click(getTrigger());
    expect(screen.getByTestId('mock-dropdown')).toBeInTheDocument();

    await user.click(screen.getByText('SetOpenFalse'));

    expect(screen.queryByTestId('mock-dropdown')).not.toBeInTheDocument();
  });
});
