import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import { db } from '../../../../../mocks/db';
import { ICheckListItem } from '../../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../../utils';
import CardCheckListItem from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CardCheckListItem';
import userEvent from '@testing-library/user-event';

describe('CardCheckListItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getElements = () => {
    return {
      getEditIcon: () => screen.getByTestId('card-checklist-item-edit-icon'),
      getRemoveIcon: () => screen.getByTestId('card-checklist-item-delete-icon'),
      getCheckbox: () => screen.getByRole('checkbox'),
      getInput: () => screen.getByTestId('card-checklist-item-edit-input'),
    };
  };

  const getProps = (overrides: Partial<ICheckListItem> = {}) => {
    const checkListItem: ICheckListItem = {
      ...toPlainObject(db.checkListItem.create()),
      checkListId: 1,
      userId: 1,
      ...overrides,
    };
    return {
      checkListItem,
      updateCheckListItem: vi.fn(),
      deleteCheckListItem: vi.fn(),
    };
  };

  const renderComponent = (overrides = {}) => {
    const { wrapper } = getLoggedInUser();
    const props = getProps(overrides);

    render(<CardCheckListItem {...props} />, { wrapper });

    return {
      props,
      elements: getElements(),
      user: userEvent.setup(),
    };
  };

  it('should render title and checkbox', () => {
    const { props, elements } = renderComponent();

    expect(screen.getByText(props.checkListItem.title)).toBeInTheDocument();
    expect(elements.getCheckbox()).toBeInTheDocument();
  });

  it('should call "updateCheckListItem" when checkbox is toggled', async () => {
    const { user, elements, props } = renderComponent({ isCompleted: false });

    await user.click(elements.getCheckbox());

    await waitFor(() => {
      expect(props.updateCheckListItem).toHaveBeenCalledWith(
        { ...props.checkListItem, isCompleted: true },
        'isCompleted'
      );
    });
  });

  it('should enter edit mode and updates title on blur', async () => {
    const { user, props, elements } = renderComponent();

    await user.click(elements.getEditIcon());

    await user.clear(elements.getInput());
    await user.type(elements.getInput(), 'updated title');
    await user.tab();

    await waitFor(() => {
      expect(props.updateCheckListItem).toHaveBeenCalledWith(
        { ...props.checkListItem, title: 'updated title' },
        'title'
      );
    });
  });

  it('should call "deleteCheckListItem" when delete icon is clicked', async () => {
    const { user, props, elements } = renderComponent();

    await user.click(elements.getRemoveIcon());

    await waitFor(() => {
      expect(props.deleteCheckListItem).toHaveBeenCalledWith(props.checkListItem);
    });
  });
});
