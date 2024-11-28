import { useDispatch, useSelector } from 'react-redux';
import { ITeamInvitiation } from '../../../interfaces';
import { initializeName } from '../../../util';
import Avatar from '../../Shared/Avatar';
import {
  TRootState,
  removeTeamInvitation,
  useDeleteTeamInvitationMutation,
  useLazyFetchTeamMemberTeamsQuery,
  useUpdateTeamInvitationMutation,
  setTeamMemberTeams,
  clearTeamPagination,
  setTeamPagination,
} from '../../../state/store';
import dayjs from 'dayjs';

export interface IIInvitationItemProps {
  teamInvitation: ITeamInvitiation;
}

const InvitationItem = ({ teamInvitation }: IIInvitationItemProps) => {
  const dispatch = useDispatch();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const { teamMemberTeamPagination } = useSelector((store: TRootState) => store.team);
  const [deleteTeamInvitation] = useDeleteTeamInvitationMutation();
  const [updateTeamInvitation] = useUpdateTeamInvitationMutation();
  const [fetchTeamMemberTeams] = useLazyFetchTeamMemberTeamsQuery();
  const [firstName, lastName] = teamInvitation.senderFullName.split(' ');

  const handleOnAcceptTeamInvitation = async (): Promise<void> => {
    try {
      const payload = {
        token,
        teamInvitationId: teamInvitation.id,
        teamId: teamInvitation.teamId,
        userId: teamInvitation.receiverId,
      };

      await updateTeamInvitation(payload).unwrap();
      dispatch(removeTeamInvitation(teamInvitation.id));
      dispatch(clearTeamPagination('member'));

      const refetchPayload = { ...teamMemberTeamPagination, userId: user.id, token };
      const fetchResponse = await fetchTeamMemberTeams(refetchPayload);
      if (fetchResponse.isSuccess) {
        const { direction, items, page, pageSize, totalElements, totalPages } = fetchResponse.data.data;
        const newPagination = {
          ...teamMemberTeamPagination,
          direction,
          page,
          totalElements,
          totalPages,
          pageSize,
        };
        dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'member' }));
        dispatch(setTeamMemberTeams({ team: items, reset: true }));
      }
    } catch (err) {
      console.log(err);
    }
  };

  const handleOnIgnoreTeamInvitation = (): void => {
    const payload = { token, teamInvitationId: teamInvitation.id };
    deleteTeamInvitation(payload)
      .unwrap()
      .then(() => {
        dispatch(removeTeamInvitation(teamInvitation.id));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="my-4">
      <div className="flex items-center">
        <Avatar
          width="w-12"
          height="h-12"
          avatarUrl={teamInvitation.senderAvatarUrl}
          initials={initializeName(firstName, lastName)}
        />
        <div>
          <h3 className="text-sm font-bold ml-2">{teamInvitation.senderFullName}</h3>

          <small className="ml-2">{dayjs(teamInvitation.createdAt).format('MM/DD/YYYY')}</small>
          <p className="text-sm ml-2">
            has sent you a team inivitation to join a team called{' '}
            <span className="text-sm font-bold">{teamInvitation.teamName}</span>
          </p>
          <div className="my-2 ml-2">
            <button onClick={handleOnAcceptTeamInvitation} className="btn !p-1 !h-7 mr-2">
              Accept
            </button>
            <button onClick={handleOnIgnoreTeamInvitation} className="btn !p-1 !h-7 ml-2 !bg-red-300 !text-white">
              Ignore
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default InvitationItem;
