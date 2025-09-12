import { useEffect, useState } from 'react';
import { MdTitle } from 'react-icons/md';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { badgeFormState } from '../../../../../data';
import Upload from '../../../../Shared/Upload';
import FormTextareaField from '../../../../Form/FormTextareaField';
import FormInputField from '../../../../Form/FormInputField';
import { IBadgeForm, IError } from '../../../../../interfaces';
import {
  TRootState,
  useCreateBadgeMutation,
  useLazyFetchBadgeQuery,
  useUpdateBadgeMutation,
} from '../../../../../state/store';

export interface IBadgeFormProps {
  formType: string;
  handleCloseModal?: () => void;
  badgeId?: number;
}

const BadgeForm = ({ formType, badgeId = 0, handleCloseModal = () => {} }: IBadgeFormProps) => {
  const navigate = useNavigate();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [fetchBadge] = useLazyFetchBadgeQuery();
  const [createBadge] = useCreateBadgeMutation();
  const [updateBadge] = useUpdateBadgeMutation();
  const [form, setForm] = useState(badgeFormState);

  useEffect(() => {
    if (formType === 'edit' && badgeId !== 0) {
      console.log('fetch data');
      fetchBadge({ token, badgeId })
        .unwrap()
        .then((res) => {
          const data = res.data;
          for (let prop in data) {
            handleUpdateField(prop, data[prop], 'value');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, [formType, badgeId]);

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
        console.log(field.name);
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

  const handleCreateBadge = (body: FormData): void => {
    createBadge({ token, body })
      .unwrap()
      .then(() => {
        navigate(`/admin/dashboard/${user.slug}/badges`);
      })
      .catch((err) => {
        applyErrors(err.data);
      });
  };

  const handleUpdateBadge = (body: FormData): void => {
    updateBadge({ token, badgeId, body })
      .unwrap()
      .then((res) => {
        console.log(res);
        handleCloseModal();
      })
      .catch((err) => {
        console.log(err);
        applyErrors(err.data);
      });
  };

  const appendToBody = (body: FormData): void => {
    Object.entries(form).forEach(([fieldName, field]) => {
      const value = (field as { value: string | File | null }).value;
      if (fieldName === 'image' && value instanceof File) {
        body.append(fieldName, value);
      } else if (fieldName !== 'image') {
        body.append(fieldName, value ?? '');
      }
    });
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    clearErrors();
    if (formHasErrors()) {
      return;
    }
    const body = new FormData();
    appendToBody(body);

    if (formType === 'create') {
      handleCreateBadge(body);
      return;
    }

    if (formType === 'edit') {
      handleUpdateBadge(body);
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
          <button data-testid="badge-form-btn" type="submit" className="btn w-full">
            {formType === 'create' ? 'Submit' : 'Update'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default BadgeForm;
