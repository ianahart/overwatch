import { screen, render } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import TopicDetailsDescription from '../../../src/components/TopicDetails/TopicDetailsDescription';
import { AllProviders } from '../../AllProviders';
import { createTags } from '../../mocks/dbActions';
import { mockNavigate } from '../../setup';

describe('TopicDetailsDescription', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const description = 'This is some description text';
    const tags = createTags(2);

    return {
      tags,
      description,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<TopicDetailsDescription {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
    };
  };

  it('should render the description text', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.description)).toBeInTheDocument();
  });

  it('should render the tags with the correct label', () => {
    const { props } = renderComponent();

    props.tags.forEach(({ name }) => {
      expect(screen.getByText(`#${name}`)).toBeInTheDocument();
    });
  });

  it('should navigate to tag page on tag click', async () => {
    const { user, props } = renderComponent();

    const firstTag = screen.getByText(`#${props.tags[0].name}`);

    await user.click(firstTag);

    expect(mockNavigate).toHaveBeenCalledWith(`/community/tags?tag=${props.tags[0].name}`);
  });
});
