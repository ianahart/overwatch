import { render, screen, waitFor } from '@testing-library/react';
import { server } from '../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../src/util';
import { getLoggedInUser } from '../../../../../utils';
import Statistic from '../../../../../../src/components/Dashboard/Routes/Reviewer/Statistic';

describe('Statistic', () => {
  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<Statistic />, { wrapper });
  };

  it('renders spinner and then all chart titles when data loads', async () => {
    server.use(
      http.get(`${baseURL}/statistics`, () => {
        return HttpResponse.json({
          data: {
            avgRatings: [{ language: 'TypeScript', rating: 4.8 }],
            avgReviewTimes: [{ day: '2024-06-01', reviewsCompleted: 5 }],
            mainLanguages: [{ language: 'JavaScript', count: 12 }],
            reviewTypesCompleted: [{ reviewType: 'Initial', count: 3 }],
            reviewsCompleted: [{ day: '2024-06-01', reviewsCompleted: 2 }],
            statusTypes: [{ status: 'completed', count: 5 }],
            topRequesters: [{ fullName: 'Alice Johnson', count: 7 }],
          },
        });
      })
    );

    renderComponent();

    await waitFor(() => {
      screen.debug();
      expect(screen.getByText(/Review Types Completed/i)).toBeInTheDocument();
      expect(screen.getByText(/Most Reviewed Languages/i)).toBeInTheDocument();
      expect(screen.getByText(/Average Review Times This Month/i)).toBeInTheDocument();
      expect(screen.getByText(/Status of Reviews/i)).toBeInTheDocument();
      expect(screen.getByText(/Reviews Completed This Month/i)).toBeInTheDocument();
      expect(screen.getByText(/Top Requesters/i)).toBeInTheDocument();
    });
  });
});
