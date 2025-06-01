import { screen, render } from '@testing-library/react';

import ProfileContainer from '../../../src/components/Profile/ProfileContainer';

describe('ProfileContainer', () => {
  it('should render children', () => {
    render(
      <ProfileContainer>
        <p>test content</p>
      </ProfileContainer>
    );
    expect(screen.getByText('test content')).toBeInTheDocument();
  });
});
