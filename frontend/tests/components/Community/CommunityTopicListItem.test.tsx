import { screen, render } from '@testing-library/react';
import { mockNavigate } from '../../setup';
import { db } from '../../mocks/db';
import CommunityTopicListItem from '../../../src/components/Community/CommunityTopicListItem';
import { AllProviders } from '../../AllProviders';
import { faker } from '@faker-js/faker';
import userEvent from '@testing-library/user-event';

describe('CommunityTopicListItem', () => {
  const tagIds = [1];

  const renderComponent = () => {
    tagIds.forEach((id) => db.tag.create({ id, name: 'Web Development' }));

    const topic = db.topic.create({
      id: faker.number.int(10000),
      title: 'List Item',
      description: 'This is a sentence that makes no sense, but is to be shortened by a function.',
      tags: db.tag.findMany({ where: { id: { in: tagIds } } }),
      totalCommentCount: 1,
    });

    render(<CommunityTopicListItem topic={topic} />, { wrapper: AllProviders });

    return { topic, navigate: mockNavigate, user: userEvent.setup() };
  };

  beforeEach(() => {
    db.tag.deleteMany({ where: { id: { in: tagIds } } });
  });

  it('should render topic title and shortened description', async () => {
    const { topic } = renderComponent();

    expect(await screen.findByRole('heading', { name: topic.title })).toBeInTheDocument();
    expect(await screen.findByText('This is a sentence that makes no sense, but is...')).toBeInTheDocument();
  });

  it('should render the tags correctly', async () => {
    renderComponent();

    expect(await screen.findByText(/web development/i)).toBeInTheDocument();
  });

  it('should navigate to the correct topic page when clicked', async () => {
    const { navigate, topic, user } = renderComponent();

    const container = await screen.findByText(topic.title);

    await user.click(container);

    expect(navigate).toHaveBeenCalledWith(`/community/topics/${topic.id}`);
  });
});
