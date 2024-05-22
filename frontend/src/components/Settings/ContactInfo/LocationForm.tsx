import { AiOutlineHome } from 'react-icons/ai';
import { FaRegBuilding } from 'react-icons/fa';
import { CiMapPin } from 'react-icons/ci';

import { ILocationForm } from '../../../interfaces';
import FormInputField from '../../Form/FormInputField';
import FormSelect from '../../Form/FormSelect';
import { countries } from '../../../data';
import LocationPhoneInput from './LocationPhoneInput';

export interface ILocationFormProps {
  form: ILocationForm;
  handleUpdateField: (name: string, value: string, attribute: string) => void;
  closeForm: () => void;
}

const LocationForm = ({ form, handleUpdateField, closeForm }: ILocationFormProps) => {
  const updateField = (name: string, value: string) => {
    handleUpdateField(name, value, 'value');
  };

  return (
    <form>
      <div className="my-4">
        <FormSelect updateField={updateField} country={form.country.value} data={countries} />
      </div>
      <div className="my-12">
        <p className="text-gray-400 font-bold px-4">
          We take privacy seriously. Only your city, state, and country will be shared with clients.
        </p>
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
        <LocationPhoneInput value={form.phoneNumber.value} updateField={updateField} />
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
