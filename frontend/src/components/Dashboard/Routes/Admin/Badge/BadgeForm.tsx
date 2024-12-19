import { useState } from 'react';
import { MdTitle } from 'react-icons/md';
import { useSelector } from 'react-redux';

import { badgeFormState } from '../../../../../data';
import Upload from '../../../../Shared/Upload';
import FormTextareaField from '../../../../Form/FormTextareaField';
import FormInputField from '../../../../Form/FormInputField';
import { IBadgeForm, IError } from '../../../../../interfaces';
import { TRootState, useCreateBadgeMutation } from '../../../../../state/store';
import { useNavigate } from 'react-router-dom';

export interface IBadgeFormProps {
  formType: string;
}

const BadgeForm = ({ formType }: IBadgeFormProps) => {
  const navigate = useNavigate();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [createBadge] = useCreateBadgeMutation();
  const [form, setForm] = useState(badgeFormState);

  const handleUpdateField = (name: string, value: string | File | null, attribute: string): void => {
    setForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof IBadgeForm], [attribute]: value },
    }));
  };

  const clearErrors = (): void => {
    for (const fieldName of Object.keys(form)) {
      handleUpdateField(fieldName, '', 'error');
    }
  };

  const formHasErrors = (): boolean => {
    let errors = false;
    for (const field of Object.values(form)) {
      if (field.value === null || field.value === '' || field.error.length > 0) {
        handleUpdateField(field.name, `${field.name} cannot be empty`, 'error');
        errors = true;
      }
    }
    return errors;
  };

  const applyErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      if (prop === 'message') {
        handleUpdateField('title', data[prop], 'error');
      } else {
        handleUpdateField(prop, data[prop], 'error');
      }
    }
  };

  const handleCreateBadge = (): void => {
    const body = new FormData();
    Object.entries(form).forEach(([fieldName, field]) => body.append(fieldName, field.value ?? ''));
    createBadge({ token, body })
      .unwrap()
      .then(() => {
        navigate(`/admin/dashboard/${user.slug}/badges`);
      })
      .catch((err) => {
        applyErrors(err.data);
      });
  };

  const handleUpdateBadge = (): void => {
    console.log('updating badge...');
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    clearErrors();
    if (formHasErrors()) {
      return;
    }

    if (formType === 'create') {
      handleCreateBadge();
      return;
    }

    if (formType === 'update') {
      handleUpdateBadge();
    }
  };

  return (
    <div>
      <form onSubmit={handleOnSubmit}>
        <div className="my-6">
          <FormInputField
            icon={<MdTitle />}
            handleUpdateField={handleUpdateField}
            name={form.title.name}
            value={form.title.value}
            error={form.title.error}
            type={form.title.type}
            label="Title"
            id={form.title.name}
            errorField="Title"
            placeholder="Enter a title"
            min={1}
            max={100}
          />
        </div>
        <div className="my-6">
          <FormTextareaField
            handleUpdateField={handleUpdateField}
            name={form.description.name}
            value={form.description.value}
            error={form.description.error}
            label="Description"
            id={form.description.name}
            errorField="Description"
            placeholder="Enter a description"
            min={1}
            max={200}
          />
        </div>
        <div className="my-6">
          <Upload
            title="Upload an image"
            value={form.image.value}
            fieldName="image"
            maxFileSizeWord="1MB"
            maxFileSize={1000000}
            error={form.image.error}
            handleUpdateAttachment={handleUpdateField}
          />
        </div>
        <div className="mt-12 mb-4">
          <button className="btn w-full">{formType === 'create' ? 'Submit' : 'Update'}</button>
        </div>
      </form>
    </div>
  );
};

export default BadgeForm;
