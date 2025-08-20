import { screen, render, waitFor } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../../utils';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';

import DropDownInputField from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CustomField/DropDownInputField';
import { server } from '../../../../../../mocks/server';
import { baseURL } from '../../../../../../../src/util';

describe('DropDownInputField', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides = {}) => {
    const options = [
      { id: '1', value: 'bananas' },
      { id: '2', value: 'apples' },
    ];
    return {
      todoCardId: 1,
      options,
      handleCloseClickAway: vi.fn(),
      addCustomFieldValue: vi.fn(),
      deleteOption: vi.fn(),
      customFieldType: {
        fieldName: 'groceries',
        fieldType: 'DROPDOWN',
        selectedTitle: 'groceries',
        selectedValue: 'apples',
        dropDownOptions: options,
      },
      ...overrides,
    };
  };

  const getForm = () => {
    return {
      getHeading: () => screen.getByRole('heading', { level: 3 }),
      getInput: () => screen.getByPlaceholderText('Add option...'),
      getCreateBtn: () => screen.getByRole('button', { name: /create/i }),
      getSaveBtn: () => screen.getByRole('button', { name: /save/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    const { wrapper } = getLoggedInUser();

    render(<DropDownInputField {...props} />, { wrapper });

    return {
      props,
      form: getForm(),
      user: userEvent.setup(),
    };
  };

  it('should render title, placeholder, and buttons', () => {
    const { form } = renderComponent();

    const { getInput, getHeading, getSaveBtn, getCancelBtn, getCreateBtn } = form;

    expect(getInput()).toBeInTheDocument();
    expect(getHeading()).toBeInTheDocument();
    expect(getSaveBtn()).toBeInTheDocument();
    expect(getCancelBtn()).toBeInTheDocument();
    expect(getCreateBtn()).toBeInTheDocument();
  });

  it('should add an option and clear input', async () => {
    const { user, form, props } = renderComponent();

    const { getInput, getCreateBtn } = form;

    await user.type(getInput(), 'pears');

    await user.click(getCreateBtn());

    expect(props.addCustomFieldValue).toHaveBeenCalledWith(
      expect.objectContaining({ value: 'pears' }),
      props.customFieldType.fieldType
    );
    expect(getInput()).toHaveValue('');
  });

  it('should show error when max options exceeded', async () => {
    const { user, props, form } = renderComponent({
      options: Array.from({ length: 10 }).map((_, i) => ({ id: String(i), value: `Opt ${i}` })),
    });
    await user.type(form.getInput(), 'max option');
    await user.click(form.getCreateBtn());

    expect(await screen.findByText(/maximum amount of options/i)).toBeInTheDocument();
    expect(props.addCustomFieldValue).not.toHaveBeenCalled();
  });

  it('should delete an option when trash icon is clicked', async () => {
    const { user, props } = renderComponent();

    const [trashIcon] = Array.from(screen.getAllByTestId('dropdown-input-field-trash-icon'));

    await user.click(trashIcon);

    expect(props.deleteOption).toHaveBeenCalledWith(props.options[0].id);
  });

  it('should submit and close on success', async () => {
    const { user, form, props } = renderComponent();

    const { getInput, getSaveBtn, getCreateBtn } = form;

    await user.type(getInput(), 'pears');
    await user.click(getCreateBtn());
    await user.click(getSaveBtn());

    await waitFor(() => {
      expect(props.handleCloseClickAway).toHaveBeenCalled();
    });
  });

  it('should show server error on mutation failure', async () => {
    server.use(
      http.post(`${baseURL}/custom-fields`, () => {
        return HttpResponse.json(
          {
            message: 'server error',
          },
          { status: 400 }
        );
      })
    );
    const { user, form } = renderComponent();

    const { getInput, getSaveBtn } = form;

    await user.type(getInput(), '123');
    await user.click(getSaveBtn());

    expect(await screen.findByText('server error')).toBeInTheDocument();
  });
});
