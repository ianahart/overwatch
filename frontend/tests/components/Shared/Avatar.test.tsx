import { screen, render } from '@testing-library/react';
import Avatar from '../../../src/components/Shared/Avatar';

describe('Avatar', () => {
  interface IAvatarProps {
    initials: string;
    avatarUrl: string;
    width: string;
    height: string;
  }

  const renderComponent = (url: string = '') => {
    const avatar: IAvatarProps = {
      initials: 'IH',
      avatarUrl: url,
      width: 'w-9',
      height: 'h-9',
    };

    const { initials, avatarUrl, width, height } = avatar;

    render(<Avatar initials={initials} avatarUrl={avatarUrl} width={width} height={height} />);

    return {
      img: screen.queryByRole('img'),
      paragraph: screen.queryByRole('paragraph'),
      avatar,
    };
  };

  it('should render an avatar when provided with a url', () => {
    const avatarUrl = 'https://via.placeholder.com/300x200.png';

    const { img } = renderComponent(avatarUrl);

    expect(img).toBeInTheDocument();
    expect(img!.getAttribute('src')).toContain(avatarUrl);
  });

  it('should render initials when avatar url is missing', () => {
    const { img, paragraph, avatar } = renderComponent();

    expect(img).not.toBeInTheDocument();
    expect(paragraph).toHaveTextContent(avatar.initials);
  });
});
