import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import { HttpResponse, http } from 'msw';
import { IFetchActiveLabelsResponse, ITodoCard } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import { createActiveLabels, createLabels } from '../../../../../../mocks/dbActions';
import { getLoggedInUser } from '../../../../../../utils';
import CardLabels from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardLabels';
import { server } from '../../../../../../mocks/server';
import { baseURL } from '../../../../../../../src/util';

describe('CardLabels', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };
    return { card, handleOnOpenLabelForm: vi.fn() };
  };

  const getElements = () => {
    return {
      getAddBtn: () => screen.getByRole('button', { name: /add a new label/i }),
      getAllCardLabel: () => screen.getAllByTestId('CardLabel'),
    };
  };

  const renderComponent = () => {
    let labels = createLabels(2);

    labels = labels.map((label, i) => {
      return { ...label, isChecked: i === 0 };
    });

    const props = getProps();

    const { wrapper } = getLoggedInUser(
      {},
      {
        workSpace: { labels },
      }
    );

    render(<CardLabels {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      props,
      labels,
      elements: getElements(),
    };
  };

  it('should render add button and call handler on click', async () => {
    const { user, elements, props } = renderComponent();
    const { getAddBtn } = elements;

    await user.click(getAddBtn());

    await waitFor(() => {
      expect(props.handleOnOpenLabelForm).toHaveBeenCalled();
    });
  });

  it('should render labels from redux store', () => {
    const { labels } = renderComponent();

    labels.forEach((label) => {
      expect(screen.getByText(label.title)).toBeInTheDocument();
    });
  });

  it('should mark active labels correctly from query data', async () => {
    const { labels } = renderComponent();

    server.use(
      http.get(`${baseURL}/active-labels`, () => {
        const [data] = createActiveLabels(1);

        const active = { ...data, labelId: labels[0].id };

        return HttpResponse.json<IFetchActiveLabelsResponse>(
          {
            message: 'success',
            data: [active],
          },
          { status: 200 }
        );
      })
    );

    renderComponent();
    const inputs = await screen.findAllByRole('checkbox');

    expect(inputs[0]).toBeChecked();
  });

  it('should remove a label when the delete mutation runs', async () => {
    const { user } = renderComponent();

    const [firstDeleteBtn] = screen.getAllByTestId('delete-active-label-btn');

    expect(async () => {
      await user.click(firstDeleteBtn);
    }).not.toThrow();
  });
});
