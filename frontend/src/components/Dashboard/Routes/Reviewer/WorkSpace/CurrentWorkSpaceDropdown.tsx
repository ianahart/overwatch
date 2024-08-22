import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import ClickAway from '../../../../Shared/ClickAway';
import { IPaginationState, IWorkSpaceEntity } from '../../../../../interfaces';
import { paginationState } from '../../../../../data';
import {
  TRootState,
  setWorkSpace,
  useFetchWorkspacesQuery,
  useLazyFetchWorkspacesQuery,
} from '../../../../../state/store';

interface ICurrentWorkSpaceDropdownProps {
  onClickAway: () => void;
  handleSetOpen: (open: boolean) => void;
}

const CurrentWorkSpaceDropdown = ({ onClickAway, handleSetOpen }: ICurrentWorkSpaceDropdownProps) => {
  const dispatch = useDispatch();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [pagination, setPagination] = useState<IPaginationState>(paginationState);
  const [workSpaces, setWorkSpaces] = useState<IWorkSpaceEntity[]>([]);
  const [fetchWorkSpaces] = useLazyFetchWorkspacesQuery();
  const { data } = useFetchWorkspacesQuery({
    userId: user.id,
    token,
    page: -1,
    pageSize: 3,
    direction: 'next',
  });

  useEffect(() => {
    if (data !== undefined) {
      const { items, page, pageSize, totalPages, direction, totalElements } = data.data;
      setPagination((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setWorkSpaces(items);
    }
  }, [data]);

  const paginateWorkSpaces = async (dir: string) => {
    try {
      const response = await fetchWorkSpaces({
        userId: user.id,
        token,
        page: pagination.page,
        pageSize: pagination.pageSize,
        direction: dir,
      }).unwrap();

      const { items, page, pageSize, totalPages, direction, totalElements } = response.data;
      setPagination((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setWorkSpaces((prevState) => [...prevState, ...items]);
    } catch (err) {
      console.log(err);
    }
  };

  const selectWorkSpace = (entity: IWorkSpaceEntity) => {
    dispatch(setWorkSpace(entity));
    handleSetOpen(false);
  };

  return (
    <div className="absolute min-w-[250px] top-10 left-0 z-10 border border-gray-800 rounded bg-stone-950 p-2  overflow-y-auto h-32">
      <ClickAway onClickAway={onClickAway}>
        {workSpaces.map((ws) => {
          return (
            <div
              onClick={() => selectWorkSpace(ws)}
              key={ws.id}
              className="my-2 flex items-center cursor-pointer hover:bg-gray-900"
            >
              <div
                style={{ backgroundColor: ws.backgroundColor ? ws.backgroundColor : 'gray' }}
                className="w-6 h-6 rounded"
              ></div>
              <p className="ml-3">{ws.title}</p>
            </div>
          );
        })}
        {pagination.page < pagination.totalPages - 1 && (
          <div className="flex justify-center">
            <button onClick={() => paginateWorkSpaces('next')} className="mx-auto text-sm">
              See more
            </button>
          </div>
        )}
      </ClickAway>
    </div>
  );
};
export default CurrentWorkSpaceDropdown;
