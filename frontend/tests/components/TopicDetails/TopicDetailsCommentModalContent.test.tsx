import { screen, render } from '@testing-library/react';

import TopicDetailsCommentModalContent from '../../../src/components/TopicDetails/TopicDetailsCommentModalContent';
import { getLoggedInUser } from '../../utils';
import userEvent from '@testing-library/user-event';

describe('TopicDetailsCommentModalContent', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getForm = () => {
    return {
      getHeading: () => screen.getByRole('heading', { name: /add a comment/i }),
      getTextarea: () => screen.getByPlaceholderText('Write your comment here'),
      getAddButton: () => screen.getByRole('button', { name: /add comment/i }),
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = {
      topicId: 1,
      handleCloseModal: vi.fn(),
      ...overrides,
    };

    const { wrapper } = getLoggedInUser();

    render(<TopicDetailsCommentModalContent {...props} />, { wrapper });

    return {
      getForm,
      user: userEvent.setup(),
      props,
    };
  };

  it('should render form elements correctly', () => {
    const { getForm } = renderComponent();

    const form = getForm();

    expect(form.getHeading()).toBeInTheDocument();
    expect(form.getTextarea()).toBeInTheDocument();
    expect(form.getAddButton()).toBeInTheDocument();
    expect(form.getCancelButton()).toBeInTheDocument();
  });

  it('should show validation error for empty content', async () => {
    const { user, getForm } = renderComponent();

    const form = getForm();

    await user.clear(form.getTextarea());
    await user.click(form.getAddButton());

    const error = 'Comment must not be empty and must be under 400 characters';

    expect(await screen.findByText(error)).toBeInTheDocument();
  });

  it('should submit valid content and calls close handler', async () => {
    const { props, getForm, user } = renderComponent();

    const form = getForm();

    await user.type(form.getTextarea(), 'This is a comment');

    await user.click(form.getAddButton());

    expect(props.handleCloseModal).toHaveBeenCalledTimes(1);
  });

  it('should show server side validation errors', async () => {
    const { getForm, user } = renderComponent({ topicId: 0 });

    const form = getForm();

    await user.type(form.getTextarea(), 'This is a comment');

    await user.click(form.getAddButton());

    expect(await screen.findByText('Missing topicId or userId')).toBeInTheDocument();
  });

  it('should close modal when the cancel button is clicked', async () => {
    const { user, props, getForm } = renderComponent();

    const form = getForm();

    await user.click(form.getCancelButton());

    expect(props.handleCloseModal).toHaveBeenCalledTimes(1);
  });
});
