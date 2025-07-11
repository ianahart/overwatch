import { screen, render, waitFor } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import Repository from '../../../../../../src/components/Dashboard/Routes/User/AddReview/Repository';
import { IGitHubRepositoryPreview } from '../../../../../../src/interfaces';
import { db } from '../../../../../mocks/db';
import { AllProviders } from '../../../../../AllProviders';
import userEvent from '@testing-library/user-event';

describe('Repository', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const data: IGitHubRepositoryPreview = { ...toPlainObject(db.gitHubRepositoryPreview.create()) };
    const selectRepository = vi.fn();

    return {
      data,
      selectRepository,
    };
  };

  const getElements = (repository: IGitHubRepositoryPreview) => {
    return {
      getRepositoryContainer: () => screen.getByTestId('Repository'),
      getAvatarImage: () => screen.getByRole('img'),
      getFullName: () => screen.getByText(repository.fullName),
      getStargazersCount: () => screen.getByText(repository.stargazersCount),
      getLanguage: () => screen.getByText(repository.language),
      getHtmlLink: () => screen.getByRole('link'),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<Repository {...props} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      elements: getElements(props.data),
      props,
    };
  };

  it('should render all of the repository stats', () => {
    const { elements } = renderComponent();

    const { getFullName, getHtmlLink, getLanguage, getAvatarImage, getStargazersCount, getRepositoryContainer } =
      elements;

    expect(getFullName()).toBeInTheDocument();
    expect(getHtmlLink()).toBeInTheDocument();
    expect(getLanguage()).toBeInTheDocument();
    expect(getAvatarImage()).toBeInTheDocument();
    expect(getStargazersCount()).toBeInTheDocument();
    expect(getRepositoryContainer()).toBeInTheDocument();
  });

  it('should call "selectRepository" when the repository container is clicked', async () => {
    const { user, props, elements } = renderComponent();

    await user.click(elements.getRepositoryContainer());

    await waitFor(() => {
      expect(props.selectRepository).toHaveBeenCalledWith(props.data);
    });
  });
});
