import { render } from '@testing-library/react';
import ListItemElement from '../../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/ListItemElement';
import type { RenderElementProps } from 'slate-react';
import { ReactNode } from 'react';

const createProps = (
  children: ReactNode = 'List item',
  attributes = { 'data-testid': 'list-item-element' } as any
): RenderElementProps => ({
  attributes,
  children,
  element: {} as any,
});

describe('<ListItemElement />', () => {
  it('renders an <li> with given children', () => {
    const props = createProps('Item 1');
    const { getByTestId } = render(<ListItemElement {...props} />);
    const li = getByTestId('list-item-element');

    expect(li.tagName).toBe('LI');
    expect(li.textContent).toBe('Item 1');
  });

  it('renders nested children correctly', () => {
    const props = createProps(
      <>
        <span>Child 1</span>
        <strong>Child 2</strong>
      </>
    );
    const { container } = render(<ListItemElement {...props} />);
    const li = container.querySelector('li');

    expect(li).toContainHTML('<span>Child 1</span>');
    expect(li).toContainHTML('<strong>Child 2</strong>');
  });

  it('forwards attributes to the <li>', () => {
    const props = createProps('Test', { 'data-testid': 'custom-list-item' });
    const { getByTestId } = render(<ListItemElement {...props} />);
    const li = getByTestId('custom-list-item');

    expect(li).toHaveAttribute('data-testid', 'custom-list-item');
  });
});
