import { screen, render, waitFor } from '@testing-library/react';

import { mockNavigate } from '../../../../setup';
import SuggestionForm from '../../../../../src/components/Dashboard/Routes/Shared/Suggestion/SuggestionForm';
import { getLoggedInUser } from '../../../../utils';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { server } from '../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../src/util';

export interface IForm {
  getSubmitBtn: () => HTMLElement;
  getTitleInput: () => HTMLElement;
  getContactInput: () => HTMLElement;
  getFeedbackTypeSelect: () => HTMLElement;
  getPrioritySelect: () => HTMLElement;
  getDescriptionTextarea: () => HTMLElement;
}

describe('SuggestionForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const submitForm = async (user: UserEvent, form: IForm) => {
    const {
      getSubmitBtn,
      getTitleInput,
      getContactInput,
      getPrioritySelect,
      getFeedbackTypeSelect,
      getDescriptionTextarea,
    } = form;

    await user.type(getTitleInput(), 'Bug in dashboard');
    await user.type(getDescriptionTextarea(), 'The dashboard crashes when clicking stats');
    await user.type(getContactInput(), 'test@example.com');
    await user.selectOptions(getFeedbackTypeSelect(), 'BUG_REPORT');
    await user.selectOptions(getPrioritySelect(), 'HIGH');

    await user.click(getSubmitBtn());
  };

  const getForm = () => {
    return {
      getSubmitBtn: () => screen.getByRole('button', { name: /submit/i }),
      getFeedbackTypeSelect: () => screen.getByLabelText(/feedback type/i),
      getTitleInput: () => screen.getByLabelText(/title/i),
      getDescriptionTextarea: () => screen.getByLabelText(/description/i),
      getContactInput: () => screen.getByLabelText(/contact/i),
      getPrioritySelect: () => screen.getByLabelText(/priority level/i),
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    render(<SuggestionForm />, { wrapper });

    return {
      user: userEvent.setup(),
      form: getForm(),
    };
  };

  it('should render all form fields', () => {
    const { form } = renderComponent();

    const {
      getSubmitBtn,
      getTitleInput,
      getContactInput,
      getPrioritySelect,
      getFeedbackTypeSelect,
      getDescriptionTextarea,
    } = form;

    expect(getSubmitBtn()).toBeInTheDocument();
    expect(getTitleInput()).toBeInTheDocument();
    expect(getContactInput()).toBeInTheDocument();
    expect(getPrioritySelect()).toBeInTheDocument();
    expect(getFeedbackTypeSelect()).toBeInTheDocument();
    expect(getDescriptionTextarea()).toBeInTheDocument();
  });

  it('should validate empty fields and prevent submission', async () => {
    const { user, form } = renderComponent();
    // feedback type and priority level are not included because they are select tags
    const fields = ['title', 'description', 'contact'];

    await user.click(form.getSubmitBtn());

    fields.forEach((field) => {
      expect(screen.getByText(`Make sure ${field} is filled out`)).toBeInTheDocument();
    });
  });

  it('should submit the form when valid and navigate back to the previous route', async () => {
    const { user, form } = renderComponent();

    await submitForm(user, form);

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(-1);
    });
  });

  it('should apply server errors correctly on a bad submission', async () => {
    const errorMessage = 'title is too short';
    server.use(
      http.post(`${baseURL}/suggestions`, () => {
        return HttpResponse.json(
          {
            title: errorMessage,
          },
          { status: 400 }
        );
      })
    );

    const { user, form } = renderComponent();

    await submitForm(user, form);

    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });
});
