import { useDispatch, useSelector } from 'react-redux';
import { FaRegEnvelope } from 'react-icons/fa';
import { NavLink, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { AiOutlineLock, AiOutlineUser } from 'react-icons/ai';
import RoleQuestionField from './RoleQuestionField';
import { TRootState, updateSignUpField, useSignUpMutation, clearSignUpForm } from '../../state/store';
import FormInputField from '../Form/FormInputField';
import FormInputPasswordField from '../Form/FormInputPasswordField';
import { ISignUpForm } from '../../interfaces';
import Spinner from '../Shared/Spinner';

const Form = () => {
  const [signUp, results] = useSignUpMutation();
  const [error, setError] = useState('');
  const form = useSelector((store: TRootState) => store.signup);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    if (results.isSuccess) {
      dispatch(clearSignUpForm());
      navigate('/signin');
    }
  }, [results.isSuccess]);

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    dispatch(updateSignUpField({ name, value, attribute }));
  };

  const clearErrors = (form: ISignUpForm) => {
    for (const [key, _] of Object.entries(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: ISignUpForm) => {
    let isValidated = true;
    for (const key of Object.keys(form)) {
      if (!form[key as keyof ISignUpForm].value) {
        handleUpdateField(key, 'Field must not be empty', 'error');
        isValidated = false;
      }
    }
    return isValidated;
  };

  const applyServerErrors = <T extends object>(data: T) => {
    for (const [key, val] of Object.entries(data)) {
      if (key === 'message') {
        setError(val);
        return;
      }
      handleUpdateField(key, val, 'error');
    }
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    clearErrors(form);
    setError('');
    if (!validateForm(form)) {
      return;
    }
    signUp(form)
      .unwrap()
      .catch((err) => {
        if (err.status === 400) {
          applyServerErrors(err.data);
        }
      });
  };

  return (
    <div className="bg-stone-950 lg:w-3/6 md:mx-auto  md:w-3/4 sm:w-full rounded-r p-4">
      <form onSubmit={handleSubmit} className="max-w-xl mx-auto">
        <header className="text-center">
          <h1 className="text-2xl font-display text-green-400">Sign up for OverWatch</h1>
          <p>Create a free account or log in</p>
        </header>
        {error.length > 0 && (
          <div className="flex justify-center">
            <p className="text-red-400">{error}</p>
          </div>
        )}
        <section className="flex flex-col justify-center min-h-40">
          <div className="my-4 rounded p-2">
            <RoleQuestionField />
          </div>
          <div className="my-4">
            <FormInputField
              handleUpdateField={handleUpdateField}
              name={form.firstName.name}
              value={form.firstName.value}
              error={form.firstName.error}
              type={form.firstName.type}
              label="First Name"
              id="firstName"
              errorField="First name"
              placeholder="Enter your first name"
              icon={<AiOutlineUser />}
            />
          </div>
          <div className="my-4">
            <FormInputField
              handleUpdateField={handleUpdateField}
              name={form.lastName.name}
              value={form.lastName.value}
              error={form.lastName.error}
              type={form.lastName.type}
              label="Last Name"
              id="lastName"
              errorField="Last name"
              placeholder="Enter your last name"
              icon={<AiOutlineUser />}
            />
          </div>
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
              placeholder="Create password"
              visibility={true}
              min={5}
              max={50}
              icon={<AiOutlineLock />}
            />
          </div>
          <div className="my-4">
            <FormInputPasswordField
              handleUpdateField={handleUpdateField}
              name={form.confirmPassword.name}
              value={form.confirmPassword.value}
              error={form.confirmPassword.error}
              type={form.confirmPassword.type}
              label="Confirm Password"
              id="confirmPassword"
              errorField="Confirm password"
              placeholder="Confirm your password"
              min={5}
              max={50}
              icon={<AiOutlineLock />}
            />
          </div>
        </section>
        <div className="flex justify-center items-center my-4">
          <p className="mr-1 text-sm">Already have an account? Sign in</p>
          <NavLink className="text-green-400 font-bold" to="/signup">
            here
          </NavLink>
        </div>
        {results.isLoading ? (
          <div className="my-2">
            <Spinner message="Creating account..." />
          </div>
        ) : (
          <div className="flex justify-center w-full my-2">
            <button className="btn w-full" type="submit">
              Sign up
            </button>
          </div>
        )}
      </form>
    </div>
  );
};

export default Form;
