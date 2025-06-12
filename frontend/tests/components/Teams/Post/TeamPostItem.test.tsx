import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import TeamPostItem from '../../../../src/components/Teams/Post/TeamPostItem';
import { db } from '../../../mocks/db';
import { ITeamPost } from '../../../../src/interfaces';
import { getLoggedInUser } from '../../../utils';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

describe('TeamPostItem', () => {
  const getPostElements = (teamPost: ITeamPost) => {
    return {
      getAvatar: () => screen.getByRole('img'),
      getFullName: () => screen.getByRole('heading', { level: 3, name: teamPost.fullName }),
      getDate: () => screen.getByText(dayjs(teamPost.createdAt).format('MM/DD/YYYY')),
      getSnippet: () => screen.getByRole('code'),
    };
  };

  const getActions = () => {
    return {
      getMenuTrigger: () => screen.getByTestId('team-post-menu-trigger'),
      getCreateCommentButton: () => screen.getByRole('button', { name: /leave a comment/i }),
      getLoadCommentsButton: () => screen.getByRole('button', { name: /read comments/i }),
    };
  };

  const getProps = (overrides = {}) => {
    const teamPost: ITeamPost = {
      ...toPlainObject(db.teamPost.create({ code: 'console.log("hello")', hasComments: true })),
      ...overrides,
    };

    return {
      teamPost,
    };
  };

  const renderComponent = (overrides = {}, propsOverrides = {}) => {
    const { curUser, wrapper } = getLoggedInUser(overrides);

    const props = getProps(propsOverrides);
    const postElements = getPostElements(props.teamPost);
    const actions = getActions();

    render(<TeamPostItem {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      postElements,
      curUser,
      actions,
    };
  };

  it('should render avatar, fullName, date, and snippet', () => {
    const { postElements, props } = renderComponent();

    expect(postElements.getDate()).toBeInTheDocument();
    expect(postElements.getAvatar()).toBeInTheDocument();
    expect(postElements.getFullName()).toBeInTheDocument();
    expect(postElements.getSnippet()).toHaveTextContent(props.teamPost.code);
  });

  it('should show remove button in menu if user is author', async () => {
    const { user, actions } = renderComponent({ id: 1 }, { userId: 1 });
    await user.click(actions.getMenuTrigger());

    expect(await screen.findByText(/remove post/i)).toBeInTheDocument();
  });

  it('should not show remove button in menu if user is not the author', async () => {
    const { user, actions } = renderComponent({ id: 1 }, { userId: 2 });

    await user.click(actions.getMenuTrigger());

    await waitFor(() => {
      expect(screen.queryByText(/remove post/i)).not.toBeInTheDocument();
    });
  });

  it('should open the comment modal when "Leave a comment" is clicked', async () => {
    const { user, actions } = renderComponent();

    const { getCreateCommentButton } = actions;

    await user.click(getCreateCommentButton());

    expect(await screen.findByTestId('team-modal')).toBeInTheDocument();
  });

  it('should load comments via "Read comments..." button', async () => {
    const { user, actions } = renderComponent();

    const { getLoadCommentsButton } = actions;

    await user.click(getLoadCommentsButton());

    const teamComments = await screen.findAllByTestId('team-comment-list-item');

    expect(teamComments.length).toBeGreaterThan(0);
  });
});
