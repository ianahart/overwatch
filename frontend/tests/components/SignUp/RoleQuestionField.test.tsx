import { screen, render } from '@testing-library/react';
import RoleQuestionField from '../../../src/components/SignUp/RoleQuestionField';
import { AllProviders } from '../../AllProviders';

describe('RoleQuestionField', () => {
  const renderComponent = () => {
    render(<RoleQuestionField />, { wrapper: AllProviders });

    return {
      userText: screen.getByText(/review my code/i),
      reviewerText: screen.getByText(/code of others/i),
    };
  };
  it('should render role answers correctly', () => {
    const { userText, reviewerText } = renderComponent();

    expect(userText).toBeInTheDocument();
    expect(reviewerText).toBeInTheDocument();
  });
});
