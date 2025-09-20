import { render, screen } from '@testing-library/react';
import BannedUserForm, {
  IBannedUserFormProps,
} from '../../../../../src/components/Dashboard/Routes/Admin/BannedUser/BannedUserForm';
import { getLoggedInUser } from '../../../../utils';
import userEvent from '@testing-library/user-event';

describe('BannedUserForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides: Partial<IBannedUserFormProps> = {}) => {
    return {
      formType: 'create',
      handleSetView: vi.fn(),
      banId: 1,
      updateBannedUserState: vi.fn(),
      ...overrides,
    };
  };

  const getElements = () => {
    return {
      getTextarea: () => screen.getByRole('textbox'),
      getSelect: () => screen.getByRole('combobox'),
      getBtn: () => screen.getByRole('button'),
    };
  };

  const renderComponent = (overrides: Partial<IBannedUserFormProps> = {}) => {
    const props = getProps(overrides);

    const { wrapper } = getLoggedInUser();

    render(<BannedUserForm {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      elements: getElements(),
    };
  };

  it('should render create mode with default values', () => {
    const { elements } = renderComponent({ banId: 0, formType: 'create' });

    expect(screen.getByRole('heading', { name: /ban a user/i, level: 3 })).toBeInTheDocument();
    expect(elements.getBtn()).toHaveTextContent('Create');
  });
});
