import { screen, render } from '@testing-library/react';
import CommunitySearchTopicList from '../../../src/components/Community/CommunitySearchTopicList';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';

describe('CommunitySearchTopicList', () => {
  const topicIds = [1, 2, 3];

  const renderComponent = () => {
    topicIds.forEach((topicId) => {
      db.topic.create({ id: topicId });
    });

    const topics = db.topic.findMany({});

    render(<CommunitySearchTopicList topics={topics} />, { wrapper: AllProviders });
  };

  beforeEach(() => {
    db.topic.deleteMany({ where: { id: { in: topicIds } } });
  });

  it('should render the passed down topics initially', async () => {
    renderComponent();

    const topics = await screen.findAllByTestId('community-topic-list-item');

    expect(topics).toHaveLength(topicIds.length);
  });
});
