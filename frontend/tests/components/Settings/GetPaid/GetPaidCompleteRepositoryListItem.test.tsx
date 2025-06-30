import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import GetPaidCompleteRepositoryListItem from '../../../../src/components/Settings/GetPaid/GetPaidCompleteRepositoryListItem';
import { IRepositoryReview } from '../../../../src/interfaces';
import { db } from '../../../mocks/db';
import { AllProviders } from '../../../AllProviders';
import userEvent from '@testing-library/user-event';
import dayjs from 'dayjs';
import { initializeName } from '../../../../src/util';

describe('GetPaidCompleteRepositoryListItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = (overrides: Partial<IRepositoryReview> = {}) => {
    const repository: IRepositoryReview = { ...toPlainObject(db.repository.create()), ...overrides };
    const transferMoneyBetweenParties = vi.fn();

    return { repository, transferMoneyBetweenParties };
  };

  const renderComponent = (overrides: Partial<IRepositoryReview> = {}) => {
    const props = getProps(overrides);

    render(<GetPaidCompleteRepositoryListItem {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      props,
    };
  };

  it('should render all repository info correctly', () => {
    const { props } = renderComponent({ status: 'COMPLETED' });

    const { reviewEndTime, firstName, lastName, profileUrl, avatarUrl, repoName, repoUrl, language, paymentPrice } =
      props.repository;

    expect(screen.getByText(`Completed on:${dayjs(reviewEndTime).format('MM/D/YYYY')}`));
    expect(
      screen.getByRole('img', { name: `A profile picture of ${initializeName(firstName, lastName)}` })
    ).toHaveAttribute('src', profileUrl);
    expect(screen.getByText(`${firstName} ${lastName}`)).toBeInTheDocument();
    expect(screen.getByRole('img', { name: repoName })).toHaveAttribute('src', avatarUrl);
    expect(screen.getByText(repoName)).toBeInTheDocument();
    expect(screen.getByText(language)).toBeInTheDocument();
    expect(screen.getByText(`+ $${paymentPrice}`)).toBeInTheDocument();
    expect(screen.getByRole('link')).toHaveAttribute('href', repoUrl);
  });

  it('should call "transferMoneyBetweenParties" with correct arguments when button is clicked', async () => {
    const { props, user } = renderComponent();

    await user.click(screen.getByRole('button', { name: /get paid/i }));

    const { id, ownerId, reviewerId } = props.repository;
    expect(props.transferMoneyBetweenParties).toHaveBeenCalledWith(id, ownerId, reviewerId);
    expect(props.transferMoneyBetweenParties).toHaveBeenCalledTimes(1);
  });
});
