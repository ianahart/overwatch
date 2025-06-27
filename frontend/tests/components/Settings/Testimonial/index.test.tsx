import { screen, render, waitFor } from '@testing-library/react';
import { getLoggedInUser } from '../../../utils';
import userEvent from '@testing-library/user-event';
import Testimonial from '../../../../src/components/Settings/Testimonial';
import { server } from '../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../src/util';

describe('Testimonial', () => {
  const getForm = () => {
    return {
      getInput: () => screen.getByRole('textbox', { name: /name of person/i }),
      getTextarea: () => screen.getByLabelText(/testimonial/i),
      getSubmitBtn: () => screen.getByRole('button', { name: /add testimonial/i }),
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();

    const form = getForm();

    render(<Testimonial />, { wrapper });

    return {
      form,
      user: userEvent.setup(),
    };
  };

  it('should render header and form fields', () => {
    const { form } = renderComponent();

    const { getInput, getTextarea, getSubmitBtn } = form;

    expect(getInput()).toBeInTheDocument();
    expect(getTextarea()).toBeInTheDocument();
    expect(getSubmitBtn()).toBeInTheDocument();

    expect(screen.getByRole('heading', { name: /add testimonial/i, level: 2 })).toBeInTheDocument();
  });

  it('should show validation errors if fields are empty on submit', async () => {
    const { user, form } = renderComponent();

    await user.click(form.getSubmitBtn());

    await waitFor(() => {
      expect(screen.getByText('name must be present and be of maximum 100 chars.'));
      expect(screen.getByText('text must be present and be of maximum 300 chars.'));
    });
  });

  it('should successfully submit a testimonial and show toast', async () => {
    const { user, form } = renderComponent();

    const { getInput, getTextarea, getSubmitBtn } = form;

    await user.type(getInput(), 'john doe');
    await user.type(getTextarea(), 'this is a testimonial');

    await user.click(getSubmitBtn());

    await waitFor(() => {
      expect(screen.getByText(/your testimonial was successfully added!/i)).toBeInTheDocument();
    });
  });
  it('shows server errors if API responds with 400', async () => {
    const { user, form } = renderComponent();

    server.use(
      http.post(`${baseURL}/testimonials`, async () => HttpResponse.json({ name: 'Name is required' }, { status: 400 }))
    );

    await user.type(form.getInput(), 'Alice');
    await user.type(form.getTextarea(), 'Nice work');

    await user.click(form.getSubmitBtn());

    await waitFor(() => {
      expect(screen.getByText(/name is required/i)).toBeInTheDocument();
    });
  });
});
