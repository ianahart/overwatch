import { screen, render } from '@testing-library/react';
import BannedUser from '../../../../../../src/components/Dashboard/Routes/Admin/BannedUser';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('BannedUser', () => {
  const renderComponent = () => {
    render(<BannedUser />, { wrapper: AllProviders });
    return {
      user: userEvent.setup(),
      getListBtn: () => screen.getByRole('button', { name: /banned user/i }),
      getCreateBtn: () => screen.getByRole('button', { name: /create ban/i }),
    };
  };

  it('should render buttons', () => {
    const { getListBtn, getCreateBtn } = renderComponent();

    expect(getListBtn()).toBeInTheDocument();
    expect(getCreateBtn()).toBeInTheDocument();
  });
});
