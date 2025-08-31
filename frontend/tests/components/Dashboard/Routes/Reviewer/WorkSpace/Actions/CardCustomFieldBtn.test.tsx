import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import { ITodoCard } from '../../../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../../../utils';
import CardCustomFieldBtn from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardCustomFieldBtn';

vi.mock('../../../../../Shared/ClickAway', () => {
  return {
    default: ({ onClickAway, children }: any) => {
      return (
        <div data-testid="mock-click-away">
          {children}
          <button data-testid="mock-click-away-btn" onClick={onClickAway}>
            Click Away
          </button>
        </div>
      );
    },
  };
});

vi.mock('../CustomField/CardStartCustomField', () => {
  return { default: () => <div>Start your custom field - page 1</div> };
});

vi.mock('../CustomField/CardCreateCustomField', () => {
  return { default: () => <div>Create custom field - page 2</div> };
});

vi.mock('../CustomField/CardCustomFieldForm', () => {
  return { default: () => <div>Custom field form - page 3</div> };
});

describe('CardCustomFieldBtn', () => {
  const getProps = (): { card: ITodoCard } => ({
    card: { id: 1, title: 'Test Card', userId: 1, todoListId: 1 } as ITodoCard,
  });

  const renderComponent = async () => {
    const props = getProps();
    const { wrapper } = getLoggedInUser();
    render(<CardCustomFieldBtn {...props} />, { wrapper });
    const user = userEvent.setup();
    return { props, user };
  };

  it('renders the custom fields button', async () => {
    await renderComponent();
    const btn = screen.getByRole('button', { name: /custom fields/i });
    expect(btn).toBeInTheDocument();
  });
});
