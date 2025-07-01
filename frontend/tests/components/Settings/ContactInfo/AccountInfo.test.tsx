import { render, screen } from '@testing-library/react';

import AccountInfo from '../../../../src/components/Settings/ContactInfo/AccountInfo';
import { AllProviders } from '../../../AllProviders';

describe('AccountInfo', () => {
  const getProps = () => {
    return {
      userId: 1,
      email: 'alice@example.com',
      firstName: 'alice',
      lastName: 'smith',
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<AccountInfo {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render all props', () => {
    const { props } = renderComponent();

    const { userId, email, firstName, lastName } = props;

    const maskedEmail = email
      .split('')
      .map((char, i) => {
        return i < firstName.length && i > 0 ? '*' : char;
      })
      .join('');

    expect(screen.getByText(firstName)).toBeInTheDocument();
    expect(screen.getByText(lastName)).toBeInTheDocument();
    expect(screen.getByText(userId.toString())).toBeInTheDocument();
    expect(screen.getByText(maskedEmail)).toBeInTheDocument();
  });
});
