import { screen, render } from '@testing-library/react';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { getLoggedInUser } from '../../../../../utils';
import AppTestimonial from '../../../../../../src/components/Dashboard/Routes/User/AppTestimonial';

export interface IForm {
  getHeading: () => HTMLElement;
  getTextArea: () => HTMLElement;
  getQuestion: () => HTMLElement;
  getSubmitBtn: () => Promise<HTMLElement>;
  getRadioBtns: () => HTMLElement[];
}

vi.mock('react-spring', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-spring')>();
  return {
    ...actual,
    animated: {
      div: (props: any) => <div {...props} />,
      span: (props: any) => <span {...props} />,
    },
    useSpring: () => ({}),
  };
});

vi.mock('react-toastify', async (importOriginal) => {
  const actual = await importOriginal<typeof import('react-toastify')>();
  return {
    ...actual,
    toast: {
      ...actual.toast,
      success: vi.fn(),
    },
    ToastContainer: () => <div data-testid="toast-container">ToastContainer</div>,
  };
});

describe('AppTestimonial', () => {
  const submitForm = async (user: UserEvent, form: IForm, contentLength: number) => {
    const { getTextArea, getRadioBtns, getSubmitBtn } = form;

    const radios = getRadioBtns();

    await user.click(radios[0]);
    await user.type(getTextArea(), 'a'.repeat(contentLength));

    await user.click(await getSubmitBtn());
  };

  const getForm = () => ({
    getHeading: () => screen.getByRole('heading', { name: /tell us about your journey/i }),
    getTextArea: () => screen.getByLabelText(/share your story/i),
    getQuestion: () => screen.getByText(/what type of developer are you?/i),
    getSubmitBtn: () => screen.findByRole('button', { name: /submit|update/i }),
    getRadioBtns: () => screen.getAllByRole('radio'),
  });

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    render(<AppTestimonial />, { wrapper });

    return {
      curUser,
      user: userEvent.setup(),
      form: getForm(),
    };
  };

  it('should render form fields and radio buttons', async () => {
    const { form } = renderComponent();
    const { getHeading, getTextArea, getQuestion, getRadioBtns } = form;

    expect(getHeading()).toBeInTheDocument();
    expect(getTextArea()).toBeInTheDocument();
    expect(getQuestion()).toBeInTheDocument();
    expect(getRadioBtns().length).toBeGreaterThan(1);
  });

  it('should show submit button when fields are filled', async () => {
    const { form } = renderComponent();

    expect(await form.getSubmitBtn()).toBeInTheDocument();
  });

  it('should show error if content is too long', async () => {
    const { user, form } = renderComponent();

    await submitForm(user, form, 201);

    expect(await screen.findByText(/testimonial must be between/i)).toBeInTheDocument();
  });

  it('should update the testimonial when fields are filled out and submit is clicked', async () => {
    const { user, form } = renderComponent();

    await submitForm(user, form, 20);

    expect(await screen.findByTestId('toast-container')).toBeInTheDocument();
  });
});
