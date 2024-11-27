import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import {
  TRootState,
  useFetchTeamsQuery,
  setAdminTeams,
  setTeamPagination,
  useLazyFetchTeamsQuery,
  clearAdminTeams,
  setCurrentTeam,
} from '../../state/store';
import { AiOutlineTeam } from 'react-icons/ai';

const AdminTeams = () => {
  const { adminTeams, adminTeamPagination, currentTeam } = useSelector((store: TRootState) => store.team);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [fetchTeamsQuery] = useLazyFetchTeamsQuery();
  const { data, error, isLoading } = useFetchTeamsQuery(
    {
      token,
      page: -1,
      pageSize: adminTeamPagination.pageSize,
      direction: adminTeamPagination.direction,
      userId: user.id,
    },
    { skip: !token }
  );

  useEffect(() => {
    if (data) {
      dispatch(clearAdminTeams());
      const { direction, items, page, pageSize, totalElements, totalPages } = data.data;
      const newPagination = {
        ...adminTeamPagination,
        direction,
        page,
        totalElements,
        totalPages,
        pageSize,
      };

      dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'admin' }));

      dispatch(setAdminTeams(items));
    }
  }, [data, dispatch]);

  const getAdminTeamCount = (): number => {
    if (adminTeams.length > 0) {
      return adminTeams[0].totalTeams;
    } else {
      return 0;
    }
  };

  const paginateAdminTeams = () => {
    const payload = {
      token,
      page: adminTeamPagination.page,
      pageSize: adminTeamPagination.pageSize,
      direction: adminTeamPagination.direction,
      userId: user.id,
    };
    fetchTeamsQuery(payload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        const newPagination = {
          ...adminTeamPagination,
          direction,
          page,
          totalElements,
          totalPages,
          pageSize,
        };

        dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'admin' }));

        dispatch(setAdminTeams(items));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const changeCurrentTeam = (teamId: number): void => {
    dispatch(setCurrentTeam(teamId));
    navigate(`/settings/${user.slug}/teams/${currentTeam}/invitations`);
  };
  return (
    <div>
      <h3>Teams You Manage ({getAdminTeamCount()})</h3>
      <div className="h-20 overflow-y-auto">
        {adminTeams.map((adminTeam) => {
          return (
            <div onClick={() => changeCurrentTeam(adminTeam.id)} className="my-1 cursor-pointer" key={adminTeam.id}>
              <p className="hover:text-gray-500 flex items-center">
                <AiOutlineTeam className="mr-1" />
                {adminTeam.teamName}
              </p>
            </div>
          );
        })}
      </div>

      {adminTeamPagination.page < adminTeamPagination.totalPages - 1 && (
        <button onClick={paginateAdminTeams} className="mx-2">
          Load more...
        </button>
      )}

      {isLoading && <p>Loading...</p>}
      {error && <p>Error loading teams</p>}
    </div>
  );
};

export default AdminTeams;
