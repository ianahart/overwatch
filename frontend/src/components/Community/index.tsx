import { useState } from 'react';
import { useSelector } from 'react-redux';

import CommunityCreateTopicLink from './CommunityCreateTopicLink';
import CommunityHeader from './CommunityHeader';
import CommunitySearchBar from './CommunitySearchBar';
import CommunityTopicList from './CommunityTopicList';
import CommunityTopicSavedCommentList from './CommunityTopicSavedCommentList';
import { TRootState } from '../../state/store';

const Community = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [activeTab, setActiveTab] = useState('topics');
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
        <button
          onClick={() => setActiveTab('topics')}
          className={`mx-4 ${activeTab === 'topics' ? 'font-bold text-green-400' : 'text-gray-400 font-normal'}`}
        >
          Topics
        </button>
        <button
          onClick={() => setActiveTab('savedComments')}
          className={`mx-4 ${activeTab === 'savedComments' ? 'font-bold text-green-400' : 'text-gray-400 font-normal'}`}
        >
          Saved Comments
        </button>
      </div>
      <div className="my-8">
        {activeTab === 'topics' && <CommunityTopicList />}
        {activeTab === 'savedComments' && user.id !== 0 && (
          <CommunityTopicSavedCommentList userId={user.id} token={token} />
        )}
      </div>
    </div>
  );
};

export default Community;
