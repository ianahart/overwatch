import { screen, render } from '@testing-library/react';
import TableHeader from '../../../../../../src/components/Dashboard/Routes/Admin/Transactions/TableHeader';
import { AllProviders } from '../../../../../AllProviders';

describe('TableHeader', () => {
  const getProps = () => {
    const heading = 'suggestions';

    return { heading };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<TableHeader {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render the table heading', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.heading)).toBeInTheDocument();
  });
});
