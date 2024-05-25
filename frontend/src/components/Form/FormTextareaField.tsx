export interface IFormTextareaFieldProps {
  handleUpdateField: (name: string, value: string, attribute: string) => void;
  name: string;
  value: string;
  error: string;
  label: string;
  id: string;
  errorField: string;
  placeholder: string;
  min?: number;
  max?: number;
}

const FormTextareaField = ({
  handleUpdateField,
  name,
  value,
  error,
  label,
  id,
  errorField,
  placeholder,
  min = 1,
  max = 200,
}: IFormTextareaFieldProps) => {
  const handleOnChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const { value, name } = e.target;
    handleUpdateField(name, value, 'value');
  };

  const handleOnBlur = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const { value, name } = e.target;
    if (value.length < min || value.length > max) {
      const validationError = `${errorField} must be between ${min} and ${max} characters`;
      handleUpdateField(name, validationError, 'error');
    }
  };

  const handleOnFocus = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const { name } = e.target;
    handleUpdateField(name, '', 'error');
  };

  return (
    <div className="flex flex-col">
      <label className="mb-1" htmlFor={name}>
        {label}
      </label>
      <div className="relative">
        <textarea
          onChange={handleOnChange}
          onBlur={handleOnBlur}
          onFocus={handleOnFocus}
          className="h-20 rounded bg-transparent border border-gray-800 w-full placeholder:pl-2 pl-2 shadow"
          id={id}
          name={name}
          value={value}
          placeholder={placeholder}
        ></textarea>
      </div>
      {error.length > 0 && <p className="text-red-400 mt-1 text-sm">{error}</p>}
    </div>
  );
};

export default FormTextareaField;
