import { useEffect, useState } from 'react';
import { IComment, IPaginationState } from '../../interfaces';
import { useFetchCommentsQuery, useLazyFetchCommentsQuery } from '../../state/store';
import TopicDetailsCommentItem from './TopicDetailsCommentItem';
import Spinner from '../Shared/Spinner';

export interface ITopicDetailsCommentsProps {
  topicId: number;
}

export interface IEnhancedPaginationState extends IPaginationState {
  sort: string;
}

const TopicDetailsComments = ({ topicId }: ITopicDetailsCommentsProps) => {
  const pagState = {
    page: -1,
    pageSize: 2,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
    sort: 'DESC',
  };
  const [fetchComments] = useLazyFetchCommentsQuery();
  const { data, isLoading } = useFetchCommentsQuery({
    topicId,
    page: pagState.page,
    pageSize: pagState.pageSize,
    direction: pagState.direction,
    sort: pagState.sort,
  });
  const [pag, setPag] = useState<IEnhancedPaginationState>(pagState);
  const [comments, setComments] = useState<IComment[]>([]);

  useEffect(() => {
    if (data !== undefined) {
      const { page, pageSize, direction, totalPages, totalElements, items } = data.data;

      setComments(items);
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        direction,
        totalPages,
        totalElements,
      }));
    }
  }, [data]);

  const paginateComments = async (dir: string, sort: string, reset = false) => {
    try {
      const response = await fetchComments({
        topicId,
        page: reset ? -1 : pag.page,
        pageSize: pag.pageSize,
        direction: dir,
        sort,
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
      setComments(items);
    } catch (err) {
      console.log(err);
    }
  };

  const handleOnChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const sort = e.target.value;
    setPag((prevState) => ({
      ...prevState,
      sort,
    }));
    paginateComments('next', sort, true);
  };

  return (
    <>
      <div className="my-4">
        <select
          value={pag.sort}
          onChange={handleOnChange}
          className="bg-transparent border border-gray-800 w-[200px] p-2 rounded"
        >
          <option selected value="DESC">
            Newest
          </option>
          <option value="ASC">Oldest</option>
        </select>
      </div>

      <div className="my-8">
        {isLoading && (
          <div className="my-4 flex justify-center">
            <Spinner message="Loading comments..." />
          </div>
        )}
        <div>
          {comments.map((comment) => {
            return <TopicDetailsCommentItem key={comment.id} comment={comment} />;
          })}
        </div>
        <div className="flex items-center justify-center">
          {pag.page > 0 && (
            <button onClick={() => paginateComments('prev', pag.sort)} className="mx-1">
              Prev
            </button>
          )}
          <p className="text-green-400">
            {pag.page + 1} of {pag.totalPages}
          </p>
          {pag.page < pag.totalPages - 1 && (
            <button onClick={() => paginateComments('next', pag.sort)} className="mx-1">
              Next
            </button>
          )}
        </div>
      </div>
    </>
  );
};

export default TopicDetailsComments;
