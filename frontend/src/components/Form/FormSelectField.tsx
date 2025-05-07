import { nanoid } from 'nanoid';

export interface IFormSelectFieldProps {
  handleUpdateField: (name: string, value: string, attribute: string) => void;
  options: { value: string; display: string }[];
  name: string;
  value: string;
  error: string;
  label: string;
  id: string;
  errorField: string;
}

const FormSelectField = ({ handleUpdateField, options, name, value, error, label, id }: IFormSelectFieldProps) => {
  const handleOnChange = (e: React.ChangeEvent<HTMLSelectElement>): void => {
    const { name, value } = e.target;
    handleUpdateField(name, value, 'value');
  };

  return (
    <div className="flex flex-col">
      <label htmlFor={name}>{label}</label>
      <select
        name={name}
        onChange={handleOnChange}
        id={id}
        className="border border-gray-800 rounded h-9 bg-transparent"
        value={value}
        data-testid="custom-select"
      >
        {options.map((option) => {
          return (
            <option key={nanoid()} value={option.value}>
              {option.display}
            </option>
          );
        })}
      </select>
      {error.length > 0 && <p className="my-1 text-red-300 text-xs">{error}</p>}
    </div>
  );
};

export default FormSelectField;
