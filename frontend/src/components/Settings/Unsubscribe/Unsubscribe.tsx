import { Link } from 'react-router-dom';
import { useSearchParams } from 'react-router-dom';
import { useUnsubscribeEmailSettingsQuery } from '../../../state/store';

const Unsubscribe = () => {
  const [searchParams, _] = useSearchParams();
  const email = searchParams.get('email');

  useUnsubscribeEmailSettingsQuery({ email: email as string }, { skip: !email });

  return (
    <div className="flex flex-col justify-center items-center min-h-[50vh] bg-gray-900 p-2 rounded max-w-[600px] mx-auto">
      <header className="mx-auto">
        <h2 className="text-gray-400 text-xl text-center mb-2">You have been successfully unsubscribed.</h2>
        <p>
          To resubscribe to in-app emails go to your settings and go to the notification settings page to toggle back on
          receiving emails.
        </p>
      </header>
      <div className="my-4">
        Return{' '}
        <Link className="font-bold text-blue-400" to="/">
          Home
        </Link>
      </div>
    </div>
  );
};

export default Unsubscribe;
