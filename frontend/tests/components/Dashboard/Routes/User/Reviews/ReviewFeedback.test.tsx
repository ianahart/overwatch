import { screen, render } from '@testing-library/react';
import ReviewFeedback from '../../../../../../src/components/Dashboard/Routes/User/Reviews/ReviewFeedback';

describe('<ReviewFeedback />', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it('renders read-only editor with provided feedback content', () => {
    const feedback = JSON.stringify([
      {
        type: 'paragraph',
        children: [{ text: 'Test feedback text' }],
      },
    ]);

    render(<ReviewFeedback feedback={feedback} />);
    const editor = screen.getByTestId('review-feedback-editor');
    expect(editor).toBeInTheDocument();
  });

  it('falls back to localStorage when feedback is empty', () => {
    const storedContent = JSON.stringify([
      {
        type: 'paragraph',
        children: [{ text: 'Fallback from localStorage' }],
      },
    ]);

    vi.spyOn(window.localStorage.__proto__, 'getItem').mockReturnValue(storedContent);

    render(<ReviewFeedback feedback="" />);
    const editor = screen.getByTestId('review-feedback-editor');
    expect(editor).toBeInTheDocument();
  });

  it('renders default fallback content when feedback and localStorage are invalid', () => {
    vi.spyOn(window.localStorage.__proto__, 'getItem').mockReturnValue('not valid json');

    render(<ReviewFeedback feedback="invalid json" />);
    const editor = screen.getByTestId('review-feedback-editor');
    expect(editor).toBeInTheDocument();
  });
});
