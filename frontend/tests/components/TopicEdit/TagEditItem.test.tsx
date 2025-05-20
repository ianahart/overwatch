import { screen, render } from '@testing-library/react';
import TagEditItem from '../../../src/components/TopicEdit/TagEditItem';
import { AllProviders } from '../../AllProviders';
import { ITag } from '../../../src/interfaces';
import userEvent from '@testing-library/user-event';

describe('TagEditItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderComponent = () => {
    const handleUpdateTag = vi.fn();
    const handleDeleteTag = vi.fn();

    const mockTag: ITag = {
      id: 1,
      name: 'javascript',
    };

    render(<TagEditItem tag={mockTag} handleDeleteTag={handleDeleteTag} handleUpdateTag={handleUpdateTag} />, {
      wrapper: AllProviders,
    });

    return {
      tag: mockTag,
      user: userEvent.setup(),
      getUpdateIcon: () => screen.findByTestId('update-tag-icon'),
      getDeleteIcon: () => screen.findByTestId('delete-tag-icon'),
      getTagText: () => screen.findByText(`#${mockTag.name}`),
      handleDeleteTag,
      handleUpdateTag,
    };
  };
  it('should render tag name and edit/delete-icons', async () => {
    const { getDeleteIcon, getUpdateIcon, getTagText } = renderComponent();

    expect(await getDeleteIcon()).toBeInTheDocument();
    expect(await getUpdateIcon()).toBeInTheDocument();
    expect(await getTagText()).toBeInTheDocument();
  });

  it('should enter edit mode when the edit icon is clicked', async () => {
    const { user, tag, getUpdateIcon } = renderComponent();

    await user.click(await getUpdateIcon());

    expect(screen.getByLabelText(/edit tag name/i)).toBeInTheDocument();
    expect(screen.getByDisplayValue(tag.name)).toBeInTheDocument();
  });

  it('should update tag and exit edit mode', async () => {
    const { user, tag, getUpdateIcon, handleUpdateTag } = renderComponent();

    await user.click(await getUpdateIcon());

    const input = screen.getByDisplayValue(tag.name);

    await user.clear(input);
    await user.type(input, 'typescript');

    const editButton = screen.getByRole('button', { name: /edit/i });

    await user.click(editButton);

    expect(handleUpdateTag).toHaveBeenCalledWith('typescript', 1);
    expect(screen.queryByLabelText(/edit tag name/i)).not.toBeInTheDocument();
  });

  it('should call handleDeleteTag when delete icon is clicked', async () => {
    const { handleDeleteTag, user, getDeleteIcon } = renderComponent();

    await user.click(await getDeleteIcon());

    expect(handleDeleteTag).toHaveBeenCalledWith(1);
  });
});
