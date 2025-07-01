import { screen, render } from '@testing-library/react';

import LocationInfo from '../../../../src/components/Settings/ContactInfo/LocationInfo';
import { ILocationForm } from '../../../../src/interfaces';
import { AllProviders } from '../../../AllProviders';

describe('LocationInfo', () => {
  const renderComponent = () => {
    const form: ILocationForm = {
      address: { name: 'address', error: '', value: '123 Main St', type: 'text' },
      addressTwo: { name: 'addressTwo', error: '', value: '', type: 'text' },
      city: { name: 'city', error: '', value: 'Laconia', type: 'text' },
      country: { name: 'country', error: '', value: 'United States', type: 'text' },
      phoneNumber: { name: 'phoneNumber', error: '', value: '1111111111', type: 'text' },
      state: { name: 'state', error: '', value: 'MA', type: 'text' },
      zipCode: { name: 'zipCode', error: '', value: '12312', type: 'text' },
    };

    render(<LocationInfo form={form} />, { wrapper: AllProviders });

    return {
      form,
    };
  };

  it('should render form props', () => {
    const { form } = renderComponent();

    expect(screen.getByText(form.address.value)).toBeInTheDocument();
    expect(screen.getByText(`${form.city.value}, ${form.state.value} ${form.zipCode.value}`)).toBeInTheDocument();
    expect(screen.getByText(form.phoneNumber.value)).toBeInTheDocument();
  });
});
