import { screen, render } from '@testing-library/react';

import CommunityActiveTab from '../../../src/components/Community/CommunityActiveTab';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('CommunityActiveTab', () => {
  const renderComponent = (overrides = {}) => {
    const props = {
      tab: 'topics',
      activeTab: 'userTopics',
      btnText: 'Topics',
      handleSetActiveTab: vi.fn(),
      ...overrides,
    };
    render(<CommunityActiveTab {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
      button: screen.getByText(props.btnText),
    };
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render with the correct text', () => {
    const { button } = renderComponent();

    expect(button).toBeInTheDocument();
  });

  it('should apply the active styles when the tab is active', () => {
    const { button } = renderComponent({ activeTab: 'topics' });

    expect(button).toHaveClass('font-bold text-green-400');
  });

  it('should apply non active styles when the tab is inactive', () => {
    const { button } = renderComponent();

    expect(button).toHaveClass('font-normal text-gray-400');
  });

  it('should call handleSetActiveTab with the correct argument when clicked', async () => {
    const { props, button, user } = renderComponent();

    await user.click(button);
    expect(props.handleSetActiveTab).toHaveBeenCalledOnce();
    expect(props.handleSetActiveTab).toHaveBeenCalledWith('topics');
  });
});
