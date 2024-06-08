import { useSelector } from 'react-redux';
import { nanoid } from 'nanoid';
import { TRootState } from '../../state/store';
import NavigationLink from './NavigationLink';

const Navigation = () => {
  const { user } = useSelector((store: TRootState) => store.user);

  const billingLinks = [{ path: `/settings/${user.slug}/billing`, name: 'Billing & Payments' }];

  const userLinks =
    user.role === 'REVIEWER'
      ? [
          { path: `/settings/${user.slug}/connects`, name: 'Connects' },
          { path: `/settings/${user.slug}/contact-info`, name: 'Contact Info' },
          { path: `/settings/${user.slug}/profile`, name: 'My Profile' },
          { path: `/settings/${user.slug}/profile/edit`, name: 'Edit Profile' },
          { path: `/settings/${user.slug}/profile/settings`, name: 'Profile Settings' },
          { path: `/settings/${user.slug}/testimonials/`, name: 'Add Testimonial' },
          { path: `/settings/${user.slug}/pay`, name: 'Get Paid' },
          { path: `/settings/${user.slug}/teams`, name: 'My Teams' },
          { path: `/settings/${user.slug}/security`, name: 'Password & Security' },
          { path: `/settings/${user.slug}/notifications/settings`, name: 'Notification Settings' },
        ]
      : [
          { path: `/settings/${user.slug}/connects`, name: 'Connects' },
          { path: `/settings/${user.slug}/contact-info`, name: 'Contact Info' },
          { path: `/settings/${user.slug}/profile`, name: 'My Profile' },
          { path: `/settings/${user.slug}/profile/edit`, name: 'Edit Profile' },
          { path: `/settings/${user.slug}/profile/settings`, name: 'Profile Settings' },
          { path: `/settings/${user.slug}/security`, name: 'Password & Security' },
          { path: `/settings/${user.slug}/notifications/settings`, name: 'Notification Settings' },
        ];

  return (
    <div className="min-h-full min-w-[250px] p-4">
      <h2 className="text-4xl text-gray-400">Settings</h2>
      <section className="my-4">
        <h3 className="text-gray-400 text-2xl">Billing</h3>
        <ul>
          {billingLinks.map((billingLink) => {
            return <NavigationLink key={nanoid()} data={billingLink} />;
          })}
        </ul>
      </section>
      <section className="my-4">
        <h3 className="text-gray-400 text-2xl">User Settings</h3>
        <ul>
          {userLinks.map((userLink) => {
            return <NavigationLink key={nanoid()} data={userLink} />;
          })}
        </ul>
      </section>
    </div>
  );
};

export default Navigation;
