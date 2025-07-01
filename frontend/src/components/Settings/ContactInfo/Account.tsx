import { BsChevronDown, BsChevronLeft } from 'react-icons/bs';
import { TRootState, clearSetting, clearUser, useUpdateUserMutation } from '../../../state/store';
import { useState } from 'react';
import { IAccountForm } from '../../../interfaces';
import AccountInfo from './AccountInfo';
import AccountForm from './AccountForm';
import { useDispatch, useSelector } from 'react-redux';
import Spinner from '../../Shared/Spinner';
import { useNavigate } from 'react-router-dom';

const formState = {
  firstName: { name: 'firstName', value: '', type: 'text', error: '' },
  lastName: { name: 'lastName', value: '', type: 'text', error: '' },
  email: { name: 'email', value: '', type: 'email', error: '' },
};

const Account = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [performUpdateUser, results] = useUpdateUserMutation();
  const [form, setForm] = useState(formState);
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [error, setError] = useState('');

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    setForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof IAccountForm], [attribute]: value },
    }));
  };

  const clearErrors = (form: IAccountForm) => {
    for (const key of Object.keys(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: IAccountForm) => {
    let isValidated = true;
    for (const key of Object.keys(form)) {
      if (!form[key as keyof IAccountForm].value) {
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

  const handleSubmit = () => {
    setError('');
    clearErrors(form);

    if (!validateForm(form)) {
      return;
    }
    performUpdateUser({ form, userId: user.id, token })
      .unwrap()
      .then(() => {
        dispatch(clearUser());
      })
      .then(() => {
        dispatch(clearSetting());
        navigate('/signin');
      })

      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  const cancelUpdate = () => {
    setIsFormOpen(false);
  };

  return (
    <div>
      <div className="border rounded-lg p-8 border-slate-800 my-8">
        <div className="flex justify-between">
          <p className="font-bold text-gray-400">Account</p>
          <div className="text-gray-400 cursor-pointer">
            {isFormOpen ? (
              <BsChevronDown data-testid="account-chevron-down" onClick={() => setIsFormOpen(false)} />
            ) : (
              <BsChevronLeft data-testid="account-chevron-left" onClick={() => setIsFormOpen(true)} />
            )}
          </div>
        </div>
        {!isFormOpen && (
          <AccountInfo email={user.email} firstName={user.firstName} lastName={user.lastName} userId={user.id} />
        )}
        {error.length > 0 && <p className="text-red-400 text-sm">{error}</p>}
        {isFormOpen && results.isLoading && (
          <div className="flex justify-center my-4">
            <Spinner message="Updating user..." />
          </div>
        )}
        {isFormOpen && !results.isLoading && (
          <AccountForm
            cancelUpdate={cancelUpdate}
            handleSubmit={handleSubmit}
            handleUpdateField={handleUpdateField}
            form={form}
          />
        )}
      </div>
    </div>
  );
};

export default Account;
