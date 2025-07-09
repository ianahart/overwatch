import { screen, render } from '@testing-library/react';

import OutletContainer from '../../../src/components/Dashboard/OutletContainer';
import { getLoggedInUser } from '../../utils';
import { IUser } from '../../../src/interfaces';
import { toPlainObject } from 'lodash';

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    Outlet: () => <div data-testid="OutletContent">Mock Outlet Content</div>,
  };
});

describe('OutletContainer', () => {
  const getElements = (curUser: IUser) => {
    return {
      getEmail: () => screen.getByText(curUser.email),
      getFullName: () => screen.getByRole('heading', { name: `${curUser.firstName} ${curUser.lastName}`, level: 3 }),
      getOutlet: () => screen.getByTestId('OutletContent'),
      getDashboardAvatarComponent: () => screen.getByTestId('DashboardAvatar'),
    };
  };

  const renderComponent = () => {
    const { curUser, wrapper } = getLoggedInUser();

    const elements = getElements(toPlainObject(curUser));

    render(<OutletContainer />, { wrapper });

    return {
      curUser,
      elements,
    };
  };

  it('should render user full name and email', () => {
    const { elements } = renderComponent();

    const { getEmail, getFullName } = elements;

    expect(getFullName()).toBeInTheDocument();
    expect(getEmail()).toBeInTheDocument();
  });

  it('should render Dashboard Avatar', () => {
    const { elements } = renderComponent();

    expect(elements.getDashboardAvatarComponent()).toBeInTheDocument();
  });

  it('should render Outlet content', () => {
    const { elements } = renderComponent();

    expect(elements.getOutlet()).toBeInTheDocument();
    expect(screen.getByText('Mock Outlet Content')).toBeInTheDocument();
  });
});
