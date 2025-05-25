import { screen, render } from '@testing-library/react';

import TopicDetailsHeader from '../../../src/components/TopicDetails/TopicDetailsHeader';
import { AllProviders } from '../../AllProviders';

describe('TopicDetailsHeader', () => {
  const getProps = () => {
    return {
      title: 'javascript',
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<TopicDetailsHeader {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should pass down the title prop and render it as the title', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.title)).toBeInTheDocument();
  });
});
