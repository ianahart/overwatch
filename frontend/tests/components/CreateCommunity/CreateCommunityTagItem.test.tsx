import { screen, render } from '@testing-library/react';

import { db } from '../../mocks/db';
import CreateCommunityTagItem, {
  ICreateCommunityTagItemProps,
} from '../../../src/components/CreateCommunity/CreateCommunityTagItem';
import { AllProviders } from '../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('CreateCommunityTagItem', () => {
  const renderComponent = () => {
    const mockRemoveTag = vi.fn();
    const tag = db.tag.create({ name: 'React' });

    const props: ICreateCommunityTagItemProps = {
      tag: { ...tag, id: tag.id.toString() },
      removeTag: mockRemoveTag,
    };

    render(<CreateCommunityTagItem {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      removeTag: mockRemoveTag,
      tagText: screen.getByText(`#${tag.name}`),
      container: screen.getByTestId('tag-item-container'),
      closeIcon: screen.getByRole('close-icon'),
      tag,
    };
  };

  it('should render the component correctly', () => {
    const { tagText, closeIcon } = renderComponent();

    expect(tagText).toBeInTheDocument();
    expect(closeIcon).toBeInTheDocument();
  });

  it('should display the tag name correctly', () => {
    const { tagText, tag } = renderComponent();

    expect(tagText).toHaveTextContent(`#${tag.name}`);
  });

  it('should call removeTag when the close icon is clicked', async () => {
    const { user, container, removeTag, tag } = renderComponent();

    await user.click(container);

    expect(removeTag).toHaveBeenCalled();
    expect(removeTag).toHaveBeenCalledOnce();
    expect(removeTag).toHaveBeenCalledWith(tag.id.toString());
  });
});
