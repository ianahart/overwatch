import { screen, render, waitFor } from '@testing-library/react';

import TopicDetailsCommentItemForm from '../../../src/components/TopicDetails/TopicDetailsCommentItemForm';
import { getLoggedInUser } from '../../utils';
import userEvent from '@testing-library/user-event';

describe('TopicDetailsCommentItemForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    return {
      handleSetIsEditing: vi.fn(),
      getTextArea: () => screen.getByLabelText(/edit comment/i),
      getUpdateButton: () => screen.getByRole('button', { name: /update/i }),
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
      commentId: 1,
      content: 'This is a comment',
      ...overrides,
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps();

    render(<TopicDetailsCommentItemForm {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
      props,
    };
  };

  it('should render textarea with existing comment content', () => {
    const { props } = renderComponent();
    const { getTextArea } = props;

    expect(getTextArea()).toHaveValue(props.content);
  });

  it('should display an error if the content is empty and submit is attemped', async () => {
    const { user, props } = renderComponent();
    const { getTextArea, getUpdateButton } = props;

    await user.clear(getTextArea());
    await user.click(getUpdateButton());

    expect(await screen.findByText(/between 1 and 400 characters/)).toBeInTheDocument();
  });

  it('should call mutation and exit edit mode on successful submit', async () => {
    const { user, props } = renderComponent();
    const { handleSetIsEditing, getTextArea, getUpdateButton } = props;

    await user.clear(getTextArea());
    await user.type(getTextArea(), 'updated comment'), await user.click(getUpdateButton());

    await waitFor(() => {
      expect(handleSetIsEditing).toHaveBeenCalledWith(false);
      expect(screen.getByText('updated comment')).toBeInTheDocument();
    });
  });

  test('should exit editing mode when clicking cancel', async () => {
    const { user, props } = renderComponent();

    const { handleSetIsEditing, getCancelButton } = props;

    await user.click(getCancelButton());

    expect(handleSetIsEditing).toHaveBeenCalledWith(false);
  });
});
