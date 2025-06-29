import { useState } from 'react';
import dayjs from 'dayjs';
import { CgUnblock } from 'react-icons/cg';
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(relativeTime);

import { IBlockedUser } from '../../../interfaces';
import Avatar from '../../Shared/Avatar';
import { useSelector } from 'react-redux';
import { TRootState, useDeleteBlockedUserMutation } from '../../../state/store';

export interface IBlockedUserItemProps {
  blockedUser: IBlockedUser;
}

const BlockedUserItem = ({ blockedUser }: IBlockedUserItemProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteBlockedUserMut] = useDeleteBlockedUserMutation();
  const [isToolTipShowing, setIsToolTipShowing] = useState(false);

  const handleOnMouseEnter = () => setIsToolTipShowing(true);

  const handleOnMouseLeave = () => setIsToolTipShowing(false);

  const handleOnClick = () => {
    deleteBlockedUserMut({ blockUserId: blockedUser.id, token })
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div data-testid="blocked-user-item" className="my-4">
      <div className="flex items-center">
        <div>
          <Avatar height="h-9" width="w-9" initials="?.?" avatarUrl={blockedUser.avatarUrl} />
        </div>
        <div className="ml-2">
          <div className="flex items-center justify-between">
            <p>{blockedUser.fullName}</p>
            <div className="relative">
              <div
                data-testid="blocked-user-tooltip"
                className="cursor-pointer"
                onClick={handleOnClick}
                onMouseEnter={handleOnMouseEnter}
                onMouseLeave={handleOnMouseLeave}
              >
                <CgUnblock className="text-xl text-green-400" />
              </div>
              {isToolTipShowing && (
                <div className="absolute -top-8 left-0 bg-stone-900 p-2 rounded">
                  <p className="text-xs">Unblock</p>
                </div>
              )}
            </div>
          </div>
          <p className="text-sm">
            blocked on{' '}
            <span className="text-xs font-bold text-yellow-500">
              {dayjs(blockedUser.createdAt).format('MM/DD/YYYY')}
            </span>
            <span className="text-xs ml-2">({dayjs(blockedUser.createdAt).fromNow()})</span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default BlockedUserItem;
