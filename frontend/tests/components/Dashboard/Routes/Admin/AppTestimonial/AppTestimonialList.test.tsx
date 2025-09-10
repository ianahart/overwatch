import { screen, render } from '@testing-library/react';
import AppTestimonialList from '../../../../../../src/components/Dashboard/Routes/Admin/AppTestimonial/AppTestimonialList';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';

describe('AppTestimonialList', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<AppTestimonialList />, { wrapper });

    return {
      user: userEvent.setup(),
    };
  };

  it('should display pagination info', async () => {
    renderComponent();

    expect(await screen.findByText(/1 of/i)).toBeInTheDocument();
  });

  it('should navigate to next page when "Next" is clicked', async () => {
    const { user } = renderComponent();

    await user.click(await screen.findByRole('button', { name: /next/i }));

    expect(await screen.findByText(/2 of/i)).toBeInTheDocument();
  });

  it('should render the app testimonials', async () => {
    renderComponent();

    const testimonials = await screen.findAllByTestId('AppTestimonialListItem');
    expect(testimonials).toHaveLength(2);
  });
});
