import { FaRegEnvelope } from 'react-icons/fa';
import { AiOutlineLock } from 'react-icons/ai';
import { useDispatch, useSelector } from 'react-redux';
import Spinner from '../Shared/Spinner';
import FormInputField from '../Form/FormInputField';
import FormInputPasswordField from '../Form/FormInputPasswordField';
import { ISignInForm } from '../../interfaces';
import { useEffect, useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import {
  TRootState,
  updateSignInField,
  useSignInMutation,
  clearSignInForm,
  updateUserAndTokens,
} from '../../state/store';
const Form = () => {
  const [signIn, results] = useSignInMutation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const form = useSelector((store: TRootState) => store.signin);
  const [error, setError] = useState('');

  useEffect(() => {
    if (results.isSuccess) {
      dispatch(clearSignInForm());
      navigate('/');
    }
  }, [results.isSuccess]);

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    dispatch(updateSignInField({ name, value, attribute }));
  };

  const clearErrors = (form: ISignInForm) => {
    for (const key of Object.keys(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: ISignInForm) => {
    let isValidated = true;
    for (const key of Object.keys(form)) {
      if (!form[key as keyof ISignInForm].value) {
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

    signIn(form)
      .unwrap()
      .then(({ user, token, refreshToken }) => {
        const tokens = { token, refreshToken };
        dispatch(updateUserAndTokens({ user, tokens }));
      })
      .catch(({ data }) => {
        setError(data.message);
      });
  };

  return (
    <div className="bg-slate-900 lg:w-3/6 md:w-3/4 md:mx-auto min-h-96 rounded-r p-4 flex-col flex items-center justify-center">
      <form onSubmit={handleSubmit} className="max-w-xl mx-auto">
        <header className="text-center">
          <h1 className="text-2xl font-display text-green-400">Sign in to OverWatch</h1>
          <p className="mt-2">
            Whether you're looking to get a code review or you are reviewing code, dive into overwatch to stay connected
          </p>
        </header>
        {error && (
          <div className="flex justify-center my-2">
            <p className="text-red-400">{error}</p>
          </div>
        )}
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
        <div className="flex justify-center items-center my-4">
          <p className="mr-1 text-sm">Don't have an account? Sign up</p>
          <NavLink className="text-green-400 font-bold" to="/signup">
            here
          </NavLink>
        </div>
        {results.isLoading ? (
          <div className="my-2">
            <Spinner message="Signing in..." />
          </div>
        ) : (
          <div className="flex justify-center w-full my-2">
            <button className="btn w-full" type="submit">
              Sign in
            </button>
          </div>
        )}
      </form>
    </div>
  );
};

export default Form;
