import { screen, render, waitFor } from '@testing-library/react';

import Testimonials from '../../../src/components/Profile/Testimonials';
import { getLoggedInUser } from '../../utils';

describe('Testimonials', () => {
  const getProps = () => {
    return {
      userId: 1,
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    const props = getProps();

    render(<Testimonials {...props} />, { wrapper });

    return {
      props,
      getHeading: () => screen.getByRole('heading', { level: 3, name: /testimonials/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });

  it('should render the loading message', async () => {
    renderComponent();

    await waitFor(() => {
      expect(screen.getByText(/fetching testimonials/i)).toBeInTheDocument();
    });
  });

  it('should render the testimonials', async () => {
    renderComponent();

    const testimonials = await screen.findAllByTestId('profile-testimonial');

    expect(testimonials.length).toBe(3);
  });
});
