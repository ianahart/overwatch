import { screen, render } from '@testing-library/react';
import { faker } from '@faker-js/faker';
import dayjs from 'dayjs';
import { IViewUser } from '../../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../../utils';
import UserListItem from '../../../../../../src/components/Dashboard/Routes/Admin/AllUser/UserListItem';
import { initializeName } from '../../../../../../src/util';

describe('UserListItem', () => {
  const getProps = (overrides: Partial<IViewUser> = {}) => {
    const user: IViewUser = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      avatarUrl: faker.image.avatar(),
      role: faker.helpers.arrayElement(['USER', 'ADMIN', 'REVIEWER']),
      email: 'johndoe@example.com',
      createdAt: dayjs().toString(),
      ...overrides,
    };

    return { user };
  };

  const renderComponent = (overrides: Partial<IViewUser> = {}) => {
    const props = getProps(overrides);
    const { wrapper, curUser } = getLoggedInUser();

    render(<UserListItem {...props} />, { wrapper });

    return {
      props,
      curUser,
    };
  };

  it('should render user info correctly', () => {
    const { props } = renderComponent();
    const { avatarUrl, firstName, lastName, id, createdAt } = props.user;

    const avatar = screen.getByRole('img');
    expect(screen.getByText(`${firstName} ${lastName}`)).toBeInTheDocument();
    expect(screen.getByText(id.toString())).toBeInTheDocument();
    expect(avatar).toHaveAttribute('src', avatarUrl);
    expect(screen.getByText(dayjs(createdAt).format('MM/DD/YYYY'))).toBeInTheDocument();
  });

  it('should mark the current user with (You)', () => {
    const { props } = renderComponent();

    const { email } = props.user;

    expect(screen.getByText(`${email} (You)`)).toBeInTheDocument();
  });

  it('should show initials if avatar is missing', () => {
    const { props } = renderComponent({ avatarUrl: '' });
    const { firstName, lastName } = props.user;

    expect(screen.getByText(initializeName(firstName, lastName))).toBeInTheDocument();
  });
});
