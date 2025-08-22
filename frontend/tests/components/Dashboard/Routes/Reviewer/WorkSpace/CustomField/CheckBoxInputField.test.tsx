import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';

import { getLoggedInUser } from '../../../../../../utils';
import CheckBoxInputField from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CustomField/CheckBoxInputField';
import { server } from '../../../../../../mocks/server';
import { baseURL } from '../../../../../../../src/util';

describe('CheckBoxInputField', () => {
  const getElements = () => {
    return {
      getHeading: () => screen.getByRole('heading', { level: 3 }),
      getInput: () => screen.getByRole('textbox'),
      getCreateBtn: () => screen.getByRole('button', { name: /create/i }),
      getOptionTrashIcons: () => screen.getAllByTestId('checkbox-input-field-option-trash-icon'),
      getSaveBtn: () => screen.getByRole('button', { name: /save/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const getProps = (overrides = {}) => {
    const options = [
      { id: '1', value: 'option1' },
      { id: '2', value: 'option2' },
    ];
    return {
      todoCardId: 1,
      options,
      customFieldType: {
        fieldName: 'list',
        fieldType: 'CHECKBOX',
        selectedTitle: 'list',
        selectedValue: 'option 1',
        options,
        ...overrides,
      },
      handleCloseClickAway: vi.fn(),
      addCustomFieldValue: vi.fn(),
      deleteOption: vi.fn(),
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);
    const { wrapper } = getLoggedInUser();

    render(<CheckBoxInputField {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render title, input, and buttons', () => {
    const { elements } = renderComponent();

    const { getInput, getHeading, getCancelBtn, getSaveBtn } = elements;

    expect(getInput()).toBeInTheDocument();
    expect(getHeading()).toBeInTheDocument();
    expect(getCancelBtn()).toBeInTheDocument();
    expect(getSaveBtn()).toBeInTheDocument();
  });

  it('should add a checkbox aftering clicking the create button', async () => {
    const { user, props, elements } = renderComponent();

    const { getInput, getCreateBtn } = elements;

    await user.clear(getInput());
    await user.type(getInput(), 'option3');
    await user.click(getCreateBtn());

    expect(props.addCustomFieldValue).toHaveBeenCalledWith(expect.objectContaining({ value: 'option3' }), 'CHECKBOX');
  });

  it('should show an error message if max checkboxes (10) is reached', async () => {
    const maxOptions = Array.from({ length: 11 }).map((_, i) => {
      return { id: String(i), value: `option${i}` };
    });
    const MAX_CHECKBOXES = 10;

    const { elements, user } = renderComponent({ options: maxOptions });

    await user.click(elements.getCreateBtn());

    expect(await screen.findByText(`You have added the maximum amount of checkboxes (${MAX_CHECKBOXES})`));
  });

  it('should call deleteOption when clicking trash', async () => {
    const { elements, user, props } = renderComponent();

    const { getInput, getCreateBtn, getOptionTrashIcons } = elements;

    const [firstOptionTrashIcon] = getOptionTrashIcons();

    await user.type(getInput(), 'checkbox123');
    await user.click(getCreateBtn());

    await user.click(firstOptionTrashIcon);

    await waitFor(() => {
      expect(props.deleteOption).toHaveBeenCalledWith('1');
    });
  });

  it('should submit successfully and close on success', async () => {
    const { props, user, elements } = renderComponent();

    await user.click(elements.getSaveBtn());

    await waitFor(() => {
      expect(props.handleCloseClickAway).toHaveBeenCalled();
    });
  });

  it('should display API server error if an error has occurred', async () => {
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
    const { user, elements } = renderComponent();

    await user.click(elements.getSaveBtn());

    expect(await screen.findByText('server error')).toBeInTheDocument();
  });
});
