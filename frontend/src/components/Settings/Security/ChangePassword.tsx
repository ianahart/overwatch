import { useCallback, useEffect, useState } from 'react';
import { BsChevronDown, BsChevronLeft } from 'react-icons/bs';
import { useDispatch, useSelector } from 'react-redux';
import { AiOutlineLock } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import FormInputPasswordField from '../../Form/FormInputPasswordField';
import { IChangePasswordForm } from '../../../interfaces';
import { TRootState, clearUser, useSignOutMutation, useUpdateUserPasswordMutation } from '../../../state/store';

const formState = {
  password: { name: 'password', value: '', error: '', type: 'password' },
  curPassword: { name: 'curPassword', value: '', error: '', type: 'password' },
};

const ChangePassword = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [updateUserPassword] = useUpdateUserPasswordMutation();
  const [signOut] = useSignOutMutation();
  const { user, token, refreshToken } = useSelector((store: TRootState) => store.user);
  const [form, setForm] = useState(formState);
  const [isFormShowing, setIsFormShowing] = useState(false);
  const [isWarning, setIsWarning] = useState(false);

  useEffect(() => {
    if (form.curPassword.value.length && form.password.value.length) {
      setIsWarning(true);
    } else {
      setIsWarning(false);
    }
  }, [form.curPassword.value, form.password.value]);

  const handleUpdateField = useCallback(
    (name: string, value: string, attribute: string) => {
      setForm((prevState) => ({
        ...prevState,
        [name]: { ...prevState[name as keyof IChangePasswordForm], [attribute]: value },
      }));
    },
    [setForm]
  );

  const toggleForm = () => {
    setIsFormShowing((prevState) => !prevState);
    clearErrors(form);
  };

  const clearErrors = (form: IChangePasswordForm) => {
    for (const key of Object.keys(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: IChangePasswordForm) => {
    let isValidated = true;

    for (const key of Object.keys(form)) {
      if (!form[key as keyof IChangePasswordForm].value) {
        handleUpdateField(key, `${key} is required`, 'error');
        isValidated = false;
      }
    }
    return isValidated;
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    clearErrors(form);
    if (!validateForm) {
      return;
    }
    updateUserPassword({ form, userId: user.id, token })
      .unwrap()
      .then(() => {
        signOut({ token, refreshToken });
      })
      .then(() => {
        dispatch(clearUser());
        navigate('/signin');
      })
      .catch((err) => {
        handleUpdateField('password', err.data.message, 'error');
      });
  };

  return (
    <div className="mt-8">
      <div className="flex justify-between">
        <p className="font-bold">Password</p>
        <p className="md:block hidden">
          <span onClick={toggleForm} className="text-green-400 font-bold cursor-pointer">
            Change password.
          </span>{' '}
          Improve your security with a strong password.
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
        <div className="my-4 animate-slidedown">
          <form onSubmit={handleSubmit} className="max-w-[600px]">
            <div className="my-4">
              <FormInputPasswordField
                handleUpdateField={handleUpdateField}
                name={form.curPassword.name}
                value={form.curPassword.value}
                error={form.curPassword.error}
                type={form.curPassword.type}
                label="Current Password"
                id="curPassword"
                errorField="Password"
                placeholder="Enter your current password"
                min={5}
                max={50}
                icon={<AiOutlineLock />}
              />
            </div>
            <div className="my-4">
              <FormInputPasswordField
                handleUpdateField={handleUpdateField}
                name={form.password.name}
                value={form.password.value}
                error={form.password.error}
                type={form.password.type}
                label="New Password"
                id="password"
                errorField="New password"
                placeholder="Enter your new password"
                visibility={true}
                min={5}
                max={50}
                icon={<AiOutlineLock />}
              />
            </div>
            {isWarning && (
              <p>After successfully changing your password you will be signed out for security purposes.</p>
            )}
            <div className="my-4 flex justify-center">
              <button className="btn md:w-[50%] w-full" type="submit">
                Change password
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};

export default ChangePassword;
