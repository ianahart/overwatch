import { screen, render } from '@testing-library/react';
import TableHeader from '../../../../../../src/components/Dashboard/Routes/Admin/Transactions/TableHeader';
import { AllProviders } from '../../../../../AllProviders';

describe('TableHeader', () => {
  const getProps = () => {
    return {
      heading: 'transactions',
    };
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

    const { heading } = props;

    expect(screen.getByText(heading)).toBeInTheDocument();
  });
});
