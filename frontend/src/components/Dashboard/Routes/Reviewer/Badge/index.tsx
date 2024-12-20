import BadgeHeader from './BadgeHeader';
import BadgeList from './BadgeList';

const ReviewerBadge = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <BadgeHeader />
      <BadgeList />
    </div>
  );
};

export default ReviewerBadge;
