import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { AiOutlineTeam } from 'react-icons/ai';
import { BsTrash } from 'react-icons/bs';
import ToolTip from '../Shared/ToolTip';

import {
  TRootState,
  useFetchTeamMemberTeamsQuery,
  setTeamPagination,
  useLazyFetchTeamMemberTeamsQuery,
  setCurrentTeam,
  setTeamMemberTeams,
  clearTeamMemberTeams,
  useDeleteTeamMemberMutation,
} from '../../state/store';

const TeamMemberTeams = () => {
  const { teamMemberTeams, teamMemberTeamPagination, currentTeam } = useSelector((store: TRootState) => store.team);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [fetchTeamMemberTeams] = useLazyFetchTeamMemberTeamsQuery();
  const [deleteTeamMember] = useDeleteTeamMemberMutation();
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

      dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'memberTeam' }));
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

        dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'memberTeam' }));
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

  const handleDeleteTeamMember = (teamMemberId: number): void => {
    deleteTeamMember({ token, teamMemberId })
      .unwrap()
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <h3>Teams You Belong to ({totalTeamMemberTeams})</h3>
      <div className="h-20 overflow-y-auto">
        {teamMemberTeams.map((teamMembersTeam, index) => {
          return (
            <div key={teamMembersTeam.id} className="flex items-center justify-between">
              <div onClick={() => changeCurrentTeam(teamMembersTeam.teamId)} className="my-1 cursor-pointer">
                <p className="hover:text-gray-500 flex items-center">
                  <AiOutlineTeam className="mr-1 text-blue-400" />
                  {teamMembersTeam.teamName}
                </p>
              </div>
              <div onClick={() => handleDeleteTeamMember(teamMembersTeam.id)} className="mr-10 cursor-pointer">
                {teamMemberTeams.length - 1 !== index ? (
                  <ToolTip message="Leave">
                    <BsTrash />
                  </ToolTip>
                ) : (
                  <BsTrash />
                )}
              </div>
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
