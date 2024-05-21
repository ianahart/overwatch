import { FaRegEnvelope } from 'react-icons/fa';
import { IAccountForm, ISyncUserResponse } from '../../../interfaces';
import FormInputField from '../../Form/FormInputField';
import { AiOutlineUser } from 'react-icons/ai';
import { useSyncUserQuery } from '../../../state/store';
import { retrieveTokens } from '../../../util';
import { useEffect } from 'react';

export interface IAccountFormProps {
  form: IAccountForm;
  handleUpdateField: (name: string, value: string, attribute: string) => void;
  cancelUpdate: () => void;
  handleSubmit: () => void;
}

const AccountForm = ({ form, handleUpdateField, cancelUpdate, handleSubmit }: IAccountFormProps) => {
  const token = retrieveTokens()?.token;
  const { data, isSuccess } = token ? useSyncUserQuery(token) : { data: null, isSuccess: false };

  useEffect(() => {
    if (isSuccess) {
      const { email, firstName, lastName, id } = data as ISyncUserResponse;
      const values = { email, firstName, lastName, id };
      Object.entries(values).forEach(([key, val]) => handleUpdateField(key, val.toString(), 'value'));
    }
  }, [data, isSuccess]);

  const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    handleSubmit();
  };

  return (
    <form onSubmit={onSubmit}>
      <div className="my-4 text-center text-sm">
        <p className="text-yellow-400">
          Changing any of this information will log out you of your account for security reasons. You will then need to
          log back in.
        </p>
      </div>
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.firstName.name}
          value={form.firstName.value}
          error={form.firstName.error}
          type={form.firstName.type}
          label="First Name"
          id="firstName"
          errorField="First name"
          placeholder="Enter your first name"
          icon={<AiOutlineUser />}
        />
      </div>
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.lastName.name}
          value={form.lastName.value}
          error={form.lastName.error}
          type={form.lastName.type}
          label="Last Name"
          id="lastName"
          errorField="Last name"
          placeholder="Enter your last name"
          icon={<AiOutlineUser />}
        />
      </div>
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.email.name}
          value={form.email.value}
          error={form.email.error}
          type={form.email.type}
          label="Email"
          id="email"
          errorField="Email"
          placeholder="Enter your email"
          icon={<FaRegEnvelope />}
        />
      </div>
      <div className="flex my-4 justify-start">
        <button className="btn m-4 ml-0" type="submit">
          Update
        </button>
        <button
          onClick={cancelUpdate}
          className="m-4 bg-gray-200 text-slate-700 p-2 h-9 rounded transition ease-in-out  hover:opacity-70;"
          type="button"
        >
          Cancel
        </button>
      </div>
    </form>
  );
};

export default AccountForm;
