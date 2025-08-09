import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { ICustomField, IDropDownOption } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import CardCustomFieldItem from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardCustomFieldItem';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('CardCustomFieldItem', () => {
  const getProps = () => {
    const optionValues: string[] = ['low', 'medium', 'high'];
    const dropDownOptions: IDropDownOption[] = Array.from({ length: 3 }).map((_, i) => {
      const dropDownOption: IDropDownOption = {
        ...toPlainObject(db.dropDownOption.create()),
        id: i + 1,
        optionValue: optionValues[i],
      };
      return dropDownOption;
    });

    const customField: ICustomField = {
      ...toPlainObject(db.customField.create()),
      todoCardId: 1,
      userId: 1,
      dropDownOptions,
    };

    return { customField };
  };

  const renderComponent = () => {
    const props = getProps();
    render(<CardCustomFieldItem {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      getBtn: () => screen.getByTestId('card-custom-field-item-button'),
    };
  };

  it('should render the field name and is collapsed by default', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.customField.fieldName)).toBeInTheDocument();
    expect(screen.queryByText(props.customField.selectedValue)).not.toBeInTheDocument();
  });

  it('should expand when chevron is clicked and shows selected value', async () => {
    const { user, getBtn, props } = renderComponent();

    await user.click(getBtn());

    expect(await screen.findByTestId('card-custom-field-item-chevron-up')).toBeInTheDocument();
    expect(await screen.findByText(props.customField.selectedValue)).toBeInTheDocument();
  });

  it('should show all dropdown options when expanded', async () => {
    const { user, getBtn, props } = renderComponent();

    await user.click(getBtn());
    await waitFor(() => {
      props.customField.dropDownOptions.forEach((option) => {
        expect(screen.getByTestId(`card-custom-field-item-dropdown-option-${option.optionValue}`)).toBeInTheDocument();
      });
    });
  });

  it('should render checkboxes when fieldType is "CHECKBOX" ', async () => {
    const { user, getBtn, props } = renderComponent();

    await user.click(getBtn());

    const checkboxes = await screen.findAllByRole('checkbox');

    expect(checkboxes).toHaveLength(props.customField.dropDownOptions.length);
  });

  it('should toggle dropdown options to closed when chevron is clicked again', async () => {
    const { user, getBtn } = renderComponent();

    await user.click(getBtn());
    await user.click(getBtn());

    expect(await screen.findByTestId('card-custom-field-item-chevron-down')).toBeInTheDocument();
  });
});
