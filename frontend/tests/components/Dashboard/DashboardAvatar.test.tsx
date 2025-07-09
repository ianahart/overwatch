import { screen, render } from '@testing-library/react';

import DashboardAvatar from '../../../src/components/Dashboard/DashboardAvatar';
import { AllProviders } from '../../AllProviders';

describe('DashboardAvatar', () => {
  const getProps = (overrides = {}) => {
    return {
      url: 'https://www.imgur.com/avatar',
      abbreviation: 'JD',
      width: 'w-9',
      height: 'h-9',
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    render(<DashboardAvatar {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render an avatar image when url is provided', () => {
    const { props } = renderComponent();

    const img = screen.getByRole('img', { name: /a profile picture of JD/i });
    expect(img).toBeInTheDocument();
    expect(img).toHaveAttribute('src', props.url);
    expect(img).toHaveClass(props.width);
    expect(img).toHaveClass(props.height);
  });

  it('should render abbreviation fallback when url is empty', () => {
    const { props } = renderComponent({ url: null });

    expect(screen.getByText(props.abbreviation)).toBeInTheDocument();
    const container = screen.getByText(props.abbreviation).parentElement;
    expect(container).toHaveClass(props.height);
    expect(container).toHaveClass(props.width);
    expect(container).toHaveClass('bg-green-400');
  });
});
