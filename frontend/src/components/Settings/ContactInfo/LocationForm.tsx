import { AiOutlineHome } from 'react-icons/ai';
import { FaRegBuilding } from 'react-icons/fa';
import { CiMapPin } from 'react-icons/ci';
import { debounce } from 'lodash';
import { useCallback, useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { ILocationAddressResult, ILocationForm } from '../../../interfaces';
import FormInputField from '../../Form/FormInputField';
import FormSelect from '../../Form/FormSelect';
import { countries } from '../../../data';
import LocationPhoneInput from './LocationPhoneInput';
import { TRootState, useLazyFetchLocationsQuery } from '../../../state/store';

export interface ILocationFormProps {
  error: string;
  form: ILocationForm;
  handleUpdateField: (name: string, value: string, attribute: string) => void;
  closeForm: () => void;
  handleOnSubmit: () => void;
}

const LocationForm = ({ error, form, handleUpdateField, closeForm, handleOnSubmit }: ILocationFormProps) => {
  const [fullAddress, setFullAddress] = useState('');
  const { token } = useSelector((store: TRootState) => store.user);
  const [fetchLocationsTrigger, { data }] = useLazyFetchLocationsQuery();
  const [addressResults, setAddressResults] = useState<ILocationAddressResult[]>([]);
  const [isDropdownOpen, setIsDropDownOpen] = useState(false);

  const updateField = (name: string, value: string) => {
    handleUpdateField(name, value, 'value');
  };

  const saveLocationAddressResults = (data: any) => {
    setAddressResults([]);
    for (const { properties } of data) {
      const { formatted, place_id, city, country, county, state, street, housenumber, postcode } = properties;
      const newAddress = { formatted, place_id, city, country, county, state, street, housenumber, zipCode: postcode };
      setAddressResults((prevState) => [...prevState, newAddress]);
    }
    setIsDropDownOpen(true);
  };

  useEffect(() => {
    if (data !== undefined && data.data !== null) {
      saveLocationAddressResults(JSON.parse(data?.data));
    }
  }, [data]);

  const debouncedHandleChange = useCallback(
    debounce((nextValue) => {
      fetchLocationsTrigger({ token, text: nextValue });
    }, 300),
    []
  );

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const nextValue = e.target.value;
    setFullAddress(nextValue);
    debouncedHandleChange(nextValue);
  };

  const handleOnKeydown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && fullAddress.length <= 0) {
      setIsDropDownOpen(false);
    }
  };

  const populateForm = (data: ILocationAddressResult) => {
    for (const [key, val] of Object.entries(data)) {
      if (key === 'housenumber') {
        handleUpdateField('address', `${val} ${data.street}`, 'value');
      } else if (Object.keys(form).includes(key)) {
        handleUpdateField(key, val, 'value');
      }
    }

    setIsDropDownOpen(false);
    setFullAddress('');
  };

  const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    handleOnSubmit();
  };

  return (
    <form data-testid="location-form" onSubmit={onSubmit}>
      {error.length > 0 && (
        <div className="my-4 flex justify-center">
          <p className="text-red-400 text-sm">{error}</p>
        </div>
      )}
      <div className="my-4 px-4">
        <FormSelect updateField={updateField} country={form.country.value} data={countries} />
      </div>
      <div className="my-12">
        <p className="text-gray-400 font-bold px-4">
          We take privacy seriously. Only your city, state, and country will be shared with clients.
        </p>
        <div className="my-4 px-4 relative">
          <input
            onKeyDown={handleOnKeydown}
            value={fullAddress}
            onChange={handleOnChange}
            className="h-9 rounded bg-transparent border border-gray-800 w-full placeholder:pl-2 pl-2 shadow"
            placeholder="Start typing your address"
          />
          {isDropdownOpen && (
            <div className="absolute top-10 w-full bg-stone-950 left-0 z-10 px-4">
              <ul>
                {addressResults.map((result) => {
                  return (
                    <li
                      onClick={() => populateForm(result)}
                      className="my-2 cursor-pointer hover:bg-stone-900"
                      key={result.place_id}
                    >
                      {result.formatted}
                    </li>
                  );
                })}
              </ul>
            </div>
          )}
        </div>
      </div>
      <div className="my-4 lg:flex lg:items-center">
        <div className="lg:w-[55%] lg:my-4 my-4 px-4">
          <FormInputField
            handleUpdateField={handleUpdateField}
            name={form.address.name}
            value={form.address.value}
            error={form.address.error}
            type={form.address.type}
            label="Address"
            id="address"
            errorField="Address"
            placeholder="Address"
            icon={<AiOutlineHome />}
          />
        </div>
        <div className="lg:w-[45%] lg:my-4 my-4 px-4">
          <FormInputField
            handleUpdateField={handleUpdateField}
            name={form.addressTwo.name}
            value={form.addressTwo.value}
            error={form.addressTwo.error}
            type={form.addressTwo.type}
            label="Add 2 (Apartment, suite, etc)"
            id="addressTwo"
            errorField="Address 2"
            placeholder="Apt/Suite"
            icon={<AiOutlineHome />}
          />
        </div>
      </div>
      <div className="my-4 lg:flex lg:items-center">
        <div className="lg:w-[55%] lg:my-4 my-4 px-4">
          <FormInputField
            handleUpdateField={handleUpdateField}
            name={form.city.name}
            value={form.city.value}
            error={form.city.error}
            type={form.city.type}
            label="City"
            id="city"
            errorField="City"
            placeholder="City"
            icon={<FaRegBuilding />}
          />
        </div>
        <div className="lg:w-[45%] lg:my-4 my-4 px-4">
          <FormInputField
            handleUpdateField={handleUpdateField}
            name={form.state.name}
            value={form.state.value}
            error={form.state.error}
            type={form.state.type}
            label="State/Province"
            id="state"
            errorField="State"
            placeholder="State"
            icon={<CiMapPin />}
          />
        </div>
      </div>
      <div className="lg:w-[40%] lg:my-4 my-4 px-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.zipCode.name}
          value={form.zipCode.value}
          error={form.zipCode.error}
          type={form.zipCode.type}
          label="ZIP/Postal code"
          id="zip"
          errorField="Zip"
          placeholder="Zip"
          icon={<CiMapPin />}
        />
      </div>
      <div className="my-4">
        <LocationPhoneInput name={form.phoneNumber.name} value={form.phoneNumber.value} updateField={updateField} />
      </div>
      <div className="flex items-center my-4">
        <button className="btn mx-4">Update</button>
        <button
          onClick={closeForm}
          className="p-2 mx-4 h-9 text-gray-400 rounded transition ease-in-out  hover:opacity-70"
        >
          Cancel
        </button>
      </div>
    </form>
  );
};

export default LocationForm;
