import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { IReviewer } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import SelectedUserBlock from '../../../../../../src/components/Dashboard/Routes/Admin/BannedUser/SelectedUserBlock';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('SelectedUserBlock', () => {
  const getProps = (overrides: Partial<IReviewer> = {}) => {
    const selectedUser: IReviewer = { ...toPlainObject(db.reviewer.create()), ...overrides };
    return { selectedUser };
  };

  const renderComponent = (overrides: Partial<IReviewer> = {}) => {
    const props = getProps(overrides);

    render(<SelectedUserBlock {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
    };
  };

  it('should render fallback when no user is selected', () => {
    renderComponent({ id: 0 });

    expect(screen.getByText(/you haven't selected anyone yet./i)).toBeInTheDocument();
  });

  it('should render the user details', () => {
    const { props } = renderComponent();

    const { avatarUrl, id, fullName } = props.selectedUser;

    const avatar = screen.getByRole('img');
    expect(avatar).toHaveAttribute('src', avatarUrl);
    expect(screen.getByText(`User Id: ${id}`)).toBeInTheDocument();
    expect(screen.getByText(fullName)).toBeInTheDocument();
  });
});
