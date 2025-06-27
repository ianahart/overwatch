import { screen, render, waitFor } from '@testing-library/react';
import { HttpResponse, http } from 'msw';
import userEvent from '@testing-library/user-event';

import Testimonials from '../../../../src/components/Settings/Testimonial/Testimonials';
import { getLoggedInUser } from '../../../utils';
import { server } from '../../../mocks/server';
import { baseURL } from '../../../../src/util';
import { IDeleteTestimonialResponse } from '../../../../src/interfaces';

describe('Testimonials', () => {
  beforeEach(() => {});

  const queryTestimonials = () => {
    return {
      getTestimonials: () => screen.findAllByTestId('setting-testimonial-item'),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    render(<Testimonials />, { wrapper });

    const { getTestimonials } = queryTestimonials();

    return {
      user: userEvent.setup(),
      curUser,
      getTestimonials,
    };
  };

  it('should render testimonials after fetching', async () => {
    const { getTestimonials } = renderComponent();

    const testimonials = await getTestimonials();

    expect(testimonials.length).toBe(2);
  });

  it('should render correct pagination info', async () => {
    renderComponent();

    expect(await screen.findByText('1 of 3')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /next/i })).toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /prev/i })).not.toBeInTheDocument();
  });

  it('should paginate to the next page', async () => {
    const { user } = renderComponent();

    const nextButton = await screen.findByRole('button', { name: /next/i });

    await user.click(nextButton);

    expect(await screen.findByText('2 of 3')).toBeInTheDocument();
  });

  it('should call delete handler when delete button is clicked', async () => {
    const deleteSpy = vi.fn();

    server.use(
      http.delete(`${baseURL}/testimonials/:id`, ({ params }) => {
        deleteSpy(params.id);
        return HttpResponse.json<IDeleteTestimonialResponse>(
          {
            message: 'success',
          },
          { status: 200 }
        );
      })
    );

    const { user, getTestimonials } = renderComponent();

    await getTestimonials();

    user.click(screen.getAllByRole('button', { name: /delete/i })[0]);

    await waitFor(() => {
      expect(deleteSpy).toHaveBeenCalledTimes(1);
    });
  });
});
