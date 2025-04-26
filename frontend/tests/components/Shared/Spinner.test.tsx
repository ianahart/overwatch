import { screen, render } from '@testing-library/react';
import Spinner from '../../../src/components/Shared/Spinner';

describe('Spinner', () => {
  const renderComponent = (message: string = 'Loading...') => {
    render(<Spinner message={message} />);

    return {
      spinner: screen.getByLabelText('Loading spinner'),
      paragraph: screen.getByRole('paragraph'),
    };
  };

  it('should render a spinner icon with the provided message', () => {
    const message = 'Loading...';
    const { spinner, paragraph } = renderComponent(message);

    expect(spinner).toBeInTheDocument();
    expect(paragraph).toHaveTextContent(message);
  });
});
