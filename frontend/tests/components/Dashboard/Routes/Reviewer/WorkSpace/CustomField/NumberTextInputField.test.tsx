import { screen, render, waitFor } from '@testing-library/react';
import { getLoggedInUser } from '../../../../../../utils';
import NumberTextInputField from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CustomField/NumberTextInputField';
import userEvent from '@testing-library/user-event';
import { server } from '../../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../../src/util';

describe('NumberTextInputField', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    return {
      todoCardId: 1,
      customFieldType: {
        fieldName: 'scores',
        fieldType: 'NUMBER',
        selectedTitle: 'scores',
        selectedValue: '100',
        dropDownOptions: [],
      },
      handleCloseClickAway: vi.fn(),
      addCustomFieldValue: vi.fn(),
    };
  };

  const getForm = () => {
    return {
      getInput: () => screen.getByPlaceholderText('Add Number...'),
      getSaveBtn: () => screen.getByRole('button', { name: /save/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
      getHeading: () => screen.getByRole('heading', { level: 3 }),
    };
  };

  const renderComponent = () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();

    render(<NumberTextInputField {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      form: getForm(),
    };
  };

  it('should render with correct title and placeholder', () => {
    const { props, form } = renderComponent();

    const { getInput, getHeading } = form;

    expect(getHeading()).toHaveTextContent(props.customFieldType.selectedTitle);
    expect(getInput()).toHaveAttribute('placeholder', 'Add Number...');
  });

  it('should show validation error if form is submitted empty', async () => {
    const { user, form } = renderComponent();

    await user.click(form.getSaveBtn());

    expect(await screen.findByText(/value must be under 50 characters/i)).toBeInTheDocument();
  });

  it('should call "addCustomFieldValue" on valid input form submission', async () => {
    const { user, form, props } = renderComponent();

    const { getInput, getSaveBtn } = form;
    const inputValue = '123';

    await user.type(getInput(), inputValue);

    await user.click(getSaveBtn());

    expect(props.addCustomFieldValue).toHaveBeenCalledWith(inputValue, props.customFieldType.fieldType);
  });

  it('should show validation error if input exceeds 50 characters', async () => {
    const { user, form } = renderComponent();

    const { getInput, getSaveBtn } = form;
    await user.type(getInput(), 'a'.repeat(50));

    await user.click(getSaveBtn());

    expect(await screen.findByText(/must be under 50 characters/i)).toBeInTheDocument();
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

  it('should call "handleCloseClickAway" when cancel is clicked', async () => {
    const { user, form, props } = renderComponent();

    await user.click(form.getCancelBtn());

    await waitFor(() => {
      expect(props.handleCloseClickAway).toHaveBeenCalled();
    });
  });
});
