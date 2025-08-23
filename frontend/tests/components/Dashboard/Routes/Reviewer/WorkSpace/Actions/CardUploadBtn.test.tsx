import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { HttpResponse, http } from 'msw';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

import { ITodoCard } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import { getLoggedInUser } from '../../../../../../utils';
import CardUploadBtn from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardUploadBtn';
import userEvent from '@testing-library/user-event';
import { updateTodoListTodoCard } from '../../../../../../../src/state/store';
import { server } from '../../../../../../mocks/server';
import { baseURL } from '../../../../../../../src/util';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CardUploadBtn', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };

    return { card };
  };

  const getForm = () => {
    return {
      getHeading: () => screen.getByRole('heading', { level: 3 }),
      getCloseBtn: () => screen.getByTestId('card-upload-close-btn'),
      getOpenBtn: () => screen.getByRole('button', { name: /upload/i }),
      getUploadBtn: () => screen.getByRole('button', { name: /upload here/i }),
      getInput: () => screen.getByTestId('card-upload-input'),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<CardUploadBtn {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      form: getForm(),
    };
  };

  it('should render open button', () => {
    const { form } = renderComponent();

    const { getOpenBtn } = form;

    expect(getOpenBtn()).toBeInTheDocument();
  });

  it('should  render form elements and heading', async () => {
    const { user, form } = renderComponent();

    const { getInput, getOpenBtn, getHeading, getCloseBtn } = form;

    await user.click(getOpenBtn());

    await waitFor(() => {
      expect(getInput()).toBeInTheDocument();
      expect(getHeading()).toBeInTheDocument();
      expect(getCloseBtn()).toBeInTheDocument();
    });
  });

  it('should show error if file is too large', async () => {
    const { user, form } = renderComponent();

    const { getOpenBtn, getInput } = form;

    await user.click(getOpenBtn());

    const file = new File(['big'], 'big.png', { type: 'image/png' });
    Object.defineProperty(file, 'size', { value: 2_000_000 });

    await user.upload(getInput(), file);
    expect(await screen.findByText(/max file size/i)).toBeInTheDocument();
  });

  it('should upload file successfully', async () => {
    const { user, form } = renderComponent();

    const { getOpenBtn, getInput } = form;

    await user.click(getOpenBtn());

    const file = new File(['photo'], 'photo.png', { type: 'image/png' });
    Object.defineProperty(file, 'size', { value: 5_000 });

    await user.upload(getInput(), file);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        updateTodoListTodoCard(expect.objectContaining({ photo: 'https://upload.com/photo' }))
      );
    });
  });

  it('should show server error on failed upload', async () => {
    server.use(
      http.patch(`${baseURL}/todo-cards/:todoCardId/upload`, () => {
        return HttpResponse.json(
          {
            message: 'photo goes beyond max file size',
          },
          { status: 400 }
        );
      })
    );
    const { user, form } = renderComponent();

    const { getOpenBtn, getInput } = form;

    await user.click(getOpenBtn());

    const file = new File(['photo'], 'photo.png', { type: 'image/png' });
    Object.defineProperty(file, 'size', { value: 5_000 });

    await user.upload(getInput(), file);

    expect(await screen.findByText(/max file size/i)).toBeInTheDocument();
  });
});
