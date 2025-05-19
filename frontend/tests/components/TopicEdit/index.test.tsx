import { screen, render } from '@testing-library/react';

import TopicEdit from '../../../src/components/TopicEdit';
import { getLoggedInUser } from '../../utils';
import userEvent from '@testing-library/user-event';
import { mockNavigate, setMockParams } from '../../setup';
import { db } from '../../mocks/db';

describe('TopicEdit', () => {
  beforeEach(() => {
    db.topic.delete({ where: { id: { equals: 1 } } });
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    render(<TopicEdit />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
      getTitle: () => screen.findByRole('heading', { name: 'title' }),
      getDescription: () => screen.findByTestId('topic-edit-description'),
      getSubmitButton: () => screen.findByRole('button', { name: /update/i }),
    };
  };
  it('should render the topic title and description', async () => {
    setMockParams({ topicId: '1' });

    const { getTitle, getDescription } = renderComponent();

    expect(await getTitle()).toBeInTheDocument();
    expect(await getDescription()).toBeInTheDocument();
  });

  it('should allow editing the description and submitting', async () => {
    setMockParams({ topicId: '1' });

    const { user, getDescription, getSubmitButton } = renderComponent();

    await user.clear(await getDescription());
    await user.type(await getDescription(), 'updated description');

    await user.click(await getSubmitButton());

    expect(mockNavigate).toHaveBeenCalledWith('/community');
  });

  it('should display an error if trying to the delete the only tag', async () => {
    const { user } = renderComponent();

    const deleteButtons = await screen.findAllByTestId('delete-tag-icon');

    for (let i = 0; i < deleteButtons.length; i++) {
      await user.click(deleteButtons[i]);
    }

    expect(await screen.findByText('You must have at least one tag')).toBeInTheDocument;
  });

  it('should display an error if description exceeds limit', async () => {
    const { user, getDescription, getSubmitButton } = renderComponent();

    await user.type(await getDescription(), 'a'.repeat(252));

    await user.click(await getSubmitButton());

    expect(await screen.findByText('Description must be between 1 and 250')).toBeInTheDocument();
  });

});
