import { screen, render } from '@testing-library/react';
import NavbarLink from '../../../src/components/Navbar/NavbarLink';
import userEvent from '@testing-library/user-event';
import { getLoggedInUser } from '../../utils';

describe('NavbarLink', () => {
  const getProps = () => {
    return {
      path: '/test-path',
      title: 'test link',
    };
  };

  const renderComponent = () => {
    const { wrapper } = getLoggedInUser();
    const props = getProps();

    render(<NavbarLink {...props} />, { wrapper });

    return {
      user: userEvent.setup(),
      getLink: () => screen.getByRole('link', { name: props.title }),
      props,
    };
  };

  it('should render the link with correct text and href', () => {
    const { getLink, props } = renderComponent();

    expect(getLink()).toHaveAttribute('href', props.path);
  });
});
