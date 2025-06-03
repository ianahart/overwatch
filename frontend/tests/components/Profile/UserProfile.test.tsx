import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import UserProfile from '../../../src/components/Profile/UserProfile';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';
import { IFullProfile } from '../../../src/interfaces';

describe('UserProfile', () => {
  const getProps = () => {
    const profileEntity = db.fullProfile.create();
    const userProfile = {
      id: 1,
      userId: 2,
      role: 'USER',
      country: 'USA',
      abbreviation: 'JA',
      city: 'Market',
    };
    const profile: IFullProfile = { ...toPlainObject(profileEntity), userProfile };

    return {
      profile,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<UserProfile {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render without crashing', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.profile.basicInfo.fullName)).toBeInTheDocument();
  });
});

