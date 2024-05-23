import { ILocationForm } from '../../../interfaces';

export interface ILocationInfoProps {
  form: ILocationForm;
}

const LocationInfo = ({ form }: ILocationInfoProps) => {
  return (
    <div className="my-4">
      <div className="my-8">
        <h3 className="mb-2 text-gray-400">Location</h3>
        <p>{form.address.value}</p>
        <p>
          {form.city.value}, {form.state.value} {form.zipCode.value}
        </p>
      </div>
      <div className="my-8">
        <h3 className="mb-2 text-gray-400">Phone</h3>
        <p>{form.phoneNumber.value}</p>
      </div>
    </div>
  );
};

export default LocationInfo;
