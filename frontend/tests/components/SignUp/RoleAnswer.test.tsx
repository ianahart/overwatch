import { render, screen } from '@testing-library/react';
import { getWrapper } from '../../RenderWithProviders';
import RoleAnswer from '../../../src/components/SignUp/RoleAnswer';
import { Role } from '../../../src/enums';
import userEvent from '@testing-library/user-event';

describe('RoleAnswer', () => {
  const renderComponent = (answer: string, role: Role, roleState: Role) => {
    const wrapper = getWrapper({ signup: { role: { value: roleState } } } as any);
    render(<RoleAnswer answer={answer} role={role} />, { wrapper });

    return {
      user: userEvent.setup(),
    };
  };
  it('should render the answer text when unassigned', () => {
    const answer = 'I am looking for someone to review my code';
    renderComponent(answer, Role.UNASSIGNED, Role.UNASSIGNED);

    expect(screen.getByText(answer)).toBeInTheDocument();
  });

  it('should dispatch updateRole when clicked', async () => {
    const answer = 'I am looking for someone to review my code';
    const { user } = renderComponent(answer, Role.UNASSIGNED, Role.UNASSIGNED);

    await user.click(screen.getByText(answer));

    expect(screen.getByLabelText('checkmark')).toBeInTheDocument();
  });

  it('should not render if roleState is assigned to a different role', () => {
    const answer = 'I am looking to review the code of others';
    renderComponent(answer, Role.REVIEWER, Role.USER);

    expect(screen.queryByText(answer)).toBeNull();
  });
});
