import { screen, render, waitFor, fireEvent } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';

import { ITodoCard, IUpdateTodoCardResponse } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import CardUploadPhoto from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardUploadPhoto';
import { getLoggedInUser } from '../../../../../utils';
import { updateTodoListTodoCard } from '../../../../../../src/state/store';
import { server } from '../../../../../mocks/server';
import { baseURL } from '../../../../../../src/util';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('CardUploadPhoto', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), todoListId: 1, userId: 1 };

    return { card };
  };

  const renderComponent = () => {
    const props = getProps();

    const { wrapper } = getLoggedInUser();

    render(<CardUploadPhoto {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
    };
  };

  it('should render image when "uploadPhotoUrl is provided"', () => {
    const { props } = renderComponent();

    const photo = screen.getByRole('img');

    expect(photo).toHaveAttribute('src', props.card.uploadPhotoUrl);
  });

  it('should show an overlay on image hover', async () => {
    const { user } = renderComponent();

    const imgWrapper = screen.getByRole('img', { name: /card uploaded photo/i }).parentElement!;
    expect(screen.queryByText(/remove image/i)).not.toBeInTheDocument();

    await user.hover(imgWrapper);
    expect(screen.getByRole('img', { hidden: true })).toBeInTheDocument();
  });

  it('should show tooltip when hovering over close icon', () => {
    renderComponent();

    const imgWrapper = screen.getByRole('img', { name: /card uploaded photo/i }).parentElement!;
    fireEvent.mouseEnter(imgWrapper);

    const closeIcon = screen.getByTestId('card-upload-photo-close-icon');
    fireEvent.mouseEnter(closeIcon);

    expect(screen.getByText(/remove image/i)).toBeInTheDocument();
  });

  it('should update todocard to have no uploadPhotoUrl', async () => {
    server.use(
      http.put(`${baseURL}/todo-cards/:id`, () => {
        const data: ITodoCard = {
          ...toPlainObject(db.todoCard.create()),
          title: 'updated title',
          uploadPhotoUrl: null,
        };

        return HttpResponse.json<IUpdateTodoCardResponse>(
          {
            message: 'success',
            data,
          },
          { status: 200 }
        );
      })
    );

    const { user } = renderComponent();

    const imgWrapper = screen.getByRole('img', { name: /card uploaded photo/i }).parentElement!;
    fireEvent.mouseEnter(imgWrapper);

    const closeIcon = screen.getByTestId('card-upload-photo-close-icon');

    await user.click(closeIcon);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(
        updateTodoListTodoCard(expect.objectContaining({ uploadPhotoUrl: null }))
      );
    });
  });
});
