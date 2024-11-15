import { useEffect, useState } from 'react';
import { IPaginationState, IPaymentIntent } from '../../../../interfaces';
import { repositoryPaginationState } from '../../../../data';
import { useLazyFetchPaymentIntentsQuery } from '../../../../state/store';
import CompletedPaymentItem from './CompletedPaymentItem';

export interface ICompletedPaymentsProps {
  userId: number;
  token: string;
}

const CompletedPayments = ({ userId, token }: ICompletedPaymentsProps) => {
  const [pag, setPag] = useState<IPaginationState>(repositoryPaginationState);
  const [paymentIntents, setPaymentIntents] = useState<IPaymentIntent[]>([]);
  const [fetchPaymentIntents] = useLazyFetchPaymentIntentsQuery();

  useEffect(() => {
    paginateCompletedPayments('next', true);
  }, [token]);

  const paginateCompletedPayments = (dir: string, initial = false) => {
    const payload = {
      token,
      page: initial ? -1 : pag.page,
      pageSize: pag.pageSize,
      direction: dir,
      userId,
    };

    fetchPaymentIntents(payload)
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
        setPaymentIntents(items);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleUpdateStatus = (id: number, status: string): void => {
    setPaymentIntents(
      paymentIntents.map((pi) => {
        return pi.id === id ? { ...pi, status } : { ...pi };
      })
    );
  };

  return (
    <div className="my-8 p-2 rounded-lg border-gray-800 border">
      <h3 className="text-xl text-gray-400 my-4">Completed Payments</h3>
      <p className="my-2">
        Here you can view a list of your completed payments. If necessary you can ask for a refund if you provide a
        reason that we agree with.
      </p>
      <div className="my-4">
        {paymentIntents.map((paymentIntent) => {
          return (
            <CompletedPaymentItem
              key={paymentIntent.id}
              paymentIntent={paymentIntent}
              handleUpdateStatus={handleUpdateStatus}
            />
          );
        })}
      </div>
      <div className="flex items-center text-gray-400 justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateCompletedPayments('prev')} className="mx-2">
            Prev
          </button>
        )}
        <p className="mx-2">{pag.page + 1}</p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateCompletedPayments('next')} className="mx-2">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default CompletedPayments;
