import { useSelector } from 'react-redux';
import { TRootState, useFetchStatisticsQuery } from '../../../../../state/store';
import { useEffect } from 'react';
import Spinner from '../../../../Shared/Spinner';

const Statistic = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const { data, isLoading } = useFetchStatisticsQuery({ reviewerId: user.id, token });

  useEffect(() => {
    if (data !== undefined) {
      console.log(data);
    }
  }, [data]);

  return (
    <div className="text-white">
      {isLoading && (
        <div>
          <Spinner message="Loading statistics. Please wait." />
        </div>
      )}
    </div>
  );
};

export default Statistic;
