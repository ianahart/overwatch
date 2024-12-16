import { useState } from 'react';
import { useSelector } from 'react-redux';
import { MdTitle } from 'react-icons/md';
import { AiOutlineUser } from 'react-icons/ai';

import { suggestionFormState } from '../../../../../data';
import { IError, IFormField, ISuggestionForm } from '../../../../../interfaces';
import FormInputField from '../../../../Form/FormInputField';
import FormTextareaField from '../../../../Form/FormTextareaField';
import FormSelectField from '../../../../Form/FormSelectField';
import Upload from '../../../../Shared/Upload';
import { TRootState, useCreateSuggestionMutation } from '../../../../../state/store';
import { useNavigate } from 'react-router-dom';

const SuggestionForm = () => {
  const navigate = useNavigate();
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [createSuggestion] = useCreateSuggestionMutation();
  const [form, setForm] = useState<ISuggestionForm>(suggestionFormState);

  const feedbackTypeOptions = [
    { value: 'BUG_REPORT', display: 'Bug Report' },
    { value: 'FEATURE_REQUEST', display: 'Feature Request' },
    { value: 'GENERAL_FEEDBACK', display: 'General Feedback' },
    { value: 'SUGGESTION', display: 'Suggestion' },
  ];

  const priorityLevelOptions = [
    { value: 'LOW', display: 'Low' },
    { value: 'MEDIUM', display: 'Medium' },
    { value: 'HIGH', display: 'High' },
  ];

  const handleUpdateField = (name: string, value: string, attribute: string): void => {
    setForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof ISuggestionForm], [attribute]: value },
    }));
  };

  const handleUpdateAttachment = (name: string, value: string | File | null, attribute: string): void => {
    setForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof ISuggestionForm], [attribute]: value },
    }));
  };

  const clearErrors = (): void => {
    for (const field of Object.keys(form)) {
      handleUpdateField(field, '', 'error');
    }
  };

  const checkForErrors = (): boolean => {
    let errors = false;
    for (const [fieldName, field] of Object.entries(form).filter(([fieldName, _]) => fieldName !== 'attachment')) {
      if (field.error.length || field.value.trim().length === 0) {
        const error = `Make sure ${field.name} is filled out`;
        handleUpdateField(fieldName, error, 'error');
        errors = true;
      }
    }
    return errors;
  };

  const applyErrors = <T extends IError>(data: T): void => {
    for (let prop in data) {
      handleUpdateField(prop, data[prop], 'error');
    }
  };

  const appendFormData = (formData: FormData, fieldName: string, field: IFormField<string | File>): void => {
    if (fieldName === 'attachment') {
      if (field.value !== null) {
        formData.append(fieldName, field.value);
      }
    } else {
      formData.append(fieldName, field.value);
    }
  };

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    clearErrors();

    if (checkForErrors()) {
      return;
    }

    const formData = new FormData();
    formData.append('userId', user.id.toString());
    Object.entries(form).forEach(([fieldName, field]) => appendFormData(formData, fieldName, field));

    createSuggestion({ token, body: formData })
      .unwrap()
      .then(() => {
        navigate(-1);
      })
      .catch((err) => {
        applyErrors(err.data);
      });
  };

  return (
    <div className="w-full border border-gray-800 rounded p-2">
      <form onSubmit={handleOnSubmit}>
        <div className="my-6">
          <FormSelectField
            handleUpdateField={handleUpdateField}
            options={feedbackTypeOptions}
            name={form.feedbackType.name}
            value={form.feedbackType.value}
            error={form.feedbackType.error}
            label="Feedback Type"
            errorField="Feedback type"
            id={form.feedbackType.name}
          />
        </div>
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
            max={600}
          />
        </div>
        <div className="my-6">
          <Upload
            value={form.attachment.value}
            fieldName="attachment"
            maxFileSizeWord="2MB"
            maxFileSize={2000000}
            error={form.attachment.error}
            handleUpdateAttachment={handleUpdateAttachment}
          />
        </div>
        <div className="my-6">
          <FormInputField
            icon={<AiOutlineUser />}
            handleUpdateField={handleUpdateField}
            name={form.contact.name}
            value={form.contact.value}
            error={form.contact.error}
            type={form.contact.type}
            label="Contact Information"
            id={form.contact.name}
            errorField="Contact information"
            placeholder="Enter an email"
            min={1}
            max={150}
          />
        </div>
        <div className="my-6">
          <FormSelectField
            handleUpdateField={handleUpdateField}
            options={priorityLevelOptions}
            name={form.priorityLevel.name}
            value={form.priorityLevel.value}
            error={form.priorityLevel.error}
            label="Priority Level"
            errorField="Priority level"
            id={form.priorityLevel.name}
          />
        </div>
        <button type="submit" className="btn w-full">
          Submit
        </button>
      </form>
    </div>
  );
};

export default SuggestionForm;
