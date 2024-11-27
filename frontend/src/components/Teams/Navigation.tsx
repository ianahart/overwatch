import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import { Link } from 'react-router-dom';

const Navigation = () => {
  const { user } = useSelector((store: TRootState) => store.user);
  const { currentTeam } = useSelector((store: TRootState) => store.team);

  const nonTeamLinks = [{ id: 1, text: 'View Invitations', href: `/settings/${user.slug}/teams/invitations` }];

  const teamLinks = [
    { id: 2, text: 'Add Team Member', href: `/settings/${user.slug}/teams/${currentTeam}/add` },
    { id: 3, text: 'Messages', href: `/settings/${user.slug}/teams/${currentTeam}/messages` },
    { id: 4, text: 'Posts', href: `/settings/${user.slug}/teams/${currentTeam}/posts` },
  ];

  return (
    <>
      {currentTeam !== 0 ? (
        <div className="flex items-center justify-center">
          {teamLinks.map((teamLink) => {
            return (
              <Link key={teamLink.id} to={teamLink.href} className="mx-2 text-gray-400 font-bold">
                {teamLink.text}
              </Link>
            );
          })}
        </div>
      ) : (
        <div className="flex flex-col items-center">
          <h3 className="text-gray-400 text-xl">Please select a group.</h3>
          <h3>Or</h3>
          {nonTeamLinks.map((nonTeamLink) => {
            return (
              <Link key={nonTeamLink.id} to={nonTeamLink.href} className="mx-2 text-gray-400 font-bold">
                {nonTeamLink.text}
              </Link>
            );
          })}
        </div>
      )}
    </>
  );
};

export default Navigation;
