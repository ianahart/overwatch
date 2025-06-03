import { screen, render } from '@testing-library/react';

import ContactInfo from '../../../src/components/Profile/ContactInfo';
import { db } from '../../mocks/db';
import { AllProviders } from '../../AllProviders';

describe('ContactInfo', () => {
  const getProps = () => {
    const profileEntity = db.fullProfile.create();
    const { basicInfo } = profileEntity;

    if (!basicInfo) {
      throw new Error('Missing basicInfo from profile');
    }

    const { userName, email, contactNumber } = basicInfo;

    if (!userName || !email || !contactNumber) {
      throw new Error('missing userName, email, or contact number');
    }

    return {
      userName,
      email,
      contactNumber,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<ContactInfo {...props} />, { wrapper: AllProviders });

    return {
      props,
      getHeading: () => screen.getByRole('heading', { level: 3 }),
      getUserIcon: () => screen.getByTestId('react-icon-user'),
      getPhoneIcon: () => screen.getByTestId('react-icon-phone'),
      getEmailIcon: () => screen.getByTestId('react-icon-email'),
    };
  };

  it('should render the icons correctly', () => {
    const { getUserIcon, getEmailIcon, getPhoneIcon } = renderComponent();

    expect(getUserIcon()).toBeInTheDocument();
    expect(getEmailIcon()).toBeInTheDocument();
    expect(getPhoneIcon()).toBeInTheDocument();
  });

  it('should render the the heading correctly', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });

  it('should render the props correctly', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.contactNumber)).toBeInTheDocument();
    expect(screen.getByText(props.userName)).toBeInTheDocument();
    expect(screen.getByText(props.email)).toBeInTheDocument();
  });
});
