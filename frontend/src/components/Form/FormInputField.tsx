export interface IFormInputFieldProps {
  handleUpdateField: (name: string, value: string, attribute: string) => void;
  name: string;
  icon?: React.ReactNode;
  value: string;
  error: string;
  type: string;
  label: string;
  id: string;
  errorField: string;
  placeholder: string;
}

const FormInputField = ({
  handleUpdateField,
  name,
  icon,
  value,
  error,
  type,
  label,
  id,
  errorField,
  placeholder,
}: IFormInputFieldProps) => {
  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value, name } = e.target;
    handleUpdateField(name, value, 'value');
  };

  return (
    <div className="flex flex-col">
      <label className="mb-1" htmlFor={name}>
        {label}
      </label>
      <div className="relative">
        <input
          onChange={handleOnChange}
          className="h-9 rounded bg-slate-800 w-full placeholder:pl-6 pl-6 shadow"
          id={id}
          name={name}
          value={value}
          placeholder={placeholder}
          type={type}
        />
        <div className="absolute top-2">{icon}</div>
      </div>
    </div>
  );
};

export default FormInputField;
