import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { getLoggedInUser } from '../../../../../../utils';
import CardLabel from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardLabel';
import { ILabel, ITodoCard } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import userEvent from '@testing-library/user-event';

describe('CardLabel', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (isActiveLabel: boolean = true, isChecked: boolean = true) => {
    const card: ITodoCard = { ...toPlainObject(db.todoCard.create()), userId: 1, todoListId: 1 };
    const label: ILabel = { ...toPlainObject(db.label.create()), userId: 1, workSpaceId: 1, isChecked };
    const activeLabelIds = isActiveLabel ? [label.id] : [];
    const handleOnDeleteLabel = vi.fn();

    return {
      card,
      label,
      activeLabelIds,
      handleOnDeleteLabel,
    };
  };

  const getElements = (title: string) => {
    return {
      getInput: () => screen.getByRole('checkbox'),
      getTitle: () => screen.getByText(title),
      getTrashIcon: () => screen.getByTestId('delete-active-label-btn'),
    };
  };

  const renderComponent = (isActiveLabel: boolean = true, isChecked: boolean = true) => {
    const props = getProps(isActiveLabel, isChecked);
    const { wrapper } = getLoggedInUser();

    render(<CardLabel {...props} />, { wrapper });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(props.label.title),
    };
  };

  it('should render label title and background color', () => {
    const { elements, props } = renderComponent();

    const { getTitle } = elements;
    const { color } = props.label;

    expect(getTitle()).toBeInTheDocument();
    const titleContainer = getTitle().closest('div');
    expect(titleContainer).toHaveStyle({ background: color });
  });

  it('should render checkbox as checked when isChecked + activeLabelsId includes id', () => {
    const { elements } = renderComponent();

    const { getInput } = elements;

    expect(getInput()).toBeChecked();
  });

  it('should create an active label when checkbox is checked', async () => {
    const { user, elements } = renderComponent(false, false);

    await user.click(elements.getInput());
    const props = getProps();
    render(<CardLabel {...props} label={{ ...props.label, isChecked: true }} activeLabelIds={[props.label.id]} />, {
      wrapper: getLoggedInUser().wrapper,
    });

    await waitFor(() => {
      const inputs = screen.getAllByRole('checkbox');

      expect(inputs.some((input) => (input as HTMLInputElement).checked)).toBe(true);
    });
  });

  it('should delete an active label when checkbox is unchecked', async () => {
    const { user, elements } = renderComponent();

    await user.click(elements.getInput());

    const props = getProps();
    render(<CardLabel {...props} label={{ ...props.label, isChecked: false }} activeLabelIds={[]} />, {
      wrapper: getLoggedInUser().wrapper,
    });
    await waitFor(() => {
      screen.debug();
    });
  });

  it('should show trash icon if label is not checked', () => {
    const { elements } = renderComponent(false, false);

    const { getTrashIcon } = elements;

    expect(getTrashIcon()).toBeInTheDocument();
  });

  it('should not show trash icon if label is checked', () => {
    renderComponent();

    expect(screen.queryByTestId('delete-active-label-btn')).not.toBeInTheDocument();
  });
});
