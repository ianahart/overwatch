import { useEffect, useState } from 'react';
import { BsChevronDown, BsChevronLeft } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import LocationForm from './LocationForm';
import LocationInfo from './LocationInfo';
import { ILocation, ILocationForm } from '../../../interfaces';
import { TRootState, useCreateLocationMutation, useFetchSingleLocationQuery } from '../../../state/store';
import Spinner from '../../Shared/Spinner';

const formState = {
  address: { name: 'address', error: '', value: '', type: 'text' },
  addressTwo: { name: 'addressTwo', error: '', value: '', type: 'text' },
  city: { name: 'city', error: '', value: '', type: 'text' },
  country: { name: 'country', error: '', value: 'United States', type: 'text' },
  phoneNumber: { name: 'phoneNumber', error: '', value: '', type: 'text' },
  state: { name: 'state', error: '', value: '', type: 'text' },
  zipCode: { name: 'zipCode', error: '', value: '', type: 'text' },
};

const Location = () => {
  const [createLocation, { isLoading }] = useCreateLocationMutation();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [form, setForm] = useState(formState);
  const [error, setError] = useState('');
  const { data } = useFetchSingleLocationQuery({ token, userId: user.id });

  const syncLocation = (data: ILocation) => {
    for (let prop in data) {
      const value = data[prop as keyof ILocationForm];
      if (value) {
        handleUpdateField(prop, value, 'value');
      }
    }
  };

  useEffect(() => {
    if (data) {
      syncLocation(data.data);
    }
  }, [data]);

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    setForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof ILocationForm], [attribute]: value },
    }));
  };

  const closeForm = () => {
    setIsFormOpen(false);
  };
  const clearErrors = (form: ILocationForm) => {
    for (const [key, _] of Object.entries(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: ILocationForm) => {
    let isValidated = true;
    for (const key of Object.keys(form)) {
      if (!form[key as keyof ILocationForm].value && key !== 'addressTwo') {
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

  const handleOnSubmit = () => {
    clearErrors(form);
    setError('');

    if (!validateForm(form)) {
      return;
    }

    createLocation({ form, token, userId: user.id })
      .unwrap()
      .then(() => {
        setIsFormOpen(false);
      })
      .catch((err) => {
        applyServerErrors(err.data);
      });
  };

  return (
    <div className="border rounded-lg p-8 border-slate-800 my-8">
      <div className="flex justify-between">
        <p className="font-bold text-gray-400">Location</p>
        <div className="text-gray-400 cursor-pointer">
          {isFormOpen ? (
            <BsChevronDown onClick={() => setIsFormOpen(false)} />
          ) : (
            <BsChevronLeft onClick={() => setIsFormOpen(true)} />
          )}
        </div>
      </div>
      {!isFormOpen && <LocationInfo form={form} />}
      {isLoading && <Spinner message="Updating location..." />}
      {isFormOpen && (
        <LocationForm
          error={error}
          closeForm={closeForm}
          form={form}
          handleUpdateField={handleUpdateField}
          handleOnSubmit={handleOnSubmit}
        />
      )}
    </div>
  );
};

export default Location;
