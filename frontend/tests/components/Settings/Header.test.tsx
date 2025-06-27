import { screen, render } from '@testing-library/react';

import Header from '../../../src/components/Settings/Header';
import { AllProviders } from '../../AllProviders';

describe('Header', () => {
  const getProps = () => {
    return {
      heading: 'Settings',
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<Header {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render the heading passed via props', () => {
    const { props } = renderComponent();

    expect(screen.getByRole('heading', { level: 3, name: props.heading })).toBeInTheDocument();
  });
});
