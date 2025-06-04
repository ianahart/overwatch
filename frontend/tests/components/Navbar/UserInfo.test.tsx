import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import UserInfo from '../../../src/components/Navbar/UserInfo';
import { getLoggedInUser } from '../../utils';
import { IUser } from '../../../src/interfaces';

describe('UserInfo', () => {
  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const user: IUser = toPlainObject(curUser);

    render(<UserInfo user={user} />, { wrapper });

    return {
      user,
    };
  };

  it('should render the avatar and user full name', () => {
    const { user } = renderComponent();

    expect(screen.getByRole('img')).toHaveAttribute('src', user.avatarUrl);
    expect(screen.getByText(user.fullName)).toBeInTheDocument();
  });
});
