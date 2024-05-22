import 'react-phone-number-input/style.css';
import PhoneInput from 'react-phone-number-input';

export interface ILocationPhoneInputProps {
  updateField: (name: string, value: string) => void;
  value: string;
}

const LocationPhoneInput = ({ updateField, value }: ILocationPhoneInputProps) => {
  return (
    <div className="">
      <label>Phone Number</label>
      <PhoneInput
        defaultCountry="US"
        onChange={(e) => {
          updateField('phoneNumber', e as string);
        }}
        //@ts-ignore
        value={value}
        placeholder="Enter phone number"
      />
    </div>
  );
};

export default LocationPhoneInput;
