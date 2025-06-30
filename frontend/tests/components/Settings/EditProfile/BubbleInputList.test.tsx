import { screen, render, waitFor } from '@testing-library/react';
import { Mock } from 'vitest';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';

import BubbleInputList, {
  IBubbleInputListProps,
} from '../../../../src/components/Settings/EditProfile/BubbleInputList';
import { ISkillsFormField } from '../../../../src/interfaces';
import { AllProviders } from '../../../AllProviders';
import { addToList, removeFromList } from '../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('BubbleInputList', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = (overrides: Partial<IBubbleInputListProps> = {}): IBubbleInputListProps => {
    const data: ISkillsFormField[] = [
      { id: '1', name: 'JavaScript' },
      { id: '2', name: 'Java' },
    ];

    return {
      listName: 'programmingLanguages',
      label: 'Fluent programming languages',
      htmlFor: 'programmingLanguage',
      id: 'programmingLanguage',
      data,
      ...overrides,
    };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    render(<BubbleInputList {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render label and input', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.label)).toBeInTheDocument();
    expect(screen.getByRole('textbox')).toBeInTheDocument();
  });

  it('should render list items correctly', () => {
    const { props } = renderComponent();

    const listItems = Array.from(screen.getAllByTestId('settings-bubble-input-list-item'));

    expect(listItems.length).toBe(2);

    listItems.forEach((listItem) => {
      props.data.forEach((language) => {
        expect(typeof language === 'string' && language === listItem.textContent);
      });
    });
  });

  it('should dispatch "addToList" action when Enter is pressed with valid input and list has less than 10 items', async () => {
    const { user, props } = renderComponent();
    const input = screen.getByRole('textbox');
    await user.type(input, 'TypeScript{Enter}');

    expect(mockDispatch).toHaveBeenCalledWith(addToList({ listName: props.listName, value: 'TypeScript' }));
    expect(input).toHaveValue('');
  });

  it('does NOT dispatch "addToList" if input is empty or whitespace', async () => {
    const { user } = renderComponent();

    const input = screen.getByRole('textbox');
    await user.type(input, '   {Enter}');

    expect(mockDispatch).not.toHaveBeenCalled();
  });

  it('does NOT dispatch addToList if list length is 10 or more', async () => {
    const data = Array.from({ length: 10 }, (_, i) => ({ id: i, name: `Skill${i}` }));
    const { user } = renderComponent({ data });

    const input = screen.getByRole('textbox');
    await user.type(input, 'ExtraSkill{Enter}');

    expect(mockDispatch).not.toHaveBeenCalled();
  });

  it('dispatches removeFromList with correct payload when remove icon is clicked', async () => {
    const { user, props } = renderComponent();

    const item = screen.getByText('JavaScript');
    expect(item).toBeInTheDocument();

    const removeIcon = screen.getAllByTestId('settings-bubble-list-remove-item')[0];

    await user.click(removeIcon);

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(removeFromList({ listName: props.listName, id: '1' }));
    });
  });
});
