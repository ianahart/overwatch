import { render, screen, waitFor } from '@testing-library/react';
import BannedUserForm, {
  IBannedUserFormProps,
} from '../../../../../../src/components/Dashboard/Routes/Admin/BannedUser/BannedUserForm';
import { getLoggedInUser } from '../../../../../utils';
import userEvent, { UserEvent } from '@testing-library/user-event';
import { server } from '../../../../../mocks/server';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../../../../src/util';

export interface IBannedUserFormElements {
  getTextarea: () => HTMLElement;
  getBtn: () => HTMLElement;
  getChildInput: () => HTMLElement;
  getSelect: () => HTMLElement;
}

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
      getChildInput: () => screen.getByLabelText(/select a user/i),
      getTextarea: () => screen.getByTestId('admin-notes-textarea'),
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

  const fillOutForm = async (elements: IBannedUserFormElements, user: UserEvent) => {
    const { getBtn, getSelect, getChildInput, getTextarea } = elements;
    await user.type(getChildInput(), 'john doe');
    const [firstBannedUserSearch] = await screen.findAllByTestId('banned-user-search-item');

    await user.click(firstBannedUserSearch);
    await user.type(getTextarea(), 'new admin notes');
    await user.selectOptions(getSelect(), '86400');
    await user.click(getBtn());
  };

  it('should render create mode with default values', () => {
    const { elements } = renderComponent({ banId: 0, formType: 'create' });

    expect(screen.getByRole('heading', { name: /ban a user/i, level: 3 })).toBeInTheDocument();
    expect(elements.getBtn()).toHaveTextContent('Create');
  });

  it('should submit create form and call "createBannedUser"', async () => {
    const { user, elements, props } = renderComponent({ banId: 0, formType: 'create' });

    fillOutForm(elements, user);

    await waitFor(() => {
      expect(props.handleSetView).toHaveBeenCalledWith('list');
    });
  });

  it('should render edit mode and fetch banned users', async () => {
    const { elements } = renderComponent({ banId: 1, formType: 'edit' });

    expect(await screen.findByText(/you have selected:/i)).toBeInTheDocument();
    expect(await screen.findByText(/john doe/i)).toBeInTheDocument();
    expect(elements.getBtn()).toHaveTextContent('Update');
  });

  it('should submit edit form and call "updateBannedUser"', async () => {
    const { user, elements, props } = renderComponent({ banId: 1, formType: 'edit' });

    await waitFor(() => {
      expect(screen.getByDisplayValue('existing notes')).toBeInTheDocument();
    });

    await user.click(elements.getBtn());

    await waitFor(() => {
      expect(props.updateBannedUserState).toHaveBeenCalledWith(
        expect.objectContaining({ adminNotes: 'updated notes' })
      );
    });
  });

  it('should display API errors when create fails', async () => {
    server.use(
      http.post(`${baseURL}/admin/banned-users`, async () => {
        return HttpResponse.json(
          {
            message: 'create error',
          },
          { status: 400 }
        );
      })
    );

    const { user, elements } = renderComponent({ banId: 0, formType: 'create' });

    fillOutForm(elements, user);

    expect(await screen.findByText('create error')).toBeInTheDocument();
  });
});
