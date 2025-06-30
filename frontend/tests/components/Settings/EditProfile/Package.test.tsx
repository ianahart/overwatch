import { screen, render, waitFor } from '@testing-library/react';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';

import Package from '../../../../src/components/Settings/EditProfile/Package';
import { AllProviders } from '../../../AllProviders';
import { IPackage } from '../../../../src/interfaces';
import userEvent from '@testing-library/user-event';
import {
  addPackageItem,
  removePackageDesc,
  removePackageItem,
  updatePackageItem,
  updatePackagePrice,
} from '../../../../src/state/store';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('Package', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = (overrides = {}) => {
    const data: IPackage = {
      price: '0',
      description: 'description of pro',
      items: [
        { id: '1', name: 'Service A', isEditing: 0 },
        { id: '2', name: 'Service B', isEditing: 1 },
      ],
      ...overrides,
    };
    const name = 'pro';
    const title = 'Pro';

    return { data, name, title };
  };

  const renderComponent = (overrides = {}) => {
    const props = getProps(overrides);

    render(<Package {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
    };
  };

  it('should render title, description, price, and service inputs', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.title)).toBeInTheDocument();
    expect(screen.getByText(props.data.description)).toBeInTheDocument();
    expect(screen.getByText(props.data.price)).toBeInTheDocument();
    expect(screen.getByText(props.data.items[0].name)).toBeInTheDocument();
    expect(screen.getByText(props.data.items[0].name)).toBeInTheDocument();
  });

  it('should dispatch "updatePackagePrice" when price is changed', async () => {
    const { user } = renderComponent({ price: '' });

    const priceInput = screen.getByLabelText(/price/i);

    await user.clear(priceInput);

    await user.type(priceInput, '10');

    await waitFor(() => {
      expect(mockDispatch).toHaveBeenCalledWith(updatePackagePrice({ name: 'pro', value: '1' }));
    });
  });

  it('dispatches addPackageItem when "Add" is clicked', async () => {
    const { user } = renderComponent();

    const itemInput = screen.getByLabelText(/included services/i);
    await user.type(itemInput, 'New Service');
    await user.click(screen.getByRole('button', { name: /add/i }));

    expect(mockDispatch).toHaveBeenCalledWith(addPackageItem({ name: 'pro', value: 'New Service' }));
  });

  it('dispatches updatePackageItem toggle when "Edit" is clicked', async () => {
    renderComponent();
    await userEvent.click(screen.getAllByRole('button', { name: /edit/i })[0]);

    expect(mockDispatch).toHaveBeenCalledWith(
      updatePackageItem({ id: '1', name: 'Service A', isEditing: 1, pckg: 'pro' })
    );
  });

  it('dispatches updatePackageItem when editing input changes', async () => {
    const { user } = renderComponent();
    const editingInput = screen.getByDisplayValue('Service B');

    await user.clear(editingInput);
    await user.type(editingInput, 'Edited Service');

    expect(mockDispatch).toHaveBeenLastCalledWith(
      updatePackageItem({ id: '2', name: 'Service Be', isEditing: 1, pckg: 'pro' })
    );
  });

  it('dispatches removePackageItem when "Remove" is clicked', async () => {
    const { user } = renderComponent();
    const removeButtons = screen.getAllByRole('button', { name: /remove/i });

    await user.click(removeButtons[1]);

    expect(mockDispatch).toHaveBeenCalledWith(removePackageItem({ id: '1', pckg: 'pro' }));
  });

  it('dispatches removePackageDesc when "Remove" package description is clicked', async () => {
    renderComponent();
    const descRemoveButton = screen.getAllByRole('button', { name: /remove/i })[0];

    await userEvent.click(descRemoveButton);

    expect(mockDispatch).toHaveBeenCalledWith(removePackageDesc({ pckg: 'pro' }));
  });
});
