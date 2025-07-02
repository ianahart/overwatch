import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import Profile from '../../../../src/components/Settings/Connects/Profile';
import { db } from '../../../mocks/db';
import { IConnection } from '../../../../src/interfaces';
import { getLoggedInUser } from '../../../utils';

describe('Profile', () => {
  const getCurrentConnection = () => {
    const currentConnection: IConnection = { ...toPlainObject(db.connection.create()), senderId: 1, receiverId: 2 };

    return currentConnection;
  };

  const getIcons = () => {
    return {
      getMailIcon: () => screen.getByTestId('settings-profile-mail-icon'),
      getPhoneIcon: () => screen.getByTestId('settings-profile-phone-icon'),
      getUserIcon: () => screen.getByTestId('settings-profile-user-icon'),
    };
  };

  const renderComponent = () => {
    const currentConnection = getCurrentConnection();

    const { wrapper } = getLoggedInUser(
      {},
      {
        chat: {
          currentConnection,
        },
      }
    );

    render(<Profile />, { wrapper });

    return {
      currentConnection,
      icons: getIcons(),
    };
  };

  it('should render profile icons', () => {
    const { icons } = renderComponent();

    const { getMailIcon, getUserIcon, getPhoneIcon } = icons;

    expect(getMailIcon()).toBeInTheDocument();
    expect(getUserIcon()).toBeInTheDocument();
    expect(getPhoneIcon()).toBeInTheDocument();
  });

  it('should render full name, email, phone number, and bio', () => {
    const { currentConnection } = renderComponent();
    const { firstName, lastName, email, phoneNumber, bio } = currentConnection;

    expect(screen.getByText(`${firstName} ${lastName}`)).toBeInTheDocument();
    expect(screen.getByText(email)).toBeInTheDocument();
    expect(screen.getByText(phoneNumber)).toBeInTheDocument();
    expect(screen.getByText(bio)).toBeInTheDocument();
  });
});
