import { AiOutlineSearch } from 'react-icons/ai';
import { useCallback, useEffect, useState } from 'react';
import { debounce } from 'lodash';

import { IConnection } from '../../../interfaces';
import { useLazyFetchSearchConnectionsQuery } from '../../../state/store';
import Avatar from '../../Shared/Avatar';
import { retrieveTokens } from '../../../util';
import Spinner from '../../Shared/Spinner';

export interface IConnectionSearchProps {
  changeConnection: (connection: IConnection) => void;
  connectionId: number;
}

const paginationState = {
  page: -1,
  pageSize: 2,
  totalPages: 0,
  direction: 'next',
  totalElements: 0,
};

const ConnectionSearch = ({ changeConnection, connectionId }: IConnectionSearchProps) => {
  const [pag, setPag] = useState(paginationState);
  const [inputValue, setInputValue] = useState<string>('');
  const [searches, setSearches] = useState<IConnection[]>([]);
  const [fetchSearchConnections, { isLoading }] = useLazyFetchSearchConnectionsQuery();

  useEffect(() => {
    setSearches([]);
  }, [connectionId]);

  const preformDebounce = debounce((query) => {
    applySearch(query, false);
  }, 250);

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const searchValue = e.target.value;
    if (searchValue.length === 0) {
      setSearches([]);
      setPag({ page: 0, pageSize: 2, totalPages: 0, direction: 'next', totalElements: 0 });
    }
    debouncedSearch(searchValue);
    setInputValue(searchValue);
  };

  const debouncedSearch = useCallback((query: string) => preformDebounce(query), []);

  const applySearch = (query: string, paginate: boolean) => {
    const pageNum = paginate ? pag.page : -1;
    if (query.trim().length === 0) return;
    const token = retrieveTokens()?.token;
    fetchSearchConnections({ token, query, page: pageNum, pageSize: pag.pageSize, direction: pag.direction })
      .unwrap()
      .then((res) => {
        const { items, page, pageSize, totalPages, direction, totalElements } = res.data;
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          totalElements,
          totalPages,
          direction,
        }));
        if (paginate) {
          setSearches((prevState) => [...prevState, ...items]);
        } else {
          setSearches(items);
        }
      })
      .catch((err) => console.log(err));
  };

  const handleOnClick = (searchResult: IConnection) => {
    changeConnection(searchResult);
    setInputValue('');
  };

  return (
    <div className="mt-4 mb-32">
      <div className="relative">
        <input
          value={inputValue}
          onChange={handleOnChange}
          placeholder="Search"
          className=" pl-7 w-full h-9 bg-transparent rounded border border-gray-800"
        />
        <AiOutlineSearch className="absolute top-2 left-2 text-xl" />
      </div>
      {isLoading && (
        <div className="my-2 flex justify-center">
          <Spinner message="Loading searches..." />
        </div>
      )}
      {searches.length > 0 && (
        <div className="my-4 bg-stone-950 p-4">
          {searches.map((search) => {
            return (
              <div className="cursor-pointer my-2" onClick={() => handleOnClick(search)} key={search.id}>
                <div className="flex items-center">
                  <Avatar initials="?.?" avatarUrl={search.avatarUrl} width="w-6" height="h-6" />
                  <p className="text-sm text-gray-400">
                    {search.firstName} {search.lastName}
                  </p>
                </div>
              </div>
            );
          })}
          {pag.page < pag.totalPages - 1 && (
            <div onClick={() => applySearch(inputValue, true)} className="my-2 flex justify-center">
              <button className="text-center text-xs">See more...</button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default ConnectionSearch;
