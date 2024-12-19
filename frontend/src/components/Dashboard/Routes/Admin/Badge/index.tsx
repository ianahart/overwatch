import BadgeHeader from './BadgeHeader';
import BadgeList from './BadgeList';

const Badge = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <BadgeHeader />
      <BadgeList />
    </div>
  );
};

export default Badge;
