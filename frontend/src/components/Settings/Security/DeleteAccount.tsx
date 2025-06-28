import { useCallback, useState } from 'react';
import { BsChevronDown, BsChevronLeft } from 'react-icons/bs';
import { useDispatch, useSelector } from 'react-redux';
import { FaRegEnvelope } from 'react-icons/fa';
import { AiOutlineLock } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import { TRootState, clearSetting, clearUser, useDeleteUserMutation } from '../../../state/store';
import FormInputField from '../../Form/FormInputField';
import FormInputPasswordField from '../../Form/FormInputPasswordField';
import { IDeleteAccountForm } from '../../../interfaces';
import Spinner from '../../Shared/Spinner';

const formState = {
  email: { name: 'email', error: '', value: '', type: 'email' },
  password: { name: 'password', error: '', value: '', type: 'password' },
};

const DeleteAccount = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [deleteUser, results] = useDeleteUserMutation();
  const [error, setError] = useState('');
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [form, setForm] = useState(formState);
  const [isFormShowing, setIsFormShowing] = useState(false);

  const handleUpdateField = useCallback(
    (name: string, value: string, attribute: string) => {
      setForm((prevState) => ({
        ...prevState,
        [name]: { ...prevState[name as keyof IDeleteAccountForm], [attribute]: value },
      }));
    },
    [setForm]
  );

  const toggleForm = () => {
    setIsFormShowing((prevState) => !prevState);
  };

  const clearErrors = (form: IDeleteAccountForm) => {
    for (const key of Object.keys(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: IDeleteAccountForm) => {
    let isValidated = true;
    for (const key of Object.keys(form)) {
      if (!form[key as keyof IDeleteAccountForm].value) {
        handleUpdateField(key, 'Field must not be empty', 'error');
        isValidated = false;
      }
    }
    return isValidated;
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');
    clearErrors(form);

    if (!validateForm(form)) {
      return;
    }

    if (form.email.value !== user.email) {
      return;
    }

    deleteUser({ userId: user.id, token, password: form.password.value })
      .unwrap()
      .then(() => {
        dispatch(clearUser());
      })
      .then(() => {
        dispatch(clearSetting());
        navigate('/signin');
      })
      .catch((err) => {
        setError(err.data.message);
      });
  };

  return (
    <section>
      <div className="flex justify-between">
        <p className="font-bold text-gray-400">Delete Account</p>
        <p className="md:block hidden">
          <span
            data-testid="delete-account-form-trigger"
            onClick={toggleForm}
            className="text-yellow-400 font-bold cursor-pointer"
          >
            Warning
          </span>{' '}
          This is a danger zone, becareful actions are permanent.
        </p>
        <div onClick={toggleForm}>
          {isFormShowing ? (
            <BsChevronDown className="text-gray-400 cursor-pointer" />
          ) : (
            <BsChevronLeft className="text-gray-400 cursor-pointer" />
          )}
        </div>
      </div>
      {isFormShowing && (
        <div>
          <form onSubmit={handleSubmit} className="max-w-[600px]">
            <p className="mt-8">
              Please type out{' '}
              <span className="font-bold text-gray-400">
                {user.email.split('').map((letter, index) => (
                  <span
                    key={index}
                    className={`${letter === form.email.value[index] ? 'text-green-400' : 'text-gray-400'}`}
                  >
                    {letter}
                  </span>
                ))}
              </span>{' '}
              to confirm this action.
            </p>
            <div className="my-4">
              <FormInputField
                handleUpdateField={handleUpdateField}
                name={form.email.name}
                value={form.email.value}
                error={form.email.error}
                type={form.email.type}
                label="Email"
                id="email"
                errorField="Email"
                placeholder="Enter your email"
                icon={<FaRegEnvelope />}
              />
            </div>
            <div className="my-4">
              <FormInputPasswordField
                handleUpdateField={handleUpdateField}
                name={form.password.name}
                value={form.password.value}
                error={form.password.error}
                type={form.password.type}
                label="Password"
                id="password"
                errorField="Password"
                placeholder="Enter your password"
                visibility={false}
                icon={<AiOutlineLock />}
              />
            </div>
            {error.length > 0 && <p className="text-red-400 text-sm my-4">{error}</p>}
            {results.isLoading ? (
              <Spinner message="Deleting account..." />
            ) : (
              <div className="my-4 flex justify-center">
                <button
                  type="submit"
                  className="bg-red-500 md:w-[50%] w-full text-white p-2 h-9 rounded transition ease-in-out  hover:opacity-70;
"
                >
                  Delete Account
                </button>
              </div>
            )}
          </form>
        </div>
      )}
    </section>
  );
};

export default DeleteAccount;
