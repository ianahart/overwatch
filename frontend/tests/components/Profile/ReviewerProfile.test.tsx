import { screen, render } from '@testing-library/react';

import ReviewerProfile from '../../../src/components/Profile/ReviewerProfile';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';
import { toPlainObject } from 'lodash';
import { IFullProfile } from '../../../src/interfaces';

describe('ReviewerProfile', () => {
  const getProps = () => {
    const profileEntity = db.fullProfile.create();
    const userProfile = {
      id: 1,
      userId: 2,
      role: 'REVIEWER',
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

    render(<ReviewerProfile {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render without crashing', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.profile.basicInfo.fullName)).toBeInTheDocument();
  });
});
