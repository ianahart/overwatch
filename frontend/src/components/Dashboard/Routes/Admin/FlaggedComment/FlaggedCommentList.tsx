import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { IPaginationState, IReportComment } from '../../../../../interfaces';
import { repositoryPaginationState } from '../../../../../data';
import { TRootState, useLazyFetchReportCommentsQuery } from '../../../../../state/store';
import FlaggedCommentListItem from './FlaggedCommentListItem';

const FlaggedCommentList = () => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(repositoryPaginationState);
  const [reportComments, setReportComments] = useState<IReportComment[]>([]);
  const [fetchReportComments] = useLazyFetchReportCommentsQuery();
  useEffect(() => {
    paginateReportComments('next', true);
  }, [token]);

  const paginateReportComments = (dir: string, initial = false) => {
    const payload = {
      token,
      page: initial ? -1 : pag.page,
      pageSize: pag.pageSize,
      direction: dir,
    };

    fetchReportComments(payload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          direction,
          totalPages,
          totalElements,
        }));
        setReportComments(items);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const updateStatus = (id: number, status: string): void => {
    setReportComments(
      reportComments.map((reportComment) => {
        if (reportComment.id === id) {
          return { ...reportComment, status };
        }
        return { ...reportComment };
      })
    );
  };

  return (
    <div className="my-8 p-2">
      <div className="my-6">
        {reportComments.map((reportComment) => {
          return (
            <FlaggedCommentListItem key={reportComment.id} reportComment={reportComment} updateStatus={updateStatus} />
          );
        })}
      </div>
      <div className="flex items-center text-gray-400 justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateReportComments('prev')} className="mx-2">
            Prev
          </button>
        )}
        <p className="mx-2">{pag.page + 1}</p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateReportComments('next')} className="mx-2">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default FlaggedCommentList;
