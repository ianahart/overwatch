import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import {
  TRootState,
  useFetchTeamMemberTeamsQuery,
  setTeamPagination,
  useLazyFetchTeamMemberTeamsQuery,
  setCurrentTeam,
  setTeamMemberTeams,
  clearTeamMemberTeams,
} from '../../state/store';
import { AiOutlineTeam } from 'react-icons/ai';

const TeamMemberTeams = () => {
  const { teamMemberTeams, teamMemberTeamPagination, currentTeam } = useSelector((store: TRootState) => store.team);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [fetchTeamMemberTeams] = useLazyFetchTeamMemberTeamsQuery();
  const [totalTeamMemberTeams, setTotalTeamMemberTeams] = useState(0);
  const { data, error, isLoading } = useFetchTeamMemberTeamsQuery(
    {
      token,
      page: -1,
      pageSize: teamMemberTeamPagination.pageSize,
      direction: teamMemberTeamPagination.direction,
      userId: user.id,
    },
    { skip: !token }
  );

  useEffect(() => {
    if (data) {
      dispatch(clearTeamMemberTeams());
      const { direction, items, page, pageSize, totalElements, totalPages } = data.data;
      const newPagination = {
        ...teamMemberTeamPagination,
        direction,
        page,
        totalElements,
        totalPages,
        pageSize,
      };

      dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'member' }));
      setTotalTeamMemberTeams(data.totalTeamMemberTeams);
      dispatch(setTeamMemberTeams({ team: items, reset: false }));
    }
  }, [data, dispatch]);

  const paginateTeamMemberTeams = () => {
    const payload = {
      token,
      page: teamMemberTeamPagination.page,
      pageSize: teamMemberTeamPagination.pageSize,
      direction: teamMemberTeamPagination.direction,
      userId: user.id,
    };
    fetchTeamMemberTeams(payload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        const newPagination = {
          ...teamMemberTeamPagination,
          direction,
          page,
          totalElements,
          totalPages,
          pageSize,
        };

        dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'member' }));
        setTotalTeamMemberTeams(res.totalTeamMemberTeams);
        dispatch(setTeamMemberTeams({ team: items, reset: false }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    if (currentTeam !== 0) {
      navigate(`/settings/${user.slug}/teams/${currentTeam}/posts`);
    }
  }, [currentTeam]);

  const changeCurrentTeam = (teamId: number): void => {
    dispatch(setCurrentTeam(teamId));
    navigate(`/settings/${user.slug}/teams/${currentTeam}/posts`);
  };
  return (
    <div>
      <h3>Teams You Belong to ({totalTeamMemberTeams})</h3>
      <div className="h-20 overflow-y-auto">
        {teamMemberTeams.map((teamMembersTeam) => {
          return (
            <div
              onClick={() => changeCurrentTeam(teamMembersTeam.teamId)}
              className="my-1 cursor-pointer"
              key={teamMembersTeam.id}
            >
              <p className="hover:text-gray-500 flex items-center">
                <AiOutlineTeam className="mr-1 text-blue-400" />
                {teamMembersTeam.teamName}
              </p>
            </div>
          );
        })}
      </div>

      {teamMemberTeamPagination.page < teamMemberTeamPagination.totalPages - 1 && (
        <button onClick={paginateTeamMemberTeams} className="mx-2">
          Load more...
        </button>
      )}

      {isLoading && <p>Loading...</p>}
      {error && <p>Error loading teams</p>}
    </div>
  );
};

export default TeamMemberTeams;
