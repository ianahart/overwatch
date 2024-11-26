import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import { Link } from 'react-router-dom';

const Navigation = () => {
  const { user } = useSelector((store: TRootState) => store.user);
  const { currentTeam } = useSelector((store: TRootState) => store.team);

  const links = [
    { id: 1, text: 'Invitations', href: `/settings/${user.slug}/teams/${currentTeam}/invitations` },
    { id: 2, text: 'Add Team Member', href: `/settings/${user.slug}/teams/${currentTeam}/add` },
    { id: 3, text: 'Messages', href: `/settings/${user.slug}/teams/${currentTeam}/messages` },
    { id: 4, text: 'Posts', href: `/settings/${user.slug}/teams/${currentTeam}/posts` },
  ];

  return (
    <>
      {currentTeam !== 0 ? (
        <div className="flex items-center justify-center">
          {links.map((link) => {
            return (
              <Link key={link.id} to={link.href} className="mx-2 text-gray-400 font-bold">
                {link.text}
              </Link>
            );
          })}
        </div>
      ) : (
        <div className="flex justify-center">
          <h3 className="text-gray-400 text-xl">Please select a group.</h3>
        </div>
      )}
    </>
  );
};

export default Navigation;
