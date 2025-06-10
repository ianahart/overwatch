import { screen, render, waitFor } from '@testing-library/react';

import TeamCommentList from '../../../../../src/components/Teams/Post/Comment/TeamCommentList';
import { IPaginationState, ITeamComment } from '../../../../../src/interfaces';
import { createTeamComments } from '../../../../mocks/dbActions';
import { AllProviders } from '../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('TeamCommentList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const teamComments: ITeamComment[] = createTeamComments(5);
    const pag: IPaginationState = { page: -1, pageSize: 2, totalElements: 5, totalPages: 3, direction: 'next' };

    return {
      paginateTeamComments: vi.fn(),
      teamComments,
      pag,
      updateTeamComment: vi.fn(),
      handleResetComments: vi.fn(),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<TeamCommentList {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      getTeamCommentListItems: () => screen.findAllByTestId('team-comment-list-item'),
      getButton: () => screen.getByRole('button', { name: /read more/i }),
    };
  };

  it('should render team comments correctly', async () => {
    const { getTeamCommentListItems } = renderComponent();

    const teamCommentListItems = await getTeamCommentListItems();

    expect(teamCommentListItems.length).toBe(5);
  });

  it('should render the "Read more" button and paginate more team comments', async () => {
    const { user, getButton, getTeamCommentListItems, props } = renderComponent();

    await user.click(getButton());

    const teamCommentListItems = await getTeamCommentListItems();

    expect(teamCommentListItems.length).toBe(5);

    await waitFor(() => {
      expect(props.paginateTeamComments).toHaveBeenCalledWith('next', false);
    });
  });
});
