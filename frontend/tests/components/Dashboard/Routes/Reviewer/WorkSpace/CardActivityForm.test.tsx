import { screen, render, waitFor } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../utils';
import CardActivityForm from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardActivityForm';
import userEvent from '@testing-library/user-event';
import { server } from '../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../src/util';

describe('CardActivityForm', () => {
  const getProps = () => {
    return {
      todoCardId: 1,
    };
  };

  const getForm = () => {
    return {
      getAvatar: () => screen.getByRole('img'),
      getInput: () => screen.getByRole('textbox'),
      getCreateBtn: () => screen.queryByRole('button', { name: /create/i }),
      getCancelBtn: () => screen.queryByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps();
    const form = getForm();

    render(<CardActivityForm {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      curUser,
      form,
    };
  };

  it('should render form and avatar', () => {
    const { form } = renderComponent();

    const { getInput, getAvatar } = form;

    expect(getInput()).toBeInTheDocument();
    expect(getAvatar()).toBeInTheDocument();
  });

  it('should show Create/Cancel button after typing', async () => {
    const { user, form } = renderComponent();

    const { getInput, getCancelBtn, getCreateBtn } = form;

    await user.type(getInput(), 'Ian read your comment');

    await waitFor(() => {
      expect(getCancelBtn()).toBeInTheDocument();
      expect(getCreateBtn()).toBeInTheDocument();
    });
  });

  it('should submit valid comment and clear input', async () => {
    const { user, form } = renderComponent();

    const { getInput, getCreateBtn } = form;

    await user.type(getInput(), 'Ian read your comment');

    const createBtn = getCreateBtn();

    if (createBtn !== null) {
      await user.click(createBtn);
    }

    await waitFor(() => {
      expect(getInput()).toHaveValue('');
    });
  });

  it('should cancel input on cancel button click', async () => {
    const { user, form } = renderComponent();

    const { getCancelBtn, getInput } = form;

    await user.type(getInput(), 'Ian read your comment');

    const cancelBtn = getCancelBtn();

    if (cancelBtn !== null) {
      await user.click(cancelBtn);
    }

    await waitFor(() => {
      expect(getInput()).toHaveValue('');
    });
  });

  it('should prevent form submission when input is empty', async () => {
    const { user, form } = renderComponent();

    const createBtn = form.getCreateBtn();

    if (createBtn !== null) {
      await user.click(createBtn);
    }

    expect(screen.queryByText(/invalid/i)).not.toBeInTheDocument();
  });

  it('should show API errors if necessary', async () => {
    server.use(
      http.post(`${baseURL}/activities`, () => {
        return HttpResponse.json(
          {
            messsage: 'error creating comment',
          },
          { status: 400 }
        );
      })
    );
    const { user, form } = renderComponent();

    const { getInput, getCreateBtn } = form;

    await user.type(getInput(), 'Ian read your comment');

    const createBtn = getCreateBtn();

    if (createBtn !== null) {
      await user.click(createBtn);
    }

    expect(await screen.findByText(/error creating comment/)).toBeInTheDocument();
  });
});
