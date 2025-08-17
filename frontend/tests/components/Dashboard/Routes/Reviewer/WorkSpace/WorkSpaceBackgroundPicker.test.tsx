import { screen, render, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import userEvent from '@testing-library/user-event';
import { Mock } from 'vitest';
import { getLoggedInUser } from '../../../../../utils';
import { createWorkSpaces } from '../../../../../mocks/dbActions';
import WorkSpaceBackgroundPicker from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/WorkSpaceBackgroundPicker';
import { setWorkSpace } from '../../../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('WorkSpaceBackgroundPicker', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const renderComponent = () => {
    const [workSpace] = createWorkSpaces(1);
    const { wrapper } = getLoggedInUser(
      {},
      {
        workSpace: { workSpace },
      }
    );

    render(<WorkSpaceBackgroundPicker />, { wrapper });

    return {
      workSpace,
      user: userEvent.setup(),
      getDots: () => screen.getByTestId('background-picker-dots'),
    };
  };

  it('should render closed by default', () => {
    renderComponent();
    expect(screen.queryByText(/workspace background color/i)).not.toBeInTheDocument();
  });

  it('should open menu on dots click', async () => {
    const { user, getDots } = renderComponent();

    await user.click(getDots());

    expect(await screen.findByText(/workspace background color/i)).toBeInTheDocument();
  });

  it('should close menu on click away', async () => {
    const { user, getDots } = renderComponent();
    await user.click(getDots());
    expect(screen.getByText(/Workspace background color/i)).toBeInTheDocument();

    await user.click(document.body);
    expect(screen.queryByText(/Workspace background color/i)).not.toBeInTheDocument();
  });

  it('should select a color and update workspace', async () => {
    const { user, getDots } = renderComponent();

    await user.click(getDots());

    const backgroundColors = await screen.findAllByTestId('background-picker-colors');

    await user.click(backgroundColors[0]);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        setWorkSpace(
          expect.objectContaining({
            backgroundColor: expect.any(String),
          })
        )
      );
    });
  });
});
