import { updateSignInField } from '../../state/store';
import { useSelector } from 'react-redux';
const Form = () => {
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
  };

  return (
    <div className="bg-slate-900 lg:w-3/6 rounded-r p-4">
      <form onSubmit={handleSubmit} className="max-w-xl mx-auto border">
        <header className="text-center">
          <h1 className="text-2xl font-display text-green-400">Sign in to OverWatch</h1>
          <p className="mt-2">
            Whether you're looking to get a code review or you are reviewing code, dive into overwatch to stay connected
          </p>
        </header>
      </form>
    </div>
  );
};

export default Form;
