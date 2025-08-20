import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import CardCreateCustomField from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CustomField/CardCreateCustomField';
import { AllProviders } from '../../../../../../AllProviders';

describe('CardCreateCustomField', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (page: number, overrides = {}) => {
    return {
      page,
      navigatePrevPage: vi.fn(),
      navigateNextPage: vi.fn(),
      handleCloseClickAway: vi.fn(),
      selectCustomField: vi.fn(),
      ...overrides,
    };
  };

  const getForm = () => {
    return {
      getInput: () => screen.getByLabelText(/title/i),
      getSelect: () => screen.getByLabelText(/type/i),
      getNextBtn: () => screen.getByRole('button', { name: /next/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = (page: number = 0, overrides = {}) => {
    const props = getProps(page, overrides);

    render(<CardCreateCustomField {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      form: getForm(),
    };
  };

  it('should render input, select, and buttons', () => {
    const { form } = renderComponent();

    const { getInput, getSelect, getNextBtn, getCancelBtn } = form;

    expect(getInput()).toBeInTheDocument();
    expect(getSelect()).toBeInTheDocument();
    expect(getNextBtn()).toBeInTheDocument();
    expect(getCancelBtn()).toBeInTheDocument();
  });

  it('should call "selectCustomField" and "navigateNextPage" when vaild form is submitted', async () => {
    const { user, form, props } = renderComponent();

    const { getInput, getSelect, getNextBtn } = form;

    await user.type(getInput(), 'title');
    await user.selectOptions(getSelect(), 'TEXT');
    await user.click(getNextBtn());

    await waitFor(() => {
      expect(props.selectCustomField).toHaveBeenCalledWith('title', 'TEXT');
      expect(props.navigateNextPage).toHaveBeenCalled();
    });
  });
  it('should call "handleCloseClickAway" on cancel click', async () => {
    const { user, form, props } = renderComponent();

    const { getCancelBtn } = form;

    await user.click(getCancelBtn());

    await waitFor(() => {
      expect(props.handleCloseClickAway).toHaveBeenCalled();
    });
  });
});
