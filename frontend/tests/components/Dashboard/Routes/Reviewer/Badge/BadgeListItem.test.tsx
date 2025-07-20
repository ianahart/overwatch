import { screen, render } from '@testing-library/react';
import BadgeListItem from '../../../../../../src/components/Dashboard/Routes/Reviewer/Badge/BadgeListItem';
import { AllProviders } from '../../../../../AllProviders';
import { createBadges } from '../../../../../mocks/dbActions';
import dayjs from 'dayjs';
import { IBadge } from '../../../../../../src/interfaces';

describe('BadgeListItem', () => {
  const getProps = () => {
    return {
      data: createBadges(1)[0],
    };
  };

  const getElements = (data: IBadge) => {
    return {
      getImage: () => screen.getByRole('img'),
      getCreatedAt: () => screen.getByText(dayjs(data.createdAt).format('MM/DD/YYYY')),
      getTitle: () => screen.getByRole('heading', { name: data.title, level: 3 }),
      getDescription: () => screen.getByText(data.description),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<BadgeListItem {...props} />, { wrapper: AllProviders });

    return {
      elements: getElements(props.data),
    };
  };

  it('should render all prop data that was passed down', () => {
    const { elements } = renderComponent();

    const { getImage, getTitle, getCreatedAt, getDescription } = elements;

    expect(getImage()).toBeInTheDocument();
    expect(getTitle()).toBeInTheDocument();
    expect(getCreatedAt()).toBeInTheDocument();
    expect(getDescription()).toBeInTheDocument();
  });
});
