import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { IPaginationState, IPaymentIntentTransaction } from '../../../../../interfaces';
import { TRootState, useLazyFetchAllPaymentIntentsQuery } from '../../../../../state/store';
import TableHeader from './TableHeader';
import { nanoid } from 'nanoid';
import dayjs from 'dayjs';
import { convertCentsToDollars } from '../../../../../util';

const TransactionList = () => {
  const paginationState = {
    page: 0,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };

  const { token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [transactions, setTransactions] = useState<IPaymentIntentTransaction[]>([]);
  const [totalRevenue, setTotalRevenue] = useState<number | null>(null);
  const [fetchTransactions] = useLazyFetchAllPaymentIntentsQuery();

  useEffect(() => {
    paginateTransactions('next', true);
  }, [token]);

  const paginateTransactions = (dir: string, initial = false): void => {
    const payload = {
      token,
      page: initial ? -1 : pag.page,
      pageSize: pag.pageSize,
      direction: dir,
      search: 'all',
    };

    fetchTransactions(payload)
      .unwrap()
      .then((res) => {
        console.log(res);
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data.result;
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          direction,
          totalPages,
          totalElements,
        }));
        setTransactions(items);
        if (totalRevenue === null) {
          setTotalRevenue(convertCentsToDollars(res.data.revenue));
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const headerMapping = {
    id: 'ID',
    userId: 'User ID',
    reviewerId: 'Reviewer ID',
    userFullName: 'User Name',
    reviewerFullName: 'Reviewer Name',
    createdAt: 'Date',
    currency: 'Currency',
    amount: 'Amount',
    applicationFee: 'Fee',
    userEmail: 'User Email',
    reviewerEmail: 'Reviewer Email',
  };

  const formatColumnData = (column: string, value: string | number) => {
    switch (column) {
      case 'createdAt':
        return dayjs(value).format('MM/D/YYYY hh:mm:ss a');
      case 'amount':
        return `$${convertCentsToDollars(value as number)}`;
      case 'applicationFee':
        return `+$${convertCentsToDollars(value as number)}`;
      default:
        return value;
    }
  };

  return (
    <div className="my-8 p-2">
      <div className="flex justify-center">
        <h3 className="text-gray-400 text-xl">
          Total Revenue: <span className="text-green-400 ml-1">+${totalRevenue}</span>
        </h3>
      </div>
      <div className="my-6 overflow-x-auto">
        <table className="border-collapse w-full rounded">
          <tr>
            {Object.entries(headerMapping).map(([key, val]) => {
              return <TableHeader key={key} heading={val} />;
            })}
          </tr>
          {transactions.map((row) => {
            return (
              <tr key={row.id}>
                {Object.keys(headerMapping).map((column) => {
                  return (
                    <td
                      className={`${column === 'applicationFee' ? 'text-green-400' : 'text-gray-400'}`}
                      key={nanoid()}
                    >
                      {formatColumnData(column, row[column])}
                    </td>
                  );
                })}
              </tr>
            );
          })}
        </table>
      </div>
      <div className="flex items-center text-gray-400 justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateTransactions('prev')} className="mx-2">
            Prev
          </button>
        )}
        <p className="mx-2">{pag.page + 1}</p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateTransactions('next')} className="mx-2">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default TransactionList;
