import { RxActivityLog } from 'react-icons/rx';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import { IActivity, IPaginationState, ITodoCard } from '../../../../../interfaces';
import CardActivityForm from './CardActivityForm';
import {
  TRootState,
  useDeleteActivityMutation,
  useFetchActivitiesQuery,
  useLazyFetchActivitiesQuery,
} from '../../../../../state/store';
import { BsTrash } from 'react-icons/bs';
import Avatar from '../../../../Shared/Avatar';
import Spinner from '../../../../Shared/Spinner';
import dayjs from 'dayjs';

export interface ICardActivityProps {
  card: ITodoCard;
}

const CardActivity = ({ card }: ICardActivityProps) => {
  const paginationState = {
    page: 0,
    pageSize: 2,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const { token } = useSelector((store: TRootState) => store.user);
  const [activities, setActivities] = useState<IActivity[]>([]);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [fetchActivities] = useLazyFetchActivitiesQuery();
  const [deleteActivity] = useDeleteActivityMutation();
  const { data, isLoading } = useFetchActivitiesQuery(
    {
      token,
      page: -1,
      pageSize: 2,
      direction: 'next',
      todoCardId: card.id,
    },
    { skip: !token || !card.id }
  );

  useEffect(() => {
    if (data !== undefined) {
      const { items, page, pageSize, totalPages, direction, totalElements } = data.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setActivities(items);
    }
  }, [data]);

  const paginateActivities = async (dir: string) => {
    try {
      const response = await fetchActivities({
        todoCardId: card.id,
        token,
        page: pag.page,
        pageSize: pag.pageSize,
        direction: dir,
      }).unwrap();

      const { items, page, pageSize, totalPages, direction, totalElements } = response.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setActivities((prevState) => [...prevState, ...items]);
    } catch (err) {
      console.log(err);
    }
  };

  const handleDeleteActivity = (activityId: number) => {
    deleteActivity({ token, activityId })
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="my-8">
      <div className="flex items-center">
        <div className="mr-2">
          <RxActivityLog />
        </div>
        <h3>Activity</h3>
      </div>
      <div className="my-4">
        <CardActivityForm todoCardId={card.id} />
        {isLoading && (
          <div className="flex justify-center my-4">
            <Spinner message="Loading activity..." />
          </div>
        )}
        <div className="my-4 w-full md:w-[75%]">
          {activities.map((activity) => {
            return (
              <div className="flex justify-between my-2" key={activity.id}>
                <div>
                  <div className="mr-2 flex items-center">
                    <Avatar initials={'?.?'} avatarUrl={activity.avatarUrl} width="w-9" height="h-9" />
                    <p>{activity.text}</p>
                  </div>
                  <div>
                    <p className="text-xs">{dayjs(activity.createdAt).format('MM/DD/YYYY h:m A')}</p>
                  </div>
                </div>
                <div className="cursor-pointer" onClick={() => handleDeleteActivity(activity.id)}>
                  <BsTrash />
                </div>
              </div>
            );
          })}
          {pag.page < pag.totalPages - 1 && (
            <div className="my-4 flex justify-center">
              <button onClick={() => paginateActivities('next')} className="btn">
                See more
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CardActivity;
