import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
  TRootState,
  setTeamInvitations,
  setTeamPagination,
  useFetchTeamInvitationsQuery,
  useLazyFetchTeamInvitationsQuery,
} from '../../../state/store';
import InvitationItem from './InvitationItem';

const InvitationList = () => {
  const dispatch = useDispatch();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const { teamInvitations, teamInvitationPagination } = useSelector((store: TRootState) => store.team);
  const [fetchTeamInvitations] = useLazyFetchTeamInvitationsQuery();
  const { data, error, isLoading } = useFetchTeamInvitationsQuery(
    {
      token,
      page: -1,
      pageSize: teamInvitationPagination.pageSize,
      direction: teamInvitationPagination.direction,
      userId: user.id,
    },
    { skip: !token }
  );

  useEffect(() => {
    if (data) {
      dispatch(setTeamInvitations({ invitations: [], reset: true }));
      const { direction, items, page, pageSize, totalElements, totalPages } = data.data;
      const newPagination = {
        ...teamInvitationPagination,
        direction,
        page,
        totalElements,
        totalPages,
        pageSize,
      };

      dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'invitation' }));
      dispatch(setTeamInvitations({ invitations: items, reset: false }));
    }
  }, [data, dispatch]);

  const paginateTeamInvitations = () => {
    const payload = {
      token,
      page: teamInvitationPagination.page,
      pageSize: teamInvitationPagination.pageSize,
      direction: teamInvitationPagination.direction,
      userId: user.id,
    };
    fetchTeamInvitations(payload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        const newPagination = {
          ...teamInvitationPagination,
          direction,
          page,
          totalElements,
          totalPages,
          pageSize,
        };

        dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'invitation' }));
        dispatch(setTeamInvitations({ invitations: items, reset: false }));
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
      {teamInvitationPagination.page < teamInvitationPagination.totalPages - 1 && (
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
