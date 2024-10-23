import CommunityCreateTopicLink from './CommunityCreateTopicLink';
import CommunityHeader from './CommunityHeader';
import CommunitySearchBar from './CommunitySearchBar';

const Community = () => {
  return (
    <div className="max-w-[1280px] border mx-auto p-2 min-h-[100vh]">
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
    </div>
  );
};

export default Community;
