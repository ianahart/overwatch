import { screen, render } from '@testing-library/react';

import Connects from '../../../../src/components/Settings/Connects';
import { AllProviders } from '../../../AllProviders';

describe('Connects', () => {
  const getComponents = () => {
    return {
      getChat: () => screen.getByTestId('settings-chat-component'),
      getConnections: () => screen.getByTestId('settings-connections-component'),
      getProfile: () => screen.getByTestId('settings-profile-component'),
    };
  };

  const renderComponent = () => {
    render(<Connects />, { wrapper: AllProviders });

    return {
      components: getComponents(),
    };
  };

  it('should render <Connections />, <Chat />, and <Profile /> components', () => {
    const { components } = renderComponent();

    const { getConnections, getChat, getProfile } = components;

    expect(getConnections()).toBeInTheDocument();
    expect(getChat()).toBeInTheDocument();
    expect(getProfile()).toBeInTheDocument();
  });
});
