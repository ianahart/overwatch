import { useDispatch, useSelector } from 'react-redux';
import RoleQuestionField from './RoleQuestionField';
import { TRootState } from '../../state/store';
import FormInputField from '../Form/FormInputField';
import { AiOutlineUser } from 'react-icons/ai';
import { updateField } from '../../state/store';
import { FaRegEnvelope } from 'react-icons/fa';

const Form = () => {
  const form = useSelector((store: TRootState) => store.signup);
  const dispatch = useDispatch();

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    dispatch(updateField({ name, value, attribute }));
  };

  return (
    <div className="bg-slate-900 lg:w-3/6 rounded-r p-4">
      <form className="max-w-xl mx-auto">
        <header className="text-center">
          <h1 className="text-2xl font-display text-green-400">Sign up for OverWatch</h1>
          <p>Create a free account or log in</p>
        </header>
        <section className="flex flex-col justify-center min-h-40">
          <RoleQuestionField />
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
        </section>
      </form>
    </div>
  );
};

export default Form;
