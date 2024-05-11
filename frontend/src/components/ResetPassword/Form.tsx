import { useCallback, useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import FormOTPInputField from '../Form/FormOTPInputField';
import FormInputPasswordField from '../Form/FormInputPasswordField';
import { AiOutlineLock } from 'react-icons/ai';
import { IResetPasswordForm } from '../../interfaces';
import { useResetPasswordMutation } from '../../state/store';

const formState = {
  password: { name: 'password', error: '', value: '', type: 'password' },
  confirmPassword: { name: 'confirmPassword', error: '', value: '', type: 'password' },
};

const Form = () => {
  const OTP_INPUTS = 5;
  const [searchParams, _] = useSearchParams();
  const navigate = useNavigate();
  const [resetPassword, results] = useResetPasswordMutation();
  const [resetPasswordForm, setResetPasswordForm] = useState(formState);
  const [passCode, setPassCode] = useState<string[]>([]);
  const [passCodeError, setPassCodeError] = useState('');

  useEffect(() => {
    if (results.isSuccess) {
      navigate('/signin');
    }
  }, [results.isSuccess, navigate]);

  const handleUpdateField = useCallback(
    (name: string, value: string, attribute: string) => {
      setResetPasswordForm((prevState) => ({
        ...prevState,
        [name]: { ...prevState[name as keyof IResetPasswordForm], [attribute]: value },
      }));
    },
    [setResetPasswordForm]
  );

  const clearErrors = (form: IResetPasswordForm) => {
    setPassCodeError('');
    for (const key of Object.keys(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: IResetPasswordForm) => {
    if (passCode.length !== OTP_INPUTS) {
      setPassCodeError('Please provide the pass code that was in your email');
      return false;
    }

    for (const key of Object.keys(form)) {
      if (!form[key as keyof IResetPasswordForm].value) {
        handleUpdateField(key, `${key} is required`, 'error');
      }
    }

    return true;
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    clearErrors(resetPasswordForm);
    if (!validateForm(resetPasswordForm)) {
      return;
    }

    resetPassword({
      token: searchParams.get('token') as string,
      passCode: passCode.join(','),
      password: resetPasswordForm.password.value,
      confirmPassword: resetPasswordForm.confirmPassword.value,
    });
  };

  return (
    <section className="bg-stone-950 mx-auto rounded p-4 shadow-md md:max-w-[600px] max-w-full">
      <h1 className="text-gray-400 text-2xl my-8">Reset your password</h1>
      <p>Your new password cannot be the same as your old password.</p>
      <form onSubmit={handleSubmit}>
        <div className="my-4">
          <FormOTPInputField numOfInputs={OTP_INPUTS} passCode={passCode} setPassCode={setPassCode} />
          {passCodeError.length > 0 && <p className="my-2 text-red-400 text-sm">{passCodeError}</p>}
        </div>
        <div className="my-4">
          <FormInputPasswordField
            handleUpdateField={handleUpdateField}
            name={resetPasswordForm.password.name}
            value={resetPasswordForm.password.value}
            error={resetPasswordForm.password.error}
            type={resetPasswordForm.password.type}
            label="New Password"
            id="password"
            errorField="Password"
            placeholder="Create your new password"
            visibility={true}
            min={5}
            max={50}
            icon={<AiOutlineLock />}
          />
        </div>
        <div className="my-4">
          <FormInputPasswordField
            handleUpdateField={handleUpdateField}
            name={resetPasswordForm.confirmPassword.name}
            value={resetPasswordForm.confirmPassword.value}
            error={resetPasswordForm.confirmPassword.error}
            type={resetPasswordForm.confirmPassword.type}
            label="Confirm Password"
            id="confirmPassword"
            errorField="Confirm password"
            placeholder="Confirm your password"
            min={5}
            max={50}
            icon={<AiOutlineLock />}
          />
        </div>
        <div className="flex justify-center my-4">
          <button className="btn w-full" type="submit">
            Reset password
          </button>
        </div>
      </form>
    </section>
  );
};

export default Form;
