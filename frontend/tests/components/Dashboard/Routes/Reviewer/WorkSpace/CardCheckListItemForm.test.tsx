import { screen, render, waitFor } from '@testing-library/react';
import { HttpResponse, http } from 'msw';
import userEvent from '@testing-library/user-event';

import { getLoggedInUser } from '../../../../../utils';
import CardCheckListItemForm from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardCheckListItemForm';
import { server } from '../../../../../mocks/server';
import { baseURL } from '../../../../../../src/util';

describe('CardCheckListItemForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getForm = () => {
    return {
      getInput: () => screen.getByLabelText(/new item/i),
      getAddBtn: () => screen.getByRole('button', { name: /add/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const getProps = () => {
    return {
      closeForm: vi.fn(),
      checkListId: 1,
      addCheckListItem: vi.fn(),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();
    const props = getProps();

    render(<CardCheckListItemForm {...props} />, { wrapper });

    return {
      curUser,
      props,
      user: userEvent.setup(),
      form: getForm(),
    };
  };

  it('should render form elements', () => {
    const { form } = renderComponent();

    const { getInput, getAddBtn, getCancelBtn } = form;

    expect(getInput()).toBeInTheDocument();
    expect(getAddBtn()).toBeInTheDocument();
    expect(getCancelBtn()).toBeInTheDocument();
  });

  it('should submit a valid form and call addCheckListItem and closeForm', async () => {
    const { user, form, props } = renderComponent();
    const title = 'new title';

    const { getInput, getAddBtn } = form;

    await user.type(getInput(), title);

    await user.click(getAddBtn());

    await waitFor(() => {
      // data comes from handler
      expect(props.addCheckListItem).toHaveBeenCalledWith({
        id: 1,
        userId: 1,
        checkListId: 1,
        title,
        isCompleted: false,
      });

      expect(props.closeForm).toHaveBeenCalled();
    });
  });

  it('should not submit when title is empty', async () => {
    const { user, form, props } = renderComponent();

    await user.click(form.getAddBtn());

    await waitFor(() => {
      expect(props.closeForm).not.toHaveBeenCalled();
    });
  });

  it('should call closeForm when cancel button is clicked', async () => {
    const { user, form, props } = renderComponent();

    await user.click(form.getCancelBtn());

    await waitFor(() => {
      expect(props.closeForm).toHaveBeenCalled();
    });
  });

  it('should display server errors on failure', async () => {
    server.use(
      http.post(`${baseURL}/checklist-items`, () => {
        return HttpResponse.json(
          {
            message: 'duplicate title',
          },
          { status: 400 }
        );
      })
    );
    const { user, form } = renderComponent();

    await user.type(form.getInput(), 'new title');
    await user.click(form.getAddBtn());

    expect(await screen.findByText('duplicate title')).toBeInTheDocument();
  });
});
