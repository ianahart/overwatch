import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

import WorkExperience from '../../../../src/components/Settings/EditProfile/WorkExperience';
import { addWorkExpToList, removeWorkExpFromList } from '../../../../src/state/store';
import { getLoggedInUser } from '../../../utils';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('WorkExperience', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = () => {
    const user = userEvent.setup();

    const { wrapper } = getLoggedInUser(
      {},
      {
        workExp: {
          workExps: [{ id: '1', title: 'Software Engineer', desc: 'Worked on React apps' }],
        },
      }
    );

    render(<WorkExperience />, { wrapper });
    return { user };
  };

  it('renders header, inputs, button, and existing item', () => {
    renderComponent();

    expect(screen.getByText(/work & experience/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/title/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/description/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /add/i })).toBeInTheDocument();

    expect(screen.getByRole('heading', { name: /software engineer/i })).toBeInTheDocument();
    expect(screen.getByText(/worked on react apps/i)).toBeInTheDocument();
  });

  it('dispatches addWorkExpToList when title and description are filled and add is clicked', async () => {
    const { user } = renderComponent();

    await user.clear(screen.getByLabelText(/title/i));
    await user.type(screen.getByLabelText(/title/i), 'Frontend Dev');

    await user.clear(screen.getByLabelText(/description/i));
    await user.type(screen.getByLabelText(/description/i), 'Built UI with React');

    await user.click(screen.getByRole('button', { name: /add/i }));

    expect(mockDispatch).toHaveBeenCalledWith(
      addWorkExpToList({
        title: 'Frontend Dev',
        desc: 'Built UI with React',
      })
    );
  });

  it('does not dispatch if fields are empty', async () => {
    const { user } = renderComponent();

    await user.clear(screen.getByLabelText(/title/i));
    await user.clear(screen.getByLabelText(/description/i));
    await user.click(screen.getByRole('button', { name: /add/i }));

    expect(mockDispatch).not.toHaveBeenCalled();
  });

  it('dispatches removeWorkExpFromList when delete icon is clicked', async () => {
    const { user } = renderComponent();

    const closeIcon = screen.getByTestId('ai-outline-close');
    await user.click(closeIcon);

    expect(mockDispatch).toHaveBeenCalledWith(removeWorkExpFromList('1'));
  });
});
