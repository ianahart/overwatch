import { screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import CardPhotosLibrary from '../../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/Actions/CardPhotosLibrary';
import { AllProviders } from '../../../../../../AllProviders';

let mockRetrieveTokens: () => { token: string } | null;

vi.mock('../../../../../../../src/util', async (importOriginal) => {
  const actual = await importOriginal<typeof import('../../../../../../../src/util')>();

  return {
    ...actual,
    retrieveTokens: () => mockRetrieveTokens(),
  };
});

describe('CardPhotosLibrary', () => {
  beforeEach(() => {
    vi.clearAllMocks();

    mockRetrieveTokens = () => ({ token: 'fake-token' });
  });

  const getElements = () => {
    return {
      getTitle: () => screen.getByText(/search for a photo/i),
      getInput: () => screen.getByRole('textbox'),
      getSearchBtn: () => screen.getByRole('button', { name: /search/i }),
    };
  };

  const renderComponent = () => {
    const updateCardPhoto = vi.fn();

    render(<CardPhotosLibrary updateCardPhoto={updateCardPhoto} />, { wrapper: AllProviders });

    return {
      user: userEvent.setup(),
      updateCardPhoto,
      elements: getElements(),
    };
  };

  it('should render initial UI with search prompt', () => {
    const { elements } = renderComponent();

    const { getInput, getTitle } = elements;

    expect(getTitle()).toBeInTheDocument();
    expect(getInput()).toBeInTheDocument();
  });

  it('should render photos when data is provided from redux query', async () => {
    renderComponent();

    const photos = await screen.findAllByTestId('PexelPhoto');

    expect(photos).toHaveLength(3);
  });

  it('should submit query and update photos on success', async () => {
    const { user, elements } = renderComponent();

    const { getInput, getSearchBtn } = elements;

    await user.type(getInput(), 'photo-1');
    await user.click(getSearchBtn());

    const photos = await screen.findAllByTestId('PexelPhoto');

    expect(photos).toHaveLength(3);
  });

  it('should call "updateCardPhoto" when photo is clicked', async () => {
    const { user, updateCardPhoto } = renderComponent();

    const [photoOne] = await screen.findAllByTestId('PexelPhoto');

    await user.click(photoOne);

    await waitFor(() => {
      expect(updateCardPhoto).toHaveBeenCalledWith(expect.stringContaining('photo-1'));
    });
  });
});
