import { useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useSelector } from 'react-redux';
import Spinner from '../../Shared/Spinner';
import { TRootState, useCreatePhoneMutation, useDeletePhoneMutation, useFetchPhoneQuery } from '../../../state/store';
import { IPhone } from '../../../interfaces';

const initialPhoneState = {
  id: 0,
  createdAt: '',
  isVerified: false,
  phoneNumber: '',
};

const PhoneNumber = () => {
  const { token, user } = useSelector((store: TRootState) => store.user);
  const { data, isSuccess } = useFetchPhoneQuery({ token, userId: user.id }, { skip: !token || !user.id });
  const [createPhone, results] = useCreatePhoneMutation();
  const [deletePhone] = useDeletePhoneMutation();
  const [error, setError] = useState('');
  const [phone, setPhone] = useState<IPhone>(initialPhoneState);
  const [phoneNumber, setPhoneNumber] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    if (isSuccess) {
      if (data.data !== null) {
        setPhone(data.data);
        setPhoneNumber(data.data?.phoneNumber);
      }
    }
  }, [data, isSuccess]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setPhoneNumber(value);
  };

  const validatePhoneNumber = (phoneNumber: string) => {
    if (isNaN(parseInt(phoneNumber))) {
      setError('Please make this is a phone number');
      return false;
    }

    const regex = /^\d{10}$/;

    if (!regex.test(phoneNumber)) {
      setError('Please make sure there are no spaces in between numbers and is in valid format');
      return false;
    }
    return true;
  };

  const initiateToast = () => {
    toast.success('Your phone number was successfully added to our system!', {
      position: 'bottom-center',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: 'dark',
    });
  };

  const applyErrors = <T extends object>(data: T) => {
    for (const value of Object.values(data)) {
      setError(value);
    }
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setSuccessMsg('');
    setError('');
    if (!validatePhoneNumber(phoneNumber)) {
      return;
    }

    createPhone({ token, userId: user.id, phoneNumber })
      .unwrap()
      .then(() => {
        initiateToast();
      })
      .catch((err) => {
        applyErrors(err.data);
      });
  };

  const handleDeletePhone = () => {
    deletePhone({ token, phoneId: phone.id })
      .unwrap()
      .then(() => {
        setPhoneNumber('');
        setPhone(initialPhoneState);
      });
  };

  return (
    <div className="my-4">
      <p className="text-sm">Enter the phone number where you want the pass code to be sent to when you log in.</p>
      <form onSubmit={handleSubmit} className="my-4">
        <div>
          <label htmlFor="phone" className="block mb-1">
            Your Phone Number
          </label>
          <input
            onChange={handleChange}
            value={phoneNumber}
            id="phone"
            name="phone"
            type="text"
            placeholder="ex: 5554445555"
            className="h-9 rounded bg-transparent border border-gray-800 w-full  pl-2 shadow max-w-[600px]"
          />
        </div>
        {error.length > 0 && <p className="text-red-400 text-sm">{error}</p>}
        {successMsg.length > 0 && <p className="text-gray-400 text-sm">{successMsg}</p>}
        {results.isLoading && (
          <div className="flex justify-start my-4">
            <Spinner message="Saving updates..." />
          </div>
        )}
        {!results.isLoading && (
          <div className="flex items-center">
            <div className="my-2 mx-2">
              <button type="submit" className="btn w-16">
                Save
              </button>
            </div>
            {phone.id !== 0 && (
              <div className="my-2 mx-2">
                <button
                  onClick={handleDeletePhone}
                  type="button"
                  className="w-16 text-white bg-red-400 p-2 h-9 rounded transition ease-in-out  hover:opacity-70"
                >
                  Delete
                </button>
              </div>
            )}
          </div>
        )}
      </form>
      <ToastContainer />
    </div>
  );
};

export default PhoneNumber;
