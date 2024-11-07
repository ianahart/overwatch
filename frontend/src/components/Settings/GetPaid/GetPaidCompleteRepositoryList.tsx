import { useSelector } from 'react-redux';
import { TRootState, useLazyFetchRepositoriesQuery } from '../../../state/store';
import { repositoryPaginationState } from '../../../data';
import { IPaginationState, IRepositoryReview } from '../../../interfaces';
import { useEffect, useState } from 'react';
import GetPaidCompleteRepositoryListItem from './GetPaidCompleteRepositoryListItem';

const GetPaidCompleteRepositoryList = () => {
  const [pag, setPag] = useState<IPaginationState>(repositoryPaginationState);
  const { token } = useSelector((store: TRootState) => store.user);
  const [repositories, setRepositories] = useState<IRepositoryReview[]>([]);
  const [fetchRepositories] = useLazyFetchRepositoriesQuery();

  useEffect(() => {
    paginateCompletedRepositories('next', true);
  }, []);

  const paginateCompletedRepositories = (dir: string, initial = false) => {
    const payload = {
      token,
      page: initial ? -1 : pag.page,
      pageSize: pag.pageSize,
      direction: dir,
      sortFilter: 'desc',
      statusFilter: 'COMPLETED',
      languageFilter: 'all',
    };

    fetchRepositories(payload)
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

        setRepositories(items);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="mt-12 mb-12">
      <ul>
        {repositories.map((repository) => {
          return <GetPaidCompleteRepositoryListItem key={repository.id} repository={repository} />;
        })}
      </ul>
      <div className="flex items-center text-gray-400 justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateCompletedRepositories('prev')} className="mx-2">
            Prev
          </button>
        )}
        <p className="mx-2">{pag.page + 1}</p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateCompletedRepositories('next')} className="mx-2">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default GetPaidCompleteRepositoryList;
