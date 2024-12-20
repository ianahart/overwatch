import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import { IBadge, IPaginationState, IGetAllReviewerBadgesResponse } from '../../../../../interfaces';
import { TRootState, useLazyFetchReviewerBadgesQuery } from '../../../../../state/store';
import BadgeListItem from './BadgeListItem';

const BadgeList = () => {
  const paginationState = {
    page: 0,
    pageSize: 9,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };

  const { token, user } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [badges, setBadges] = useState<IBadge[]>([]);
  const [fetchBadges] = useLazyFetchReviewerBadgesQuery();

  useEffect(() => {
    paginateBadges('next', true);
  }, []);

  const handleSetBadges = (res: IGetAllReviewerBadgesResponse): void => {
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

  const paginateBadges = (dir: string, initial: boolean = false): void => {
    const payload = {
      reviewerId: user.id,
      token,
      page: initial ? -1 : pag.page,
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
          return <BadgeListItem key={badge.id} data={badge} />;
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
