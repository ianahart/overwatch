import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';

import 'react-toastify/dist/ReactToastify.css';
import {
  TRootState,
  useLazyFetchRepositoriesQuery,
  useTransferCustomerMoneyToReviewerMutation,
} from '../../../state/store';
import { repositoryPaginationState } from '../../../data';
import { IPaginationState, IRepositoryReview } from '../../../interfaces';
import GetPaidCompleteRepositoryListItem from './GetPaidCompleteRepositoryListItem';
import Spinner from '../../Shared/Spinner';

const GetPaidCompleteRepositoryList = () => {
  const [pag, setPag] = useState<IPaginationState>(repositoryPaginationState);
  const { token } = useSelector((store: TRootState) => store.user);
  const [repositories, setRepositories] = useState<IRepositoryReview[]>([]);
  const [fetchRepositories] = useLazyFetchRepositoriesQuery();
  const [transferCustomMoneyToReviewer, { isLoading }] = useTransferCustomerMoneyToReviewerMutation();

  useEffect(() => {
    paginateCompletedRepositories('next', true);
  }, [token]);

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
        setRepositories(items.filter((item) => item.status !== 'PAID'));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const initiateToast = () => {
    toast.success('Congratulations, you have been successfully paid!', {
      position: 'bottom-center',
      autoClose: 7000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: 'dark',
    });
  };

  const transferMoneyBetweenParties = (repositoryId: number, ownerId: number, reviewerId: number): void => {
    const payload = { repositoryId, ownerId, reviewerId, token };
    transferCustomMoneyToReviewer(payload)
      .unwrap()
      .then((res) => {
        console.log(res);
        setRepositories((prevState) => prevState.filter((repository) => repository.id !== repositoryId));
        initiateToast();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="mt-12 mb-12">
      {isLoading && (
        <div className="my-8 flex justify-center">
          <Spinner message="Completing transaction. Please wait." />
        </div>
      )}
      {!isLoading && (
        <ul>
          {repositories.map((repository) => {
            transferMoneyBetweenParties;
            return (
              <GetPaidCompleteRepositoryListItem
                key={repository.id}
                repository={repository}
                transferMoneyBetweenParties={transferMoneyBetweenParties}
              />
            );
          })}
        </ul>
      )}
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
      <ToastContainer />
    </div>
  );
};

export default GetPaidCompleteRepositoryList;
