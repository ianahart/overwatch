import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
  TRootState,
  setTeamMembers,
  setTeamPagination,
  useFetchTeamMembersQuery,
  useLazyFetchTeamMembersQuery,
} from '../../../state/store';
import { useParams } from 'react-router-dom';
import TeamMemberItem from './TeamMemberItem';

const TeamMemberList = () => {
  const dispatch = useDispatch();
  const params = useParams();
  const teamId = Number.parseInt(params.teamId as string);
  const { token } = useSelector((store: TRootState) => store.user);
  const { teamMembers, teamMemberPagination } = useSelector((store: TRootState) => store.team);
  const [fetchTeamMembers] = useLazyFetchTeamMembersQuery();
  const { data, error, isLoading } = useFetchTeamMembersQuery(
    {
      token,
      page: -1,
      pageSize: teamMemberPagination.pageSize,
      direction: teamMemberPagination.direction,
      teamId,
    },
    { skip: !token || !teamId }
  );

  useEffect(() => {
    if (data) {
      dispatch(setTeamMembers({ teamMembers: [], reset: true }));
      const { direction, items, page, pageSize, totalElements, totalPages } = data.data;
      const newPagination = {
        ...teamMemberPagination,
        direction,
        page,
        totalElements,
        totalPages,
        pageSize,
      };

      dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'teamMember' }));
      dispatch(setTeamMembers({ teamMembers: items, reset: false }));
    }
  }, [data, dispatch]);

  const paginateTeamMembers = () => {
    const payload = {
      token,
      page: teamMemberPagination.page,
      pageSize: teamMemberPagination.pageSize,
      direction: teamMemberPagination.direction,
      teamId,
    };
    fetchTeamMembers(payload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        const newPagination = {
          ...teamMemberPagination,
          direction,
          page,
          totalElements,
          totalPages,
          pageSize,
        };

        dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'teamMember' }));
        dispatch(setTeamMembers({ teamMembers: items, reset: false }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <div className="my-8 max-w-[600px] w-full">
        <div className="text-center">
          <h3 className="text-xl">Team Members</h3>
          <p>Here you can see everyone on your team.</p>
        </div>
        {data?.admin && (
          <div className="my-4">
            <TeamMemberItem isAdmin={true} teamMember={data?.admin} />
          </div>
        )}
        {teamMembers.map((teamMember) => {
          return <TeamMemberItem key={teamMember.id} isAdmin={false} teamMember={teamMember} />;
        })}
      </div>
      {teamMemberPagination.page < teamMemberPagination.totalPages - 1 && (
        <button onClick={paginateTeamMembers} className="mx-2">
          Load more...
        </button>
      )}

      {isLoading && <p>Loading...</p>}
      {error && <p>Error loading teams</p>}
    </div>
  );
};

export default TeamMemberList;
