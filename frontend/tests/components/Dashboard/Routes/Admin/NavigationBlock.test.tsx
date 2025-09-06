import { screen, render } from '@testing-library/react';
import { AiOutlineLock, AiOutlineSetting } from 'react-icons/ai';
import NavigationBlock from '../../../../../src/components/Dashboard/Routes/Admin/NavigationBlock';
import { AllProviders } from '../../../../AllProviders';

describe('NavigationBlock', () => {
  const getProps = () => {
    const links = [
      { id: 1, path: '/mfa', label: 'MFA', icon: <AiOutlineLock /> },
      { id: 2, path: '/settings', label: 'Settings', icon: <AiOutlineSetting /> },
    ];

    const title = 'Settings';

    return { links, title };
  };

  const renderComponent = () => {
    const props = getProps();
    render(<NavigationBlock {...props} />, { wrapper: AllProviders });

    return {
      props,
      getHeading: () => screen.getByRole('heading', { name: props.title }),
    };
  };

  it('should render the title of the navigation block', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });

  it('should render the prop links', () => {
    const { props } = renderComponent();

    const linkElements = screen.getAllByRole('link');

    linkElements.forEach((linkElement, index) => {
      const elPath = linkElement.getAttribute('href');
      const { path } = props.links[index];
      expect(elPath).toBe(path);
    });
  });
});
