import { FaRegEnvelope } from 'react-icons/fa';
import FormInputField from '../Form/FormInputField';
import { useState } from 'react';
import { IForgotPasswordForm } from '../../interfaces';
import { NavLink } from 'react-router-dom';
import { useForgotPasswordMutation } from '../../state/store';

const formState = { email: { name: 'email', value: '', type: 'email', error: '' } };

const Form = () => {
  const [forgotPassword, results] = useForgotPasswordMutation();
  const [forgotPasswordForm, setForgotPasswordForm] = useState(formState);

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    setForgotPasswordForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof IForgotPasswordForm], [attribute]: value },
    }));
  };

  const clearErrors = (form: IForgotPasswordForm) => {
    for (const key of Object.keys(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: IForgotPasswordForm) => {
    let isValidated = true;

    for (const val of Object.values(form)) {
      const { value, error } = val;
      if (value.trim().length === 0 || error.length) {
        isValidated = false;
      }
    }
    return isValidated;
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    clearErrors(forgotPasswordForm);
    const isValidated = validateForm(forgotPasswordForm);
    if (!isValidated) {
      return;
    }
    forgotPassword(forgotPasswordForm);
  };

  return (
    <section className="min-h-screen pt-40">
      <section className="bg-stone-950 mx-auto rounded p-4 shadow-md md:max-w-[500px] max-w-full">
        <h1 className="text-gray-400 text-2xl my-8">Reset your password</h1>
        <p className="mb-8 text-sm">
          Enter the email address associated with your account and we'll send you a link to reset your password.
        </p>
        <form onSubmit={handleSubmit}>
          <div className="my-6">
            <FormInputField
              handleUpdateField={handleUpdateField}
              name={forgotPasswordForm.email.name}
              value={forgotPasswordForm.email.value}
              error={forgotPasswordForm.email.error}
              type={forgotPasswordForm.email.type}
              label="Email"
              id="email"
              errorField="Email"
              placeholder="Enter your email"
              icon={<FaRegEnvelope />}
            />
          </div>
          <div className="flex flex-col items-center justify-center">
            <button className="btn w-full" type="submit">
              Continue
            </button>
            <div className="my-4">
              <NavLink to="/signin" className="text-green-400">
                Return to sign in
              </NavLink>
            </div>
          </div>
        </form>
      </section>
    </section>
  );
};

export default Form;
