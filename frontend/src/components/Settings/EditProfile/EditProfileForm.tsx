import { useState } from 'react';
import BasicInfo from './BasicInfo';
import ProfileSetup from './ProfileSetup';
import Skills from './Skills';
import WorkExperience from './WorkExperience';

const EditProfileForm = () => {
  const [formStep, setFormStep] = useState(0);

  const renderFormStep = () => {
    switch (formStep) {
      case 0:
        return <BasicInfo />;
      case 1:
        return <ProfileSetup />;
      case 2:
        return <Skills />;
      case 3:
        return <WorkExperience />;
      default:
        return <BasicInfo />;
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
  };

  return (
    <section className="border rounded-lg p-4 max-w-[650px] border-slate-800 my-8">
      <form onSubmit={handleOnSubmit}>
        {renderFormStep()}
        <div className="my-8 justify-center flex">
          {formStep > 0 && (
            <button
              onClick={() => setFormStep((prevState) => prevState - 1)}
              type="button"
              className="outline-btn bg-gray-400 min-w-24 mx-4"
            >
              Back
            </button>
          )}
          {formStep < 5 && (
            <button
              onClick={() => setFormStep((prevState) => prevState + 1)}
              type="button"
              className="outline-btn bg-gray-400 min-w-24 mx-4"
            >
              Next
            </button>
          )}
          {formStep === 5 && (
            <button type="submit" className="btn mx-4 min-w-24">
              Update
            </button>
          )}
        </div>
      </form>
    </section>
  );
};

export default EditProfileForm;
