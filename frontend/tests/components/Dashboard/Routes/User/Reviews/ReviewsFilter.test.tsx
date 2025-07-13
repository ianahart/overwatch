import { screen, render } from '@testing-library/react';
import ReviewsFilter from '../../../../../../src/components/Dashboard/Routes/User/Reviews/ReviewsFilter';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { setRepositorySearchFilter } from '../../../../../../src/state/store';

export interface IFilter {
  id: string | number;
  value: string;
  name: string;
}

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('ReviewsFilter', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const data: IFilter[] = [
      { id: 1, value: 'INPROGRESS', name: 'In Progress' },
      { id: 2, value: 'INCOMPLETE', name: 'In Complete' },
      { id: 3, value: 'COMPLETED', name: 'Completed' },
    ];

    return {
      data,
      id: 'sort',
      value: 'desc',
    };
  };

  const getForm = () => {
    return {
      getSelect: () => screen.getByRole('combobox'),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<ReviewsFilter {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      form: getForm(),
      props,
    };
  };

  it('should render all options correctly', () => {
    const { form, props } = renderComponent();

    expect(form.getSelect()).toBeInTheDocument();

    props.data.forEach(({ name }) => {
      expect(screen.getByRole('option', { name })).toBeInTheDocument();
    });
  });

  it('should set the default selected value', () => {
    const { form, props } = renderComponent();

    const select = form.getSelect() as HTMLSelectElement;
    expect(select.value).toBe(props.data[0].value);
  });

  it('should dispatch the correction action selection change', async () => {
    const { user, form } = renderComponent();

    await user.selectOptions(form.getSelect(), 'COMPLETED');

    expect(mockDispatch).toHaveBeenCalledWith(setRepositorySearchFilter({ name: 'sort', value: 'COMPLETED' }));
  });
});
