import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { IPaginationState, IRefund } from '../../../../../interfaces';
import { repositoryPaginationState } from '../../../../../data';
import { TRootState, useLazyFetchPaymentRefundsQuery } from '../../../../../state/store';
import RefundListItem from './RefundListItem';

const RefundList = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(repositoryPaginationState);
  const [refunds, setRefunds] = useState<IRefund[]>([]);
  const [fetchPaymentRefunds] = useLazyFetchPaymentRefundsQuery();
  useEffect(() => {
    paginateRefunds('next', true);
  }, [token]);

  const paginateRefunds = (dir: string, initial = false) => {
    const payload = {
      token,
      page: initial ? -1 : pag.page,
      pageSize: pag.pageSize,
      direction: dir,
      userId: user.id,
    };

    fetchPaymentRefunds(payload)
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
        setRefunds(items);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="my-8 p-2">
      <div className="my-6">
        {refunds.map((refund) => {
          return <RefundListItem key={refund.id} refund={refund} />;
        })}
      </div>
      <div className="flex items-center text-gray-400 justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateRefunds('prev')} className="mx-2">
            Prev
          </button>
        )}
        <p className="mx-2">{pag.page + 1}</p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateRefunds('next')} className="mx-2">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default RefundList;
