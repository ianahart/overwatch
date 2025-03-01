import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import TeamMemberList from './TeamMemberList';
import { TRootState, useFetchTeamQuery } from '../../../state/store';
import { ITeam } from '../../../interfaces';
import { teamState } from '../../../data';
import TeamPinnedMessage from './TeamPinnedMessage';

const TeamMember = () => {
  const { token } = useSelector((store: TRootState) => store.user);
  const params = useParams();
  const teamId = Number(params.teamId);
  const { data } = useFetchTeamQuery({ teamId, token }, { skip: !token || !teamId });
  const [team, setTeam] = useState<ITeam>(teamState);

  useEffect(() => {
    if (data !== undefined) {
      setTeam(data.data);
    }
  }, [data]);

  return (
    <div>
      <div>
        {team && (
          <div className="my-12">
            <TeamPinnedMessage team={team} />
          </div>
        )}
        <div className="my-8">
          <TeamMemberList />
        </div>
      </div>
    </div>
  );
};

export default TeamMember;
