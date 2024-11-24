import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { nanoid } from 'nanoid';
import dayjs from 'dayjs';
import { CiExport } from 'react-icons/ci';

import { IPaginationState, IPaymentIntentTransaction } from '../../../../../interfaces';
import {
  TRootState,
  useLazyExportPaymentIntentsToPdfQuery,
  useLazyFetchAllPaymentIntentsQuery,
} from '../../../../../state/store';
import TableHeader from './TableHeader';
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
  const [search, setSearch] = useState('');
  const [error, setError] = useState('');
  const [fetchTransactions] = useLazyFetchAllPaymentIntentsQuery();
  const [exportToPdf] = useLazyExportPaymentIntentsToPdfQuery();

  useEffect(() => {
    paginateTransactions('next', true);
  }, [token]);

  const paginateTransactions = (dir: string, initial = false): void => {
    const payload = {
      token,
      page: initial ? -1 : pag.page,
      pageSize: pag.pageSize,
      direction: dir,
      search: search.trim().length === 0 ? 'all' : search,
    };

    fetchTransactions(payload)
      .unwrap()
      .then((res) => {
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

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    setSearch(e.target.value);
  };

  const canSearch = (value: string): boolean => {
    const MAX_LENGTH = 200;
    if (value.length > MAX_LENGTH) {
      const validationError = `Name must be between 1 and ${MAX_LENGTH} characters`;
      setError(validationError);
      return false;
    }
    return true;
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setError('');

    if (!canSearch(search)) {
      return;
    }
    setPag(paginationState);
    paginateTransactions('next', true);
  };

  const generatePdf = async () => {
    setError('');
    try {
      let page = null;
      if (pag.direction === 'next') {
        page = pag.page > 0 ? pag.page - 1 : -1;
      } else {
        page = pag.page > 0 ? pag.page + 1 : -1;
      }

      const payload = {
        token,
        page,
        pageSize: pag.pageSize,
        direction: pag.direction,
        search: search.trim().length === 0 ? 'all' : search,
      };

      await exportToPdf(payload);
    } catch (err) {
      console.log(err);
      setError('Failed to download PDF');
    }
  };

  return (
    <div className="my-8 p-2">
      <div className="flex justify-start">
        <div className="my-2">
          <button
            onClick={generatePdf}
            className="flex items-center btn !text-gray-400 !bg-transparent border border-gray-700"
          >
            <CiExport className="mr-1" />
            Generate PDF
          </button>
        </div>
      </div>
      <div className="flex justify-end">
        <form onSubmit={handleOnSubmit}>
          {error.length > 0 && <p className="text-sm text-red-300 my-1">{error}</p>}
          <div className="flex justify-end">
            <label className="hidden" htmlFor="search">
              Search
            </label>
            <input
              onChange={handleOnChange}
              value={search}
              id="search"
              name="search"
              placeholder="Enter A Name..."
              className="h-10 border rounded border-gray-700 bg-transparent"
            />
            <button className="ml-2 btn !bg-blue-400">Search</button>
          </div>
        </form>
      </div>
      <div className="flex justify-center">
        <h3 className="text-gray-400 text-xl">
          Total Revenue: <span className="text-green-400 ml-1">+${totalRevenue}</span>
        </h3>
      </div>
      <div className="my-6 overflow-x-auto">
        <table className="border-collapse w-full rounded">
          <thead>
            <tr>
              {Object.entries(headerMapping).map(([key, val]) => {
                return <TableHeader key={key} heading={val} />;
              })}
            </tr>
          </thead>
          <tbody>
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
          </tbody>
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
