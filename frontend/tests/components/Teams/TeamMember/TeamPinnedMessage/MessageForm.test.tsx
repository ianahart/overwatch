import { faker } from '@faker-js/faker';
import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { db } from '../../../../mocks/db';
import MessageForm, {
  IMessageFormProps,
} from '../../../../../src/components/Teams/TeamMember/TeamPinnedMessage/MessageForm';
import { getLoggedInUser } from '../../../../utils';
import userEvent from '@testing-library/user-event';
import { server } from '../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../src/util';

describe('MessageForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getForm = () => {
    return {
      getCancelButton: () => screen.getByRole('button', { name: /cancel/i }),
      getSubmitButton: () => screen.getByRole('button', { name: /update|create/i }),
      getTextarea: () => screen.getByRole('textbox'),
    };
  };

  const getProps = (overrides: Partial<IMessageFormProps> = {}) => {
    return {
      message: faker.lorem.sentence(10),
      formType: 'create',
      team: toPlainObject(db.team.create()),
      closeModal: vi.fn(),
      teamPinnedMessageId: 1,
      ...overrides,
    };
  };

  const renderComponent = (overrides: Partial<IMessageFormProps> = {}) => {
    const { curUser, wrapper } = getLoggedInUser();

    const props = getProps(overrides);
    const form = getForm();

    render(<MessageForm {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      curUser,
      props,
      form,
    };
  };

  it('should render correctly in create mode', () => {
    renderComponent({ formType: 'create' });

    expect(screen.getByRole('button', { name: /create/i })).toBeInTheDocument();
    expect(screen.getByRole('heading', { level: 3, name: /create new message/i })).toBeInTheDocument();
  });

  it('should render correctly in update mode', () => {
    renderComponent({ formType: 'update' });

    expect(screen.getByRole('button', { name: /update/i })).toBeInTheDocument();
    expect(screen.getByRole('heading', { level: 3, name: /update message/i })).toBeInTheDocument();
  });

  it('should show validation error for empty message', async () => {
    const { user, form } = renderComponent({ formType: 'create' });

    await user.clear(form.getTextarea());

    await user.click(form.getSubmitButton());

    expect(await screen.findByText('Message must be between 1 and 100 characters')).toBeInTheDocument();
  });

  it('should show validation error for too long of a message', async () => {
    const { user, form } = renderComponent({ formType: 'create' });

    await user.type(form.getTextarea(), 'a'.repeat(101));

    await user.click(form.getSubmitButton());

    expect(await screen.findByText('Message must be between 1 and 100 characters'));
  });

  it('should submit form on successful create and close modal', async () => {
    const { props, user, form } = renderComponent({ formType: 'create' });

    await user.type(form.getTextarea(), 'this is a test message');

    await user.click(form.getSubmitButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });

  it('should submit form on successful update and close modal', async () => {
    const { props, user, form } = renderComponent({ formType: 'update' });

    await user.type(form.getTextarea(), 'updated message');

    await user.click(form.getSubmitButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });

  it('should display server-side error on failed submission', async () => {
    server.use(
      http.post(`${baseURL}/teams/:teamId/team-pinned-messages`, () => {
        return HttpResponse.json(
          {
            message: 'this is an error',
          },
          { status: 400 }
        );
      })
    );

    const { user, form } = renderComponent();

    await user.click(form.getSubmitButton());

    expect(await screen.findByText('this is an error')).toBeInTheDocument();
  });

  it('should close the modal when the cancel button is clicked', async () => {
    const { user, form, props } = renderComponent();

    await user.type(form.getTextarea(), 'test');
    await user.click(form.getCancelButton());

    await waitFor(() => {
      expect(props.closeModal).toHaveBeenCalled();
    });
  });
});
