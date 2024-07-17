import ReviewerOutletContainer from './ReviewerOutletContianer';
import ReviewerSidebarNavigation from './ReviewerSidebarNavigation';

const ReviewerDashboard = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="md:flex bg-gray-900 p-2 rounded">
        <ReviewerSidebarNavigation />
        <ReviewerOutletContainer />
      </div>
    </div>
  );
};

export default ReviewerDashboard;
