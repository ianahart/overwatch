import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Toolbar, {
  IToolbarProps,
} from '../../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/Toolbar';
import * as slateReact from 'slate-react';
import CustomEditor from '../../../../../../../src/util/CustomEditor';
import { Mock } from 'vitest';
import { getLoggedInUser } from '../../../../../../utils';

vi.mock('slate-react', () => ({
  useSlate: vi.fn(),
}));

vi.mock('../../../../../../../src/util/CustomEditor', async () => {
  return {
    default: {
      toggleUnderlineMark: vi.fn(),
      toggleItalicMark: vi.fn(),
      toggleBoldMark: vi.fn(),
      toggleCodeBlock: vi.fn(),
      toggleList: vi.fn(),
      toggleAlignment: vi.fn(),
      toggleHeading: vi.fn(),
      toggleParagraph: vi.fn(),
    },
  };
});

describe('<Toolbar />', () => {
  const dummyEditor = {};

  beforeEach(() => {
    (slateReact.useSlate as Mock).mockReturnValue(dummyEditor);
    vi.clearAllMocks();
  });

  const renderToolbar = (props: Partial<IToolbarProps> = {}) => {
    const { wrapper } = getLoggedInUser({ role: 'REVIEWER' });
    render(<Toolbar {...props} />, { wrapper });
  };

  it('renders all formatting buttons', () => {
    renderToolbar();

    expect(screen.getByTestId('bold-button')).toBeInTheDocument();
    expect(screen.getByTestId('italic-button')).toBeInTheDocument();
    expect(screen.getByTestId('underline-button')).toBeInTheDocument();
    expect(screen.getByTestId('code-button')).toBeInTheDocument();
    expect(screen.getByTestId('unordered-list-button')).toBeInTheDocument();
    expect(screen.getByTestId('ordered-list-button')).toBeInTheDocument();
    expect(screen.getByTestId('align-left-button')).toBeInTheDocument();
    expect(screen.getByTestId('align-center-button')).toBeInTheDocument();
    expect(screen.getByTestId('align-right-button')).toBeInTheDocument();
    expect(screen.getByTestId('heading1-button')).toBeInTheDocument();
    expect(screen.getByTestId('heading2-button')).toBeInTheDocument();
    expect(screen.getByTestId('heading3-button')).toBeInTheDocument();
    expect(screen.getByTestId('heading4-button')).toBeInTheDocument();
    expect(screen.getByTestId('paragraph-button')).toBeInTheDocument();
  });

  it('clicking underline button calls toggleUnderlineMark', async () => {
    renderToolbar();
    const user = userEvent.setup();

    await user.click(screen.getByTestId('underline-button'));
    expect(CustomEditor.toggleUnderlineMark).toHaveBeenCalledWith(dummyEditor);
  });

  it('calls onSaveTemplate when save icon is clicked', async () => {
    const onSaveTemplate = vi.fn();
    renderToolbar({ onSaveTemplate });
    const user = userEvent.setup();

    await user.click(screen.getByTestId('save-template-button'));
    expect(onSaveTemplate).toHaveBeenCalled();
  });

  it('shows and interacts with template list', async () => {
    const utilizeTemplate = vi.fn();
    const deleteTemplate = vi.fn();
    const templates = [
      { id: 1, userId: 1 },
      { id: 2, userId: 1 },
    ];

    renderToolbar({ utilizeTemplate, deleteTemplate, templates });
    const user = userEvent.setup();

    // open template dropdown
    await user.click(screen.getByTestId('view-templates-button'));
    expect(screen.getByText('Your Templates')).toBeInTheDocument();

    // click template text
    await user.click(screen.getByTestId('utilize-template-1'));
    expect(utilizeTemplate).toHaveBeenCalledWith(1);

    // click delete icon
    await user.click(screen.getByTestId('delete-template-1'));
    expect(deleteTemplate).toHaveBeenCalledWith(1);
  });

  it('does not render template features for non-reviewer users', () => {
    const { wrapper } = getLoggedInUser({ role: 'STUDENT' }); // simulate student role
    render(<Toolbar />, { wrapper });

    expect(screen.queryByTestId('save-template-button')).not.toBeInTheDocument();
    expect(screen.queryByTestId('view-templates-button')).not.toBeInTheDocument();
  });
});
