import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';
import { IWorkSpaceEntity } from '../../../../../../../src/interfaces';
import { db } from '../../../../../../mocks/db';
import CardLabelForm from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardLabelForm';
import { getLoggedInUser } from '../../../../../../utils';

describe('CardLabelForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getWorkSpace = () => {
    const workSpace: IWorkSpaceEntity = { ...toPlainObject(db.workSpace.create()), userId: 1 };
    return workSpace;
  };

  const getElements = () => {
    return {
      getCloseIcon: () => screen.getByTestId('card-label-form-close-icon'),
      getInput: () => screen.getByRole('textbox'),
      getClearBtn: () => screen.getByRole('button', { name: /clear/i }),
      getSaveBtn: () => screen.getByRole('button', { name: /save/i }),
      getCancelBtn: () => screen.getByRole('button', { name: /cancel/i }),
    };
  };

  const renderComponent = () => {
    const workSpace = getWorkSpace();
    const handleOnCloselabelForm = vi.fn();

    const { wrapper } = getLoggedInUser(
      {},
      {
        workSpace: { workSpace },
      }
    );

    render(<CardLabelForm handleOnCloseLabelForm={handleOnCloselabelForm} />, { wrapper });

    return {
      user: userEvent.setup(),
      handleOnCloselabelForm,
      workSpace,
      elements: getElements(),
    };
  };

  it('should render elements correctly', () => {
    const { elements } = renderComponent();

    const { getInput, getCloseIcon, getClearBtn } = elements;

    expect(getInput()).toBeInTheDocument();
    expect(getCloseIcon()).toBeInTheDocument();
    expect(getClearBtn()).toBeInTheDocument();
  });

  it('should update input value on change', async () => {
    const { user, elements } = renderComponent();

    await user.type(elements.getInput(), 'important');

    const input = elements.getInput() as HTMLInputElement;

    expect(input.value).toBe('important');
  });

  it('should limit input length and early return', async () => {
    const { user, elements } = renderComponent();

    await user.type(elements.getInput(), 'a'.repeat(21));

    const input = elements.getInput() as HTMLInputElement;

    expect(input.value.length).toBe(20);
  });

  it('should select a color when clicked', async () => {
    const { user } = renderComponent();
    // harcoded rgb background color because react
    // testing library renders hex colors as rgb
    const background = 'rgb(241, 201, 59)';
    const colorDiv = screen.getByText('', { selector: `div[style="background: ${background};"]` });
    await user.click(colorDiv);
    expect(colorDiv).toHaveClass('opacity-40');
  });

  it('clears label and color when Clear button is clicked', async () => {
    const { user, elements } = renderComponent();
    const input = elements.getInput() as HTMLInputElement;
    await user.type(input, 'Test');
    const background = 'rgb(241, 201, 59)';

    const colorDiv = screen.getByText('', { selector: `div[style="background: ${background};"]` });
    await user.click(colorDiv);

    const clearBtn = screen.getByText(/clear/i);
    await user.click(clearBtn);
    expect(input.value).toBe('');
    expect(colorDiv).toHaveClass('opacity-100');
  });

  it('should create a new label and close on successful submit', async () => {
    const { user, elements, handleOnCloselabelForm } = renderComponent();
    const input = elements.getInput() as HTMLInputElement;
    await user.type(input, 'Test');
    const background = 'rgb(241, 201, 59)';

    const colorDiv = screen.getByText('', { selector: `div[style="background: ${background};"]` });
    await user.click(colorDiv);

    const saveBtn = await screen.findByRole('button', { name: /save/i });
    await user.click(saveBtn);

    await waitFor(() => {
      expect(handleOnCloselabelForm).toHaveBeenCalled();
    });
  });

  it('does not submit if input or color is empty', async () => {
    renderComponent();
    const saveBtn = screen.queryByRole('button', { name: /save/i });
    expect(saveBtn).not.toBeInTheDocument();
  });
});
