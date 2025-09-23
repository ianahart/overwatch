import { screen, render } from '@testing-library/react';
import FlaggedCommentTitle from '../../../../../../src/components/Dashboard/Routes/Admin/FlaggedComment/FlaggedCommentTitle';
import { AllProviders } from '../../../../../AllProviders';

describe('FlaggedCommentTitle', () => {
  const renderComponent = () => {
    render(<FlaggedCommentTitle />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { name: /flagged comments/i }),
    };
  };

  it('should render the heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });
});
