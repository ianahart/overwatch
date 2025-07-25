import { screen, render } from '@testing-library/react';

import ChartContainer from '../../../../../../src/components/Dashboard/Routes/Reviewer/Statistic/ChartContainer';
import { AllProviders } from '../../../../../AllProviders';

describe('ChartContainer', () => {
  const getProps = () => {
    return {
      children: <span data-testid="children">children</span>,
      title: 'Average Review Time',
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<ChartContainer {...props} />, { wrapper: AllProviders });

    return { props };
  };

  it('should render both title and children props passed down', () => {
    const { props } = renderComponent();

    const { title } = props;

    expect(screen.getByRole('heading', { name: title })).toBeInTheDocument();
    expect(screen.getByTestId('children')).toBeInTheDocument();
  });
});
