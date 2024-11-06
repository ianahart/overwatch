import { useState } from 'react';
import { useSelector } from 'react-redux';

import CommunityCreateTopicLink from './CommunityCreateTopicLink';
import CommunityHeader from './CommunityHeader';
import CommunitySearchBar from './CommunitySearchBar';
import CommunityTopicList from './CommunityTopicList';
import CommunityTopicSavedCommentList from './CommunityTopicSavedCommentList';
import { TRootState } from '../../state/store';
import CommunityActiveTab from './CommunityActiveTab';
import CommunityUserTopicList from './CommunityUserTopicList';

const Community = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [activeTab, setActiveTab] = useState('topics');

  const handleSetActiveTab = (tab: string): void => {
    setActiveTab(tab);
  };

  return (
    <div className="max-w-[1280px] mx-auto p-2 min-h-[100vh]">
      <CommunityHeader
        heading="Community"
        description="Explore topics being discussed by reviewers and regular users"
      />
      <div className="my-4">
        <CommunityCreateTopicLink linkText="Create new topic" />
      </div>
      <div className="my-8">
        <CommunitySearchBar btnText="Search" />
      </div>
      <div className="flex items-center justify-center">
        <CommunityActiveTab
          tab="topics"
          activeTab={activeTab}
          btnText="Topics"
          handleSetActiveTab={handleSetActiveTab}
        />
        <CommunityActiveTab
          tab="saveComments"
          activeTab={activeTab}
          btnText="Saved Comments"
          handleSetActiveTab={handleSetActiveTab}
        />
        <CommunityActiveTab
          tab="userTopics"
          activeTab={activeTab}
          btnText="Your Topics"
          handleSetActiveTab={handleSetActiveTab}
        />
      </div>
      <div className="my-8">
        {activeTab === 'topics' && <CommunityTopicList />}
        {activeTab === 'savedComments' && user.id !== 0 && (
          <CommunityTopicSavedCommentList userId={user.id} token={token} />
        )}
        {activeTab === 'userTopics' && user.id !== 0 && <CommunityUserTopicList userId={user.id} token={token} />}
      </div>
    </div>
  );
};

export default Community;
