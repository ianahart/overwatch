import { useState } from 'react';
import { useSelector } from 'react-redux';
import { AiOutlineUser } from 'react-icons/ai';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Header from '../Header';
import { ITestimonialForm } from '../../../interfaces';
import FormInputField from '../../Form/FormInputField';
import FormTextareaField from '../../Form/FormTextareaField';
import { TRootState, useCreateTestimonialMutation } from '../../../state/store';
import Spinner from '../../Shared/Spinner';
import Testimonials from './Testimonials';

const formState = {
  name: { name: 'name', value: '', error: '', type: 'text', max: 100 },
  text: { name: 'text', value: '', error: '', type: 'text', max: 300 },
};

const Testimonial = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createTestimonial, { isLoading }] = useCreateTestimonialMutation();
  const [form, setForm] = useState<ITestimonialForm>(formState);
  const [error, setError] = useState('');

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
        const errorMsg = `${key} must be present and be of maximum ${max} chars.`;
        handleUpdateField(key, errorMsg, 'error');
        errors = true;
      }
    }

    return !errors;
  };

  const initiateToast = () => {
    toast.success('Your testimonial was successfully added!', {
      position: 'bottom-center',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: 'dark',
    });
  };

  const applyServerErrors = <T extends object>(data: T) => {
    for (const [key, val] of Object.entries(data)) {
      if (key === 'message') {
        setError(val);
        return;
      }
      handleUpdateField(key, val, 'error');
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    clearErrors(form);
    if (!validateForm(form)) {
      return;
    }
    createTestimonial({ userId: user.id, token, form })
      .unwrap()
      .then((res) => {
        console.log(res);
        setForm(formState);
        initiateToast();
      })
      .catch((err) => {
        console.log(err);
        if (err.status === 400) {
          applyServerErrors(err.data);
        }
      });
  };

  return (
    <div className="p-4">
      <Header heading="Testimonials" />
      <div className="my-4 rounded-lg border 1px solid border-gray-800 min-h-[200px] p-4">
        <form onSubmit={handleOnSubmit}>
          <div>
            <h2 className="text-gray-400 text-xl">Add Testimonial</h2>
            {error.length > 0 && <p className="text-sm text-red-300">{error}</p>}
          </div>
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
            {isLoading && <Spinner message="Creating testimonial..." />}
            {!isLoading && (
              <button type="submit" className="btn">
                Add Testimonial
              </button>
            )}
          </div>
          <ToastContainer />
        </form>
      </div>
      <div className="my-8">
        <h3 className="text-xl text-gray-400">Your Testimonials</h3>
        <Testimonials />
      </div>
    </div>
  );
};

export default Testimonial;
