import { useSelector } from 'react-redux';
import { TRootState, useFetchStatisticsQuery } from '../../../../../state/store';
import Spinner from '../../../../Shared/Spinner';
import AvgRatingBarChart from './AvgRatingBarChart';
import AvgReviewTimeLineChart from './AvgReviewTimeLineChart';
import MainLanguagesBarChart from './MainLanguagesBarChart';
import ReviewTypesCompletedDoughnutChart from './ReviewTypesCompletedDoughnutChart';
import ReviewsCompletedLineChart from './ReviewsCompletedLineChart';
import StatusTypesPieChart from './StatusTypesPieChart';
import TopRequesterHorizontalBarChart from './TopRequesterHorizontalBarChart';

const Statistic = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const { data, isLoading } = useFetchStatisticsQuery({ reviewerId: user.id, token });

  return (
    <div>
      {isLoading && (
        <div>
          <Spinner message="Loading statistics. Please wait." />
        </div>
      )}
      <div className="my-8">
        {data && data.data.avgRatings.length > 0 && <AvgRatingBarChart data={data.data.avgRatings} />}
        {data && data.data.avgReviewTimes.length > 0 && <AvgReviewTimeLineChart data={data.data.avgReviewTimes} />}
        {data && data.data.mainLanguages.length > 0 && <MainLanguagesBarChart data={data.data.mainLanguages} />}
        {data && data.data.reviewTypesCompleted.length > 0 && (
          <ReviewTypesCompletedDoughnutChart data={data.data.reviewTypesCompleted} />
        )}
        {data && data.data.reviewsCompleted.length > 0 && (
          <ReviewsCompletedLineChart data={data.data.reviewsCompleted} />
        )}
        {data && data.data.statusTypes.length > 0 && <StatusTypesPieChart data={data.data.statusTypes} />}
        {data && data.data.topRequesters.length > 0 && (
          <TopRequesterHorizontalBarChart data={data.data.topRequesters} />
        )}
      </div>
    </div>
  );
};

export default Statistic;
