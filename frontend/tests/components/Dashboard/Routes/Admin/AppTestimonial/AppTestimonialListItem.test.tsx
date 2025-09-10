import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import { IAdminAppTestimonial } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import AppTestimonialListItem from '../../../../../../src/components/Dashboard/Routes/Admin/AppTestimonial/AppTestimonialListItem';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';

describe('AppTestimonialListItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides: Partial<IAdminAppTestimonial> = {}) => {
    const adminAppTestimonial: IAdminAppTestimonial = {
      ...toPlainObject(db.adminAppTestimonial.create()),
      ...overrides,
    };

    return {
      adminAppTestimonial,
      selectAdminAppTestimonial: vi.fn(),
    };
  };

  const getElements = () => {
    return {
      getTestimonialSelectBtn: () => screen.getByTestId('app-testimonial-select-btn'),
    };
  };

  const renderComponent = (overrides: Partial<IAdminAppTestimonial> = {}) => {
    const props = getProps(overrides);

    render(<AppTestimonialListItem {...props} />, { wrapper: AllProviders });

    return {
      props,
      user: userEvent.setup(),
      elements: getElements(),
    };
  };

  it('should render the testimonial details', () => {
    const { props } = renderComponent();

    const { avatarUrl, firstName, developerType, content, createdAt } = props.adminAppTestimonial;

    const avatar = screen.getByRole('img');

    expect(avatar).toHaveAttribute('src', avatarUrl);
    expect(screen.getByText(firstName)).toBeInTheDocument();
    expect(screen.getByText(dayjs(createdAt).format('MM/DD/YYYY'))).toBeInTheDocument();
    expect(screen.getByText(developerType)).toBeInTheDocument();
    expect(screen.getByText(content)).toBeInTheDocument();
  });

  it('should show "Unselect" tooltip when selected', async () => {
    const { user, elements } = renderComponent({ isSelected: true });

    await user.hover(elements.getTestimonialSelectBtn());

    await waitFor(() => {
      expect(screen.getByText(/unselect/i)).toBeInTheDocument();
    });
  });

  it('should apply correct styles when selected', async () => {
    const { user, elements } = renderComponent({ isSelected: true });

    await user.hover(elements.getTestimonialSelectBtn());

    await waitFor(() => {
      expect(elements.getTestimonialSelectBtn()).toHaveClass('bg-green-400');
    });
  });

  it('should apply correct style when NOT selected', async () => {
    const { user, elements } = renderComponent({ isSelected: false });

    await user.hover(elements.getTestimonialSelectBtn());

    await waitFor(() => {
      expect(elements.getTestimonialSelectBtn()).toHaveClass('bg-transparent');
    });
  });

  it('should call "selectAdminAppTestimonial" with toggled value when clicked', async () => {
    const { user, elements, props } = renderComponent({ isSelected: false });

    await user.click(elements.getTestimonialSelectBtn());

    await waitFor(() => {
      expect(props.selectAdminAppTestimonial).toHaveBeenCalledWith(props.adminAppTestimonial.id, true);
    });
  });
});
