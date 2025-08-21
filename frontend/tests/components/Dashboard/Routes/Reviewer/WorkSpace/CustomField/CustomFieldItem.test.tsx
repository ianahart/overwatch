import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import CustomFieldItem, {
  ICustomFieldItemProps,
} from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CustomField/CustomFieldItem';
import { AllProviders } from '../../../../../../AllProviders';

describe('CustomFieldItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides: Partial<ICustomFieldItemProps> = {}) => {
    const props: ICustomFieldItemProps = {
      customField: {
        userId: 1,
        todoCardId: 1,
        id: 1,
        fieldName: 'Priority',
        fieldType: 'DROPDOWN',
        selectedValue: '',
        isActive: true,
        dropDownOptions: [
          { id: 101, optionValue: 'High', customFieldId: 1 },
          { id: 102, optionValue: 'Low', customFieldId: 1 },
        ],
      },
      deleteCustomField: vi.fn(),
      deleteDropDownOption: vi.fn(),
      updateCustomFieldActive: vi.fn(),
      ...overrides,
    };
    return props;
  };

  const getElements = () => {
    return {
      getInput: () => screen.getByRole('checkbox'),
      getTrigger: () => screen.getByTestId('custom-field-item-dropdown'),
      getTrashIcon: () => screen.getByTestId('custom-field-item-trash-icon'),
      getOptionTrashIcons: () => screen.getAllByTestId('custom-field-item-option-trash-icon'),
      getDropDownIcon: () => screen.getByTestId('custom-field-item-rendered-icon'),
    };
  };

  const renderComponent = (overrides: Partial<ICustomFieldItemProps> = {}) => {
    const props = getProps(overrides);

    render(<CustomFieldItem {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render field name and correct icon', () => {
    const { props, elements } = renderComponent();

    expect(screen.getByText(props.customField.fieldName)).toBeInTheDocument();
    expect(elements.getDropDownIcon()).toBeInTheDocument();
  });

  it('should call "updateCustomFieldActive" when checkbox is toggled', async () => {
    const { user, elements, props } = renderComponent();

    await user.click(elements.getInput());

    await waitFor(() => {
      expect(props.updateCustomFieldActive).toHaveBeenCalledWith(1, false);
    });
  });

  it('should call "deleteCustomField" when delete button is clicked', async () => {
    const { user, props, elements } = renderComponent();

    await user.click(elements.getTrashIcon());

    await waitFor(() => {
      expect(props.deleteCustomField).toHaveBeenCalledWith(1);
    });
  });

  it('should expand to show dropdown options when chevron is clicked', async () => {
    const { user, props, elements } = renderComponent();

    await user.click(elements.getTrigger());

    const [optionOne, optionTwo] = props.customField.dropDownOptions;

    expect(await screen.findByText(optionOne.optionValue)).toBeInTheDocument();
    expect(await screen.findByText(optionTwo.optionValue)).toBeInTheDocument();
  });

  it('should call "deleteDropDownOption" when dropdown option delete is clicked', async () => {
    const { user, props, elements } = renderComponent();

    const { getTrigger, getOptionTrashIcons } = elements;

    await user.click(getTrigger());

    const [firstOptionTrashIcon] = getOptionTrashIcons();

    await user.click(firstOptionTrashIcon);

    await waitFor(() => {
      expect(props.deleteDropDownOption).toHaveBeenCalledWith(
        props.customField.dropDownOptions[0].id,
        props.customField.id
      );
    });
  });

  it('should render selectedValue when provided instead of options', async () => {
    const { elements } = renderComponent({
      customField: {
        userId: 1,
        todoCardId: 1,
        id: 2,
        fieldName: 'Score',
        fieldType: 'NUMBER',
        selectedValue: '42',
        isActive: true,
        dropDownOptions: [],
      },
    });

    expect(screen.getByText('Score')).toBeInTheDocument();
    await userEvent.click(elements.getTrigger());
    expect(screen.getByText('42')).toBeInTheDocument();
  });
});
