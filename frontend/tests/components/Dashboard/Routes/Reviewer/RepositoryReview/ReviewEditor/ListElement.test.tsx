import { render } from '@testing-library/react';
import ListElement from '../../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/ListElement';
import type { RenderElementProps } from 'slate-react';
import type { TCustomElement } from '../../../../../../../src/types';
import { ReactNode } from 'react';

const createProps = (element: TCustomElement, children: ReactNode = 'List item'): RenderElementProps => ({
  element,
  children,
  attributes: {
    ...({ 'data-testid': 'list-element' } as any),
  },
});

describe('<ListElement />', () => {
  it('renders an unordered list with correct class and children', () => {
    const element: TCustomElement = { type: 'list', listType: 'unordered', children: [] };
    const props = createProps(element, <li>Item 1</li>);

    const { getByTestId } = render(<ListElement {...props} />);
    const ul = getByTestId('list-element');

    expect(ul.tagName).toBe('UL');
    expect(ul).toHaveClass('list-disc list-inside');
    expect(ul).toContainHTML('<li>Item 1</li>');
  });

  it('renders an ordered list with correct class and children', () => {
    const element: TCustomElement = { type: 'list', listType: 'ordered', children: [] };
    const props = createProps(element, <li>Item A</li>);

    const { getByTestId } = render(<ListElement {...props} />);
    const ol = getByTestId('list-element');

    expect(ol.tagName).toBe('OL');
    expect(ol).toHaveClass('list-decimal list-inside');
    expect(ol).toContainHTML('<li>Item A</li>');
  });

  it('renders nothing if element is not a list', () => {
    const element: TCustomElement = { type: 'paragraph', children: [] };
    const props = createProps(element, 'Not a list');

    const { container } = render(<ListElement {...props} />);
    expect(container).toBeEmptyDOMElement();
  });

  it('forwards attributes to the list element', () => {
    const element: TCustomElement = { type: 'list', listType: 'unordered', children: [] };
    const props = createProps(element);

    const { getByTestId } = render(<ListElement {...props} />);
    const ul = getByTestId('list-element');

    expect(ul).toHaveAttribute('data-testid', 'list-element');
  });
});
