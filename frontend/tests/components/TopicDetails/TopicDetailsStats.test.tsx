import { screen, render, waitFor, fireEvent } from '@testing-library/react';

import TopicDetailsStats from '../../../src/components/TopicDetails/TopicDetailsStats';
import { AllProviders } from '../../AllProviders';
import 'react-toastify/dist/ReactToastify.css';

describe('TopicDetailsStats', () => {
  beforeEach(() => {
    Object.defineProperty(navigator, 'clipboard', {
      value: {
        writeText: vi.fn().mockResolvedValue(undefined),
      },
      writable: true,
    });
  });

  const renderComponent = () => {
    const props = { totalCommentCount: 5 };

    render(<TopicDetailsStats {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render the total comment count', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.totalCommentCount)).toBeInTheDocument();
  });

  it('copies current URL to clipboard and shows toast on share click', async () => {
    renderComponent();

    const mockUrl = 'https://example.com/topic/123';
    Object.defineProperty(window, 'location', {
      value: { href: mockUrl },
      writable: true,
    });

    fireEvent.click(screen.getByText(/Share/i));

    expect(navigator.clipboard.writeText).toHaveBeenCalledWith(mockUrl);

    await waitFor(() => {
      expect(screen.getByText(/The current url has been copied to your clipboard!/i)).toBeInTheDocument();
    });
  });
});
