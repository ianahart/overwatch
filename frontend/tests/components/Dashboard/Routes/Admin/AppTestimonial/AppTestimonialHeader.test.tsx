import { screen, render } from '@testing-library/react';
import AppTestimonialHeader from '../../../../../../src/components/Dashboard/Routes/Admin/AppTestimonial/AppTestimonialHeader';
import { AllProviders } from '../../../../../AllProviders';

describe('AppTestimonialHeader', () => {
  const renderComponent = () => {
    render(<AppTestimonialHeader />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /testimonials/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
