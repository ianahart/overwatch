import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import { IAdminBadge, IPaginationState, IGetAllBadgesResponse } from '../../../../../interfaces';
import { TRootState, useFetchBadgesQuery, useLazyFetchBadgesQuery } from '../../../../../state/store';
import BadgeListItem from './BadgeListItem';

const BadgeList = () => {
  const paginationState = {
    page: 0,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };

  const { token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [badges, setBadges] = useState<IAdminBadge[]>([]);
  const [fetchBadges] = useLazyFetchBadgesQuery();
  const { data: res } = useFetchBadgesQuery(
    {
      token,
      page: -1,
      pageSize: 10,
      direction: 'next',
    },
    { skip: !token }
  );

  const handleSetBadges = (res: IGetAllBadgesResponse): void => {
    const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
    setPag((prevState) => ({
      ...prevState,
      page,
      pageSize,
      direction,
      totalPages,
      totalElements,
    }));
    setBadges(items);
  };

  useEffect(() => {
    if (res !== undefined) {
      handleSetBadges(res);
    }
  }, [res]);

  const paginateBadges = (dir: string): void => {
    const payload = {
      token,
      page: pag.page,
      pageSize: pag.pageSize,
      direction: dir,
    };

    fetchBadges(payload)
      .unwrap()
      .then((res) => {
        handleSetBadges(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <>
      <ul className="flex-col flex md:flex-row flex-wrap">
        {badges.map((badge) => {
          return <BadgeListItem key={badge.id} badge={badge} />;
        })}
      </ul>
      <div className="flex items-center text-gray-400 justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateBadges('prev')} className="mx-2">
            Prev
          </button>
        )}
        <p className="mx-2">{pag.page + 1}</p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateBadges('next')} className="mx-2">
            Next
          </button>
        )}
      </div>
    </>
  );
};

export default BadgeList;
