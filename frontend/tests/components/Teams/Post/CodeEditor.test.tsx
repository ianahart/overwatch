import { screen, render, waitFor } from '@testing-library/react';

import CodeEditor from '../../../../src/components/Teams/Post/CodeEditor';
import { setMockParams } from '../../../setup';
import { getLoggedInUser } from '../../../utils';
import userEvent from '@testing-library/user-event';

describe('CodeEditor', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    setMockParams({ teamId: '1' });
  });

  const getProps = () => {
    return {
      closeModal: vi.fn(),
    };
  };

  const getFormElements = () => {
    return {
      getSelect: () => screen.getByLabelText(/select language/i),
      getCreateButton: () => screen.getByRole('button', { name: /create/i }),
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps();

    render(<CodeEditor {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      curUser,
      getFormElements,
    };
  };

  it('should render language dropdown, editor, and buttons', () => {
    const { getFormElements } = renderComponent();

    const { getSelect, getCreateButton, getCancelButton } = getFormElements();

    expect(screen.getByText(/ write your code here/i)).toBeInTheDocument();
    expect(getSelect()).toBeInTheDocument();
    expect(getCreateButton()).toBeInTheDocument();
    expect(getCancelButton()).toBeInTheDocument();
  });

  it('should change language from dropdown', async () => {
    const { user, getFormElements } = renderComponent();

    const { getSelect } = getFormElements();

    await user.selectOptions(getSelect(), 'python');

    expect((getSelect() as HTMLSelectElement).value).toBe('python');
  });

  it('should let the user type in the code editor', async () => {
    const { user } = renderComponent();

    const newCode = 'console.log("Hello, world!")';
    await user.click(screen.getByText(/write your code here/i));
    await user.keyboard('{Control>}a{/Control}{Backspace}');
    await user.keyboard(newCode);

    expect(screen.getByText(/console/i)).toBeInTheDocument();
    expect(screen.getByText(/log/i)).toBeInTheDocument();
  });

  it('should call createTeamPost mutation and close modal on success', async () => {
    const { user, props } = renderComponent();

    const { getCreateButton } = getFormElements();

    await user.click(getCreateButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });

  it('should call closeModal when cancel is clicked', async () => {
    const { user, props, getFormElements } = renderComponent();

    const { getCancelButton } = getFormElements();

    await user.click(getCancelButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });
});
