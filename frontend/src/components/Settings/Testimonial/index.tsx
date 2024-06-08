import { useState } from 'react';
import { AiOutlineUser } from 'react-icons/ai';
import Header from '../Header';
import { ITestimonialForm } from '../../../interfaces';
import FormInputField from '../../Form/FormInputField';
import FormTextareaField from '../../Form/FormTextareaField';

const formState = {
  name: { name: 'name', value: '', error: '', type: 'text', max: 100 },
  text: { name: 'text', value: '', error: '', type: 'text', max: 300 },
};

const Testimonial = () => {
  const [form, setForm] = useState<ITestimonialForm>(formState);

  const handleUpdateField = (name: string, value: string | number, attribute: string) => {
    setForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof ITestimonialForm], [attribute]: value },
    }));
  };

  const clearErrors = (form: ITestimonialForm) => {
    for (const key of Object.keys(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = (form: ITestimonialForm) => {
    let errors = false;

    for (const key of Object.keys(form)) {
      const { error, value, max } = form[key as keyof ITestimonialForm];
      if (error.length || value.trim().length === 0) {
        const errorMsg = `${key} must be present and be of maxium ${max} chars.`;
        handleUpdateField(key, errorMsg, 'error');
        errors = true;
      }
    }

    return !errors;
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    clearErrors(form);
    if (!validateForm(form)) {
      return;
    }
    console.log('submit');
  };

  return (
    <div className="p-4">
      <Header heading="Add Testimonials" />
      <div className="my-4 rounded-lg border 1px solid border-gray-800 min-h-[200px] p-4">
        <form onSubmit={handleOnSubmit}>
          <div className="my-4">
            <FormInputField
              handleUpdateField={handleUpdateField}
              name={form.name.name}
              value={form.name.value}
              error={form.name.error}
              type={form.name.type}
              label="Name of person"
              id="name"
              errorField="Name"
              placeholder="Enter name of person"
              icon={<AiOutlineUser />}
              max={100}
            />
          </div>
          <div className="my-4">
            <FormTextareaField
              handleUpdateField={handleUpdateField}
              name={form.text.name}
              value={form.text.value}
              error={form.text.error}
              label="Testimonial"
              id="text"
              errorField="Testimonial"
              placeholder="What did they say about you?"
              max={300}
            />
          </div>
          <div className="mt-10 mb-5">
            <button type="submit" className="btn">
              Add Testimonial
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Testimonial;
