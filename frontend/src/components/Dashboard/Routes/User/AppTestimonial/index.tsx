import { useEffect, useState } from 'react';
import { nanoid } from 'nanoid';
import { ToastContainer, toast } from 'react-toastify';
import { animated, useSpring } from 'react-spring';
import { useSelector } from 'react-redux';

import { developerTypes as options } from '../../../../../data';
import { IError } from '../../../../../interfaces';
import { TRootState, useCreateAppTestimonialMutation } from '../../../../../state/store';

const AppTestimonial = () => {
  const MAX_CONTENT_LENGTH = 200;
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [createAppTestimonial] = useCreateAppTestimonialMutation();
  const [showSubmitBtn, setShowSubmitBtn] = useState(false);
  const [selectedOption, setSelectedOption] = useState('');
  const [testimonial, setTestimonial] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [errors, setErrors] = useState<string[]>([]);
  const springs = useSpring({
    from: { opacity: showSubmitBtn ? 0 : 1 },
    to: { opacity: showSubmitBtn ? 1 : 0 },
    config: { duration: 300 },
  });

  const handleOnChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, inputType: string) => {
    if (inputType === 'testimonial') {
      setTestimonial(e.target.value);
      return;
    }
    if (inputType === 'option') {
      setSelectedOption(e.target.value);
    }
  };

  useEffect(() => {
    if (selectedOption.trim().length > 0 && testimonial.trim().length > 0) {
      setShowSubmitBtn(true);
    } else {
      setShowSubmitBtn(false);
    }
  }, [selectedOption, testimonial]);

  const applyErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      setErrors((prevState) => [...prevState, data[prop]]);
    }
  };

  const canSubmit = () => {
    if (testimonial.length > MAX_CONTENT_LENGTH) {
      const error = `Testimonial must be between 1 and ${MAX_CONTENT_LENGTH} characters`;
      setErrors((prevState) => [...prevState, error]);
      return false;
    }
    return true;
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    setErrors([]);

    if (!canSubmit()) {
      return;
    }
    const payload = {
      userId: user.id,
      token,
      content: testimonial,
      developerType: selectedOption,
    };

    createAppTestimonial(payload)
      .unwrap()
      .then((res) => {
        console.log(res);
        initiateToast();
      })
      .catch((err) => {
        console.log(err);
        applyErrors(err.data);
      });
  };

  const initiateToast = () => {
    toast.success('Your testimonial has been recorded. Thank you!', {
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

  return (
    <div className="flex flex-col justify-center items-center">
      <div className="max-w-[800px] w-full p-2 bg-gray-900">
        <header className="text-center">
          <h3 className="text-gray-400 text-xl">Tell Us About Your Journey</h3>
          <p className="my-2">We'd love to hear how our platform supported your growth as a developer.</p>
          <p>We may use your testimonial on our website.</p>
        </header>
        <div className="mt-12 mb-6">
          <form onSubmit={handleOnSubmit}>
            <div>
              <p className="text-gray-400 my-2">What type of developer are you?</p>
              {options.map((option) => {
                return (
                  <div key={option.id} className="my-2">
                    <input
                      onChange={(e) => handleOnChange(e, 'option')}
                      value={option.value}
                      className="mr-2"
                      type="radio"
                      id={option.id}
                      name="options"
                      checked={selectedOption === option.value}
                    />
                    <label htmlFor={option.id}>{option.label}</label>
                  </div>
                );
              })}
            </div>
            <div className="flex flex-col my-4">
              <label className="text-gray-400 mb-2" htmlFor="testimonial">
                Share your story
              </label>
              <textarea
                onChange={(e) => handleOnChange(e, 'testimonial')}
                value={testimonial}
                id="testimonial"
                name="testimonial"
                className="min-h-24 p-1 resize-none border rounded border-gray-800 bg-transparent"
              ></textarea>
            </div>
            {errors.length > 0 && (
              <div className="my-4">
                {errors.map((error) => {
                  return (
                    <p className="text-sm text-red-300" key={nanoid()}>
                      {error}
                    </p>
                  );
                })}
              </div>
            )}
            {showSubmitBtn && (
              <animated.div style={springs} className="my-6">
                <button type="submit" className="btn">
                  Submit
                </button>
              </animated.div>
            )}
          </form>
        </div>
      </div>
      <ToastContainer />
    </div>
  );
};

export default AppTestimonial;
