import { screen, render } from '@testing-library/react';

import CodeElement from '../../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/CodeElement';
import { AllProviders } from '../../../../../../AllProviders';
import { RenderElementProps } from 'slate-react';

describe('CodeElement', () => {
  const getProps = () => {
    const props: RenderElementProps = {
      attributes: {
        ...({
          'data-testid': 'code-element',
        } as any),
      },
      children: <span>console.log("Hello");</span>,
      element: {
        type: 'code',
        children: [],
      },
    };
    return props;
  };

  const renderComponent = () => {
    const props = getProps();

    render(<CodeElement {...props} />, { wrapper: AllProviders });

    return { props };
  };

  it('should render a <pre><code> structure with children', () => {
    renderComponent();

    const pre = screen.getByTestId('code-element');
    expect(pre.tagName).toBe('PRE');
    expect(pre.querySelector('code')).toBeInTheDocument();
    expect(pre.querySelector('code')?.textContent).toContain('console.log("Hello")');
  });

  it('should forward attributes to the <pre> element', () => {
    renderComponent();

    const pre = screen.getByTestId('code-element');

    expect(pre).toHaveAttribute('data-testid', 'code-element');
  });
  it('should render nested children correctly', () => {
    const complexChildren = (
      <>
        <span>{`const x = 5;`}</span>
        <span>{`console.log(x);`}</span>
      </>
    );

    const props = getProps();
    const { container } = render(<CodeElement {...{ ...props, children: complexChildren }} />);

    const codeBlock = container.querySelector('code');
    expect(codeBlock?.childNodes).toHaveLength(2);
  });
});
