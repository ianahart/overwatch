import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { toPlainObject } from 'lodash';

import { getLoggedInUser } from '../../../../../utils';
import ReviewFeedbackRoute from '../../../../../../src/components/Dashboard/Routes/User/Reviews/ReviewFeedbackRoute';
import { mockLocation } from '../../../../../setup';
import { db } from '../../../../../mocks/db';
import { IRepositoryReview } from '../../../../../../src/interfaces';
import { server } from '../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../src/util';

describe('ReviewFeedbackRoute', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const submitReviewFeedbackRequest = () => {
    server.use(
      http.get(`${baseURL}/review-feedbacks/single`, () => {
        return HttpResponse.json(
          {
            message: 'success',
            data: null,
          },
          { status: 200 }
        );
      })
    );
  };

  const renderComponent = (status: 'INCOMPLETE' | 'COMPLETED') => {
    const repositoryReview: IRepositoryReview = {
      ...toPlainObject(db.repository.create({ status })),
      ownerId: 2,
      reviewerId: 1,
    };

    mockLocation.mockReturnValue({
      state: { data: repositoryReview },
    });

    const { curUser, wrapper } = getLoggedInUser();
    render(<ReviewFeedbackRoute />, { wrapper });

    return {
      repositoryReview,
      curUser,
      user: userEvent.setup(),
    };
  };

  it('should render review feedback and ratings when not already reviewed', async () => {
    submitReviewFeedbackRequest();

    const { repositoryReview } = renderComponent('INCOMPLETE');

    const { firstName, lastName, repoName } = repositoryReview;

    expect(
      await screen.findByRole('heading', {
        name: `Feedback from ${firstName} ${lastName} on ${repoName}`,
      })
    ).toBeInTheDocument();

    expect(
      screen.getByText((text) => text.includes('Please take a few moments and give your feedback'))
    ).toBeInTheDocument();

    expect(screen.getByRole('button', { name: /Submit Feedback/i })).toBeInTheDocument();
  });

  it('should show "already reviewed" message if feedback exists', async () => {
    renderComponent('COMPLETED');

    expect(await screen.findByText(/This is how you reviewed your feedback/i)).toBeInTheDocument();

    expect(screen.queryByRole('button', { name: /Submit Feedback/i })).not.toBeInTheDocument();
  });

  it('should display error when submitting empty feedback', async () => {
    submitReviewFeedbackRequest();
    const { user } = renderComponent('INCOMPLETE');
    const button = await screen.findByRole('button', { name: /Submit Feedback/i });
    await user.click(button);

    expect(await screen.findByText(/Please make sure to fill out all ratings/i)).toBeInTheDocument();
  });
});
