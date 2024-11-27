import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { TRootState, useFetchTeamInvitationsQuery, useLazyFetchTeamInvitationsQuery } from '../../../state/store';
import { IPaginationState, ITeamInvitiation } from '../../../interfaces';
import InvitationItem from './InvitationItem';

const InvitationList = () => {
  const paginationState = {
    page: 0,
    pageSize: 5,
    totalElements: 0,
    totalPages: 0,
    direction: 'next',
  };

  const dispatch = useDispatch();
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [fetchTeamsQuery] = useLazyFetchTeamInvitationsQuery();
  const [teamInvitations, setTeamInivitations] = useState<ITeamInvitiation[]>([]);
  const { data, error, isLoading } = useFetchTeamInvitationsQuery(
    {
      token,
      page: -1,
      pageSize: pag.pageSize,
      direction: pag.direction,
      userId: user.id,
    },
    { skip: !token }
  );

  useEffect(() => {
    if (data) {
      const { direction, items, page, pageSize, totalElements, totalPages } = data.data;
      const newPagination = {
        ...pag,
        direction,
        page,
        totalElements,
        totalPages,
        pageSize,
      };

      setPag(newPagination);
      setTeamInivitations(items);
    }
  }, [data, dispatch]);

  const paginateTeamInvitations = () => {
    const payload = {
      token,
      page: pag.page,
      pageSize: pag.pageSize,
      direction: pag.direction,
      userId: user.id,
    };
    fetchTeamsQuery(payload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        const newPagination = {
          ...pag,
          direction,
          page,
          totalElements,
          totalPages,
          pageSize,
        };

        setPag(newPagination);
        setTeamInivitations((prev) => [...prev, ...items]);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <div className="my-8 max-w-[600px] w-full">
        <div className="text-xl">Your Team Invitations</div>
        {teamInvitations.map((teamInvitation) => {
          return <InvitationItem key={teamInvitation.id} teamInvitation={teamInvitation} />;
        })}
      </div>
      {pag.page < pag.totalPages - 1 && (
        <button onClick={paginateTeamInvitations} className="mx-2">
          Load more...
        </button>
      )}

      {isLoading && <p>Loading...</p>}
      {error && <p>Error loading teams</p>}
    </div>
  );
};

export default InvitationList;
