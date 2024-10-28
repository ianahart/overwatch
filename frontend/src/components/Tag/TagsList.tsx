import { useEffect, useState } from 'react';
import { useLazyFetchTopicsWithTagsQuery } from '../../state/store';
import { IPaginationState, ITopic } from '../../interfaces';
import CommunityTopicListItem from '../Community/CommunityTopicListItem';

export interface ITagsListProps {
  query: string;
}

const TagsList = ({ query }: ITagsListProps) => {
  const pagState = {
    page: -1,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const [pag, setPag] = useState<IPaginationState>(pagState);
  const [topics, setTopics] = useState<ITopic[]>([]);
  const [fetchTopicsWithTags] = useLazyFetchTopicsWithTagsQuery();

  useEffect(() => {
    paginateTopicsWithTags('next');
  }, []);

  const paginateTopicsWithTags = (dir: string) => {
    fetchTopicsWithTags({ page: pag.page, direction: dir, pageSize: pag.pageSize, query })
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
          <button onClick={() => paginateTopicsWithTags('prev')} className="mx-1">
            Prev
          </button>
        )}
        <p className="text-green-400">
          {pag.page + 1} of {pag.totalPages}
        </p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateTopicsWithTags('next')} className="mx-1">
            Next
          </button>
        )}
      </div>
    </div>
  );
};
export default TagsList;
