import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import CardHeaderCustomField, {
  ICardHeaderCustomFieldProps,
} from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/CustomField/CardHeaderCustomField';

describe('CardHeaderCustomField', () => {
  const setup = (props?: Partial<ICardHeaderCustomFieldProps>) => {
    const defaultProps: ICardHeaderCustomFieldProps = {
      page: 1,
      handleCloseClickAway: vi.fn(),
      navigatePrevPage: vi.fn(),
      ...props,
    };
    render(<CardHeaderCustomField {...defaultProps} />);
    return defaultProps;
  };

  it('renders the title', () => {
    setup();
    expect(screen.getByText(/custom field/i)).toBeInTheDocument();
  });

  it('does not render back arrow when page === 1', () => {
    setup({ page: 1 });
    expect(screen.queryByTestId('back-arrow')).not.toBeInTheDocument();
  });

  it('renders back arrow when page > 1', () => {
    setup({ page: 2 });
    expect(screen.getByTestId('back-arrow')).toBeInTheDocument();
  });

  it('calls navigatePrevPage when back arrow clicked', () => {
    const props = setup({ page: 2 });
    fireEvent.click(screen.getByTestId('back-arrow'));
    expect(props.navigatePrevPage).toHaveBeenCalled();
  });

  it('calls handleCloseClickAway when close button clicked', () => {
    const props = setup();
    fireEvent.click(screen.getByTestId('close-button'));
    expect(props.handleCloseClickAway).toHaveBeenCalled();
  });
});
