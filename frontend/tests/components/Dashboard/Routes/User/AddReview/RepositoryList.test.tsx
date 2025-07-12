import { screen, render, within, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import RepositoryList from '../../../../../../src/components/Dashboard/Routes/User/AddReview/RepositoryList';

import { mockNavigate } from '../../../../../setup';
import { db } from '../../../../../mocks/db';
import { IConnection } from '../../../../../../src/interfaces';
import { getLoggedInUser } from '../../../../../utils';
import userEvent, { UserEvent } from '@testing-library/user-event';

export interface IForm {
  getReviewType: () => HTMLElement;
  getPackagePlan: () => HTMLElement;
  getSubmitBtn: () => HTMLElement;
  getTextarea: () => HTMLElement;
}

vi.mock('../../../../../../src/util/SessionService', () => ({
  Session: {
    getItem: vi.fn(() => 123),
  },
}));

vi.mock('../../../../../../src/util', async () => {
  const actual = await vi.importActual<typeof import('../../../../../../src/util')>('../../../../../../src/util');

  return {
    ...actual,
    retrieveTokens: () => ({
      token: 'mock-token',
      refreshToken: 'mock-refresh',
    }),
  };
});

describe('RepositoryList', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getForm = () => {
    return {
      getReviewType: () => screen.getByTestId('select-review-type'),
      getPackagePlan: () => screen.getByTestId('select-package-plan'),
      getSubmitBtn: () => screen.getByRole('button', { name: /submit/i }),
      getTextarea: () => screen.getByRole('textbox', { name: /add comment/i }),
    };
  };

  const fillOutForm = async (user: UserEvent, form: IForm, getRepos: () => Promise<HTMLElement[]>) => {
    await getRepos();

    const { getReviewType, getPackagePlan } = form;

    const reviewTypeSelect = getReviewType();
    const planSelect = getPackagePlan();

    const planOptions = within(planSelect).getAllByRole('option') as HTMLOptionElement[];
    const reviewOptions = within(reviewTypeSelect).getAllByRole('option') as HTMLOptionElement[];

    const reviewValue = reviewOptions.find((o) => o.value)?.value;
    const planValue = planOptions.find((o) => o.value)?.value;

    if (reviewValue && planValue) {
      await user.selectOptions(reviewTypeSelect, [reviewValue]);
      await user.selectOptions(planSelect, [planValue]);
    } else {
      throw new Error('Required option values not found in select elements');
    }
    const repoCard = await getRepos();
    await user.click(repoCard[0]);
  };

  const renderComponent = (overrides: Partial<IConnection> = {}) => {
    const connectionEntity = db.connection.create();

    const selectedReviewer: IConnection = {
      ...toPlainObject(connectionEntity),
      receiverId: 2,
      senderId: 1,
      ...overrides,
    };

    const { wrapper } = getLoggedInUser(
      {},
      {
        addReview: {
          selectedReviewer,
        },
      }
    );

    render(<RepositoryList />, { wrapper });

    return {
      user: userEvent.setup(),
      form: getForm(),
      getRepos: () => screen.findAllByTestId('Repository'),
    };
  };

  it('should render repositories fetched from API', async () => {
    const { getRepos } = renderComponent();

    expect(await screen.findByText(/click on a repository/i)).toBeInTheDocument();

    const repositoryItems = await getRepos();

    expect(repositoryItems.length).toBeGreaterThan(0);
  });

  it('should paginate and show "More repositories"... when nextPageUrl exists', async () => {
    const { user, getRepos } = renderComponent();

    const moreReposBtn = await screen.findByText(/more repositories/i);

    expect(moreReposBtn).toBeInTheDocument();

    await user.click(moreReposBtn);

    const repos = await getRepos();
    expect(repos.length).toBe(4);
  });

  it('should show error if review type or package is missing on submit', async () => {
    const { user, form, getRepos } = renderComponent();

    const repos = await getRepos();

    await user.click(repos[0]);

    await user.click(form.getSubmitBtn());

    expect(await screen.findByText(/please select a package/i)).toBeInTheDocument();
  });

  it('should show selected repository and charge message after selection', async () => {
    const { user, form, getRepos } = renderComponent();

    fillOutForm(user, form, getRepos);

    expect(await screen.findByText(/You have selected/i)).toBeInTheDocument();
    expect(screen.getByText(/You will be charged/i)).toBeInTheDocument();
  });

  it('should show error when submit clicked without selecting package and review type', async () => {
    const { user, form, getRepos } = renderComponent();

    const repos = await getRepos();

    await screen.findByText(/click on a repository/i);

    await user.click(repos[0]);

    user.click(form.getSubmitBtn());

    expect(await screen.findByText(/please select a package plan to continue/i)).toBeInTheDocument();
  });

  it('should select review type and package and submit successfully', async () => {
    const { user, form, getRepos } = renderComponent();

    fillOutForm(user, form, getRepos);

    const { getTextarea, getSubmitBtn } = form;

    await user.type(getTextarea(), 'Please review this repository');

    await user.click(getSubmitBtn());

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith(expect.stringContaining('/dashboard'));
    });
  });
});
