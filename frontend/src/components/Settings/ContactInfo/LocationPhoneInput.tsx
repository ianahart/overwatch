import 'react-phone-number-input/style.css';
import PhoneInput from 'react-phone-number-input';

export interface ILocationPhoneInputProps {
  updateField: (name: string, value: string) => void;
  value: string;
  name: string;
}

const LocationPhoneInput = ({ name, updateField, value }: ILocationPhoneInputProps) => {
  return (
    <div className="">
      <label>Phone Number</label>
      <PhoneInput
        defaultCountry="US"
        onChange={(e) => {
          updateField(name, e as string);
        }}
        //@ts-ignore
        value={value}
        placeholder="Enter phone number"
      />
    </div>
  );
};

export default LocationPhoneInput;
