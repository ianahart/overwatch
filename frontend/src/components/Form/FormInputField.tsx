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
  min?: number;
  max?: number;
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
  min = 1,
  max = 200,
}: IFormInputFieldProps) => {
  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value, name } = e.target;
    handleUpdateField(name, value, 'value');
  };

  const handleOnBlur = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value, name } = e.target;
    if (value.length < min || value.length > max) {
      const validationError = `${errorField} must be between ${min} and ${max} characters`;
      handleUpdateField(name, validationError, 'error');
    }
  };

  const handleOnFocus = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name } = e.target;
    handleUpdateField(name, '', 'error');
  };

  return (
    <div className="flex flex-col">
      <label className="mb-1" htmlFor={name}>
        {label}
      </label>
      <div className="relative">
        <input
          onChange={handleOnChange}
          onBlur={handleOnBlur}
          onFocus={handleOnFocus}
          className="h-9 rounded bg-slate-800 w-full placeholder:pl-6 pl-8 shadow"
          id={id}
          name={name}
          value={value}
          placeholder={placeholder}
          type={type}
        />
        <div className="absolute top-2 left-2">{icon}</div>
      </div>
      {error.length > 0 && <p className="text-red-400 mt-1 text-sm">{error}</p>}
    </div>
  );
};

export default FormInputField;
