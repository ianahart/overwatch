import { useState } from 'react';
import { BsChevronDown, BsChevronLeft } from 'react-icons/bs';
import LocationForm from './LocationForm';
import LocationInfo from './LocationInfo';
import { ILocationForm } from '../../../interfaces';

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
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [form, setForm] = useState(formState);

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    setForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof ILocationForm], [attribute]: value },
    }));
  };

  const closeForm = () => {
    setIsFormOpen(false);
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
      {!isFormOpen && <LocationInfo />}
      {isFormOpen && <LocationForm closeForm={closeForm} form={form} handleUpdateField={handleUpdateField} />}
    </div>
  );
};

export default Location;
