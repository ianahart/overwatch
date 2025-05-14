import { screen, render, waitFor } from '@testing-library/react';

import CreateCommunity from '../../../src/components/CreateCommunity';
import { getWrapper } from '../../RenderWithProviders';
import { db } from '../../mocks/db';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { mockNavigate } from '../../setup';

describe('CreateCommunity', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });
  const renderComponent = () => {
    const user = db.user.create({});
    const token = db.token.create({});

    const wrapper = getWrapper({ user: { user, token: token.token } } as any);

    render(<CreateCommunity />, { wrapper });

    return {
      user: userEvent.setup(),
      heading: screen.getByRole('heading', { name: /create a community topic/i }),
      getInput: () => screen.getByPlaceholderText(/topic title/i),
      getTextarea: () => screen.getByRole('textbox', { name: /description/i }),
      getAddTagInput: () => screen.getByPlaceholderText(/add tags/i),
      getSubmitButton: () => screen.getByRole('button', { name: /create/i }),
      getAddTagButton: () => screen.getByTestId('add-tag-btn'),
    };
  };

  const addTag = async (user: UserEvent, tag: string, input: HTMLElement, button: HTMLElement) => {
    await user.type(input, tag);
    await user.click(button);
  };

  it('should render the form correctly', () => {
    const { heading, getInput, getTextarea, getAddTagInput, getSubmitButton } = renderComponent();

    expect(heading).toBeInTheDocument();
    expect(getInput()).toBeInTheDocument();
    expect(getTextarea()).toBeInTheDocument();
    expect(getAddTagInput()).toBeInTheDocument();
    expect(getSubmitButton()).toBeInTheDocument();
  });

  it('should add a tag when the "Add tag" button is clicked', async () => {
    const { user, getAddTagInput, getAddTagButton } = renderComponent();
    const tag = 'React';

    await addTag(user, tag, getAddTagInput(), getAddTagButton());

    expect(await screen.findByText('#React')).toBeInTheDocument();
  });

  it('should display an error when trying to add an empty or too long tag', async () => {
    const { user, getAddTagInput, getAddTagButton } = renderComponent();

    const MAX_TAG_LENGTH = 50;
    const tag = 'fail'.repeat(MAX_TAG_LENGTH);
    const error = 'A tag cannot be empty and must be under 50 characters';

    addTag(user, tag, getAddTagInput(), getAddTagButton());

    expect(await screen.findByText(error)).toBeInTheDocument();
  });

  it('should display an error if maximum amount of tags is reached', async () => {
    const { user, getAddTagInput, getAddTagButton } = renderComponent();
    const tagText = ['React', 'JavaScript', 'HTML', 'CSS', 'PHP', 'Web'];
    const error = 'You have already added the max (5) number of tags';

    for (let i = 0; i < tagText.length; i++) {
      await addTag(user, tagText[i], getAddTagInput(), getAddTagButton());
    }

    expect(await screen.findByText(error)).toBeInTheDocument();
  });

  it('should display an error if tag exists already', async () => {
    const { user, getAddTagInput, getAddTagButton } = renderComponent();
    const tag = 'React';
    const error = `You already added the tag ${tag}`;

    await addTag(user, tag, getAddTagInput(), getAddTagButton());
    await addTag(user, tag, getAddTagInput(), getAddTagButton());

    expect(await screen.findByText(error)).toBeInTheDocument();
  });

  it('should remove a tag when the close button is clicked', async () => {
    const { user, getAddTagInput, getAddTagButton } = renderComponent();

    const tag = 'React';

    await addTag(user, tag, getAddTagInput(), getAddTagButton());

    const deleteButton = await screen.findByTestId('tag-item-container');

    await user.click(deleteButton);

    expect(screen.queryByText(`#${tag}`)).not.toBeInTheDocument();
  });

  it('should show an error if there are no tags on submission', async () => {
    const { user, getSubmitButton } = renderComponent();

    await user.click(getSubmitButton());

    expect(await screen.findByText('Please add at least one tag for this topic'));
  });

  it('should show an error if title or description is missing on submission', async () => {
    const { user, getAddTagInput, getAddTagButton, getSubmitButton } = renderComponent();

    const tag = 'React';

    await addTag(user, tag, getAddTagInput(), getAddTagButton());

    await user.click(getSubmitButton());

    expect(await screen.findByText('Please make sure to fill out all fields'));
  });

  it('should submit the form and navigate to /community on success', async () => {
    const { user, getInput, getTextarea, getAddTagInput, getAddTagButton, getSubmitButton } = renderComponent();

    const tag = 'React';
    await user.type(getInput(), 'Title');
    await user.type(getTextarea(), 'Description');
    await addTag(user, tag, getAddTagInput(), getAddTagButton());

    await user.click(getSubmitButton());

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalled();
      expect(mockNavigate).toHaveBeenCalledWith('/community');
    });
  });
});
