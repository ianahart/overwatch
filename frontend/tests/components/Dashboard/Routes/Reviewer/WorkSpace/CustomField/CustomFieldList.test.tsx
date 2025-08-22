import { screen, render } from '@testing-library/react';
import { createCustomFields } from '../../../../../../mocks/dbActions';
import CustomFieldList from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CustomField/CustomFieldList';
import { AllProviders } from '../../../../../../AllProviders';

describe('CustomFieldList', () => {
  const getProps = () => {
    const customFields = createCustomFields(2);

    return {
      customFields,
      deleteCustomField: vi.fn(),
      deleteDropDownOption: vi.fn(),
      updateCustomFieldActive: vi.fn(),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<CustomFieldList {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render the provided CustomFieldItem(s) components', () => {
    const { props } = renderComponent();

    const customFieldItems = screen.getAllByTestId('CustomFieldItem');

    expect(customFieldItems).toHaveLength(props.customFields.length);
  });
});
