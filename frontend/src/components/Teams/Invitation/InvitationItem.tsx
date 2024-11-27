import { useSelector } from 'react-redux';
import { ITeamInvitiation } from '../../../interfaces';
import { initializeName } from '../../../util';
import Avatar from '../../Shared/Avatar';
import { TRootState, useDeleteTeamInvitationMutation, useUpdateTeamInvitationMutation } from '../../../state/store';
import dayjs from 'dayjs';

export interface IIInvitationItemProps {
  teamInvitation: ITeamInvitiation;
}

const InvitationItem = ({ teamInvitation }: IIInvitationItemProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deleteTeamInvitation] = useDeleteTeamInvitationMutation();
  const [updateTeamInvitation] = useUpdateTeamInvitationMutation();
  const [firstName, lastName] = teamInvitation.senderFullName.split(' ');

  const handleOnAcceptTeamInvitation = (): void => {
    const payload = {
      token,
      teamInvitationId: teamInvitation.id,
      teamId: teamInvitation.teamId,
      userId: teamInvitation.receiverId,
    };
    updateTeamInvitation(payload)
      .unwrap()
      .then(() => {})
      .catch((err) => {
        console.log(err);
      });
  };

  const handleOnIgnoreTeamInvitation = (): void => {
    const payload = { token, teamInvitationId: teamInvitation.id };
    deleteTeamInvitation(payload)
      .unwrap()
      .then(() => {})
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
