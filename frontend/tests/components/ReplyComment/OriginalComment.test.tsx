import { screen, render } from '@testing-library/react';

import OriginalComment from '../../../src/components/ReplyComment/OriginalComment';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';
import { toPlainObject } from 'lodash';

describe('OriginalComment', () => {
  const renderComponent = () => {
    const comment = toPlainObject(db.comment.create());

    render(<OriginalComment comment={comment} />, { wrapper: AllProviders });

    return {
      comment,
    };
  };
  it('should render heading and pass comment to child', () => {
    const { comment } = renderComponent();

    expect(screen.getByText(comment.content)).toBeInTheDocument();
    expect(screen.getByText(/original comment/i)).toBeInTheDocument();
  });
});
