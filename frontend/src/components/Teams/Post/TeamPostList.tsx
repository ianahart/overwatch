import { useEffect, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
  TRootState,
  setTeamPagination,
  setTeamPosts,
  useFetchTeamPostsQuery,
  useLazyFetchTeamPostsQuery,
} from '../../../state/store';
import { useParams } from 'react-router-dom';
import TeamPostItem from './TeamPostItem';
import { connectWebSocket, disconnectWebSocket } from '../../../util/WebSocketService';

const TeamPostList = () => {
  const dispatch = useDispatch();
  const params = useParams();
  const shouldRun = useRef(true);
  const teamId = Number.parseInt(params.teamId as string);
  const { token } = useSelector((store: TRootState) => store.user);
  const { teamPosts, teamPostPagination } = useSelector((store: TRootState) => store.team);
  const [fetchTeamInvitations] = useLazyFetchTeamPostsQuery();
  const { data, error, isLoading } = useFetchTeamPostsQuery(
    {
      token,
      page: -1,
      pageSize: teamPostPagination.pageSize,
      direction: teamPostPagination.direction,
      teamId,
    },
    { skip: !token || !teamId }
  );

  useEffect(() => {
    if (data) {
      dispatch(setTeamPosts({ posts: [], reset: true }));
      const { direction, items, page, pageSize, totalElements, totalPages } = data.data;
      const newPagination = {
        ...teamPostPagination,
        direction,
        page,
        totalElements,
        totalPages,
        pageSize,
      };

      dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'post' }));
      dispatch(setTeamPosts({ posts: items, reset: false }));
    }
  }, [data, dispatch]);

  const onConnected = () => {
    console.log('WebSocket connected');
  };

  const onError = (err: any) => {
    console.error('WebSocket error:', err);
  };

  useEffect(() => {
    if (shouldRun.current) {
      shouldRun.current = false;
      connectWebSocket(onConnected, onError);
    }
    return () => {
      disconnectWebSocket();
    };
  }, []);

  const paginateTeamInvitations = () => {
    const payload = {
      token,
      page: teamPostPagination.page,
      pageSize: teamPostPagination.pageSize,
      direction: teamPostPagination.direction,
      teamId,
    };
    fetchTeamInvitations(payload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        const newPagination = {
          ...teamPostPagination,
          direction,
          page,
          totalElements,
          totalPages,
          pageSize,
        };

        dispatch(setTeamPagination({ pagination: newPagination, paginationType: 'post' }));
        dispatch(setTeamPosts({ posts: items, reset: false }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <div className="my-8 max-w-[600px] w-full">
        <div className="text-center">
          <h3 className="text-xl">Team Posts</h3>
          <p>Here you can share code snippets.</p>
        </div>
        {teamPosts.map((teamPost) => {
          return <TeamPostItem key={teamPost.id} teamPost={teamPost} />;
        })}
      </div>
      {teamPostPagination.page < teamPostPagination.totalPages - 1 && (
        <button onClick={paginateTeamInvitations} className="mx-2">
          Load more...
        </button>
      )}

      {isLoading && <p>Loading...</p>}
      {error && <p>Error loading teams</p>}
    </div>
  );
};

export default TeamPostList;
