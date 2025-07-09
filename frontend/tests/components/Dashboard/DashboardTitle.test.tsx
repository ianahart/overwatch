import { screen, render } from '@testing-library/react';

import DashboardTitle from '../../../src/components/Dashboard/DashboardTitle';
import { AllProviders } from '../../AllProviders';

describe('DashboardTitle', () => {
  const getProps = () => {
    return {
      title: 'OverWatch',
      version: 0.1,
    };
  };

  const getElements = (props: { title: string; version: number }) => {
    return {
      getHeading: () => screen.getByRole('heading', { name: props.title }),
      getVersion: () => screen.getByText(`v ${props.version.toString()}`),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    const elements = getElements(props);

    render(<DashboardTitle {...props} />, { wrapper: AllProviders });

    return {
      elements,
    };
  };

  it('should render the title and version props', () => {
    const { elements } = renderComponent();
    const { getHeading, getVersion } = elements;

    expect(getHeading()).toBeInTheDocument();
    expect(getVersion()).toBeInTheDocument();
  });
});
