import { useEffect, useState } from 'react';
import { IPaginationState, ITopic } from '../../interfaces';
import { useLazyFetchTopicsQuery } from '../../state/store';
import CommunityTopicListItem from './CommunityTopicListItem';

const CommunityTopicList = () => {
  const pagState = {
    page: -1,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const [pag, setPag] = useState<IPaginationState>(pagState);
  const [topics, setTopics] = useState<ITopic[]>([]);
  const [fetchTopics] = useLazyFetchTopicsQuery();

  useEffect(() => {
    paginateTopics('next');
  }, []);

  const paginateTopics = (dir: string) => {
    fetchTopics({ page: pag.page, direction: dir, pageSize: pag.pageSize })
      .unwrap()
      .then((res) => {
        const { items, page, pageSize, totalPages, direction, totalElements } = res.data;
        setTopics(items);
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          totalPages,
          totalElements,
          direction,
        }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="my-8">
      <div className="my-4 flex flex-col items-center">
        {topics.map((topic) => {
          return <CommunityTopicListItem key={topic.id} topic={topic} />;
        })}
      </div>
      <div className="flex items-center justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateTopics('prev')} className="mx-1">
            Prev
          </button>
        )}
        <p className="text-green-400">
          {pag.page + 1} of {pag.totalPages}
        </p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateTopics('next')} className="mx-1">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default CommunityTopicList;
