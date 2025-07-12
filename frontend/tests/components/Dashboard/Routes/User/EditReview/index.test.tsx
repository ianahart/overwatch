import { screen, render, waitFor } from '@testing-library/react';

import EditReview from '../../../../../../src/components/Dashboard/Routes/User/EditReview';
import { getLoggedInUser } from '../../../../../utils';
import userEvent from '@testing-library/user-event';
import { mockNavigate, setMockParams } from '../../../../../setup';

vi.mock('../../../../../../src/util', async () => {
  const actual = await vi.importActual('../../../../../../src/util');
  return {
    ...actual,
    retrieveTokens: () => ({
      token: 'mock-token',
      refreshToken: 'mock-refresh',
    }),
  };
});

describe('EditReview', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    setMockParams({ repositoryId: '1' });
  });

  const getForm = () => {
    return {
      getTextarea: () => screen.getByLabelText(/repository comment/i),
      getSubmitBtn: () => screen.getByRole('button', { name: /update repository comment/i }),
    };
  };

  const renderComponent = () => {
    const { wrapper, curUser } = getLoggedInUser();

    render(<EditReview />, { wrapper });

    return {
      curUser,
      form: getForm(),
      user: userEvent.setup(),
    };
  };

  it('should render the existing comment', async () => {
    renderComponent();

    expect(screen.getByText(/edit repository comment/i)).toBeInTheDocument();
    expect(await screen.findByDisplayValue('existing repository comment')).toBeInTheDocument();
  });

  it('should submit the updated comment and navigates', async () => {
    const { user, curUser, form } = renderComponent();

    const { getTextarea, getSubmitBtn } = form;

    await user.type(getTextarea(), 'updated comment');
    await user.click(getSubmitBtn());

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(`/dashboard/${curUser.slug}/user/reviews`);
    });
  });
});
