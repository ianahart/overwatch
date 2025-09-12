import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';

import { getLoggedInUser } from '../../../../../utils';
import BadgeForm from '../../../../../../src/components/Dashboard/Routes/Admin/Badge/BadgeForm';
import { mockNavigate } from '../../../../../setup';
import { server } from '../../../../../mocks/server';
import { baseURL } from '../../../../../../src/util';

describe('BadgeForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (badgeId: number = 0, formType: string = 'create') => {
    return {
      formType,
      badgeId,
      handleCloseModal: vi.fn(),
    };
  };

  const getElements = () => {
    return {
      getTextarea: () => screen.getByLabelText(/description/i),
      getInput: () => screen.getByLabelText(/title/i),
      getBtn: () => screen.getByRole('button'),
      getUpload: () => screen.getByTestId('file-input'),
    };
  };

  const renderComponent = (badgeId: number = 0, formType: string = 'create') => {
    const props = getProps(badgeId, formType);
    const { wrapper, curUser } = getLoggedInUser();

    render(<BadgeForm {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      elements: getElements(),
      curUser,
    };
  };

  it('should render with "Submit button for create form"', () => {
    const { elements } = renderComponent(0, 'create');

    const { getBtn } = elements;

    expect(getBtn()).toBeInTheDocument();
    expect(getBtn()).toHaveTextContent('Submit');
  });

  it('should render with "Update" for edit form', () => {
    const { elements } = renderComponent(1, 'edit');

    const { getBtn } = elements;

    expect(getBtn()).toBeInTheDocument();
    expect(getBtn()).toHaveTextContent('Update');
  });

  it('should show validation errors when submitted empty', async () => {
    const { user, elements } = renderComponent(0, 'create');
    const { getBtn } = elements;

    await user.click(getBtn());

    expect(await screen.findAllByText(/cannot be empty/i)).toHaveLength(3);
  });

  it('should submit successfully in create mode', async () => {
    const { user, elements, curUser } = renderComponent(0, 'create');
    const { getBtn, getTextarea, getInput } = elements;

    const file = new File(['hello'], 'test.png', { type: 'image/png' });

    await user.type(getInput(), 'new badge title');
    await user.type(getTextarea(), 'new badge description');
    await user.upload(screen.getByTestId('file-input'), file);
    await user.click(getBtn());

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(`/admin/dashboard/${curUser.slug}/badges`);
    });
  });

  it('should fetch and populate fields in edit mode', async () => {
    const { elements } = renderComponent(1, 'edit');

    const { getTextarea, getInput } = elements;

    await waitFor(() => {
      expect(getTextarea()).toHaveDisplayValue('badge description');
      expect(getInput()).toHaveDisplayValue('badge title');
    });
  });

  it('should display errors from the API', async () => {
    server.use(
      http.post(`${baseURL}/admin/badges`, () => {
        return HttpResponse.json(
          {
            message: 'server error',
          },
          { status: 400 }
        );
      })
    );
    const { user, elements } = renderComponent(0, 'create');
    const { getBtn, getTextarea, getInput } = elements;

    const file = new File(['hello'], 'test.png', { type: 'image/png' });

    await user.type(getInput(), 'new badge title');
    await user.type(getTextarea(), 'new badge description');
    await user.upload(screen.getByTestId('file-input'), file);
    await user.click(getBtn());

    expect(await screen.findByText(/server error/i)).toBeInTheDocument();
  });
});
