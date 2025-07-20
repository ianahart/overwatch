import { render } from '@testing-library/react';
import DefaultElement from '../../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/DefaultElement';
import { TParagraphElement, TCustomElement } from '../../../../../../../src/types';
import { ReactNode } from 'react';

const createProps = (element: TCustomElement, children: ReactNode = 'Test content') => ({
  element,
  children,
  attributes: {
    ...({ 'data-testid': 'default-element' } as any),
  },
});

describe('<DefaultElement />', () => {
  it('renders a paragraph element', () => {
    const element: TParagraphElement = { type: 'paragraph', align: 'left', children: [] };
    const props = createProps(element);

    const { getByTestId } = render(<DefaultElement {...props} />);
    const p = getByTestId('default-element');

    expect(p.tagName).toBe('P');
    expect(p.textContent).toBe('Test content');
  });

  it.each([
    ['left', 'text-left'],
    ['center', 'text-center'],
    ['right', 'text-right'],
  ])('applies alignment class "%s"', (align, expectedClass) => {
    const element: TParagraphElement = {
      type: 'paragraph',
      align: align as 'left' | 'center' | 'right',
      children: [],
    };

    const props = createProps(element);
    const { getByTestId } = render(<DefaultElement {...props} />);
    const p = getByTestId('default-element');

    expect(p.className).toContain(expectedClass);
  });

  it('renders with no alignment class if not a paragraph', () => {
    const nonParagraph: TCustomElement = { type: 'code', children: [] };

    const props = createProps(nonParagraph);

    const { getByTestId } = render(<DefaultElement {...props} />);
    const p = getByTestId('default-element');

    expect(p.className).toBe('');
  });

  it('forwards attributes to the paragraph', () => {
    const element: TParagraphElement = { type: 'paragraph', align: 'right', children: [] };
    const props = createProps(element);

    const { getByTestId } = render(<DefaultElement {...props} />);
    const p = getByTestId('default-element');

    expect(p).toHaveAttribute('data-testid', 'default-element');
  });

  it('renders nested children correctly', () => {
    const element: TParagraphElement = { type: 'paragraph', align: 'left', children: [] };
    const props = createProps(
      element,
      <>
        <span>Line 1</span>
        <span>Line 2</span>
      </>
    );

    const { container } = render(<DefaultElement {...props} />);
    const spans = container.querySelectorAll('p > span');

    expect(spans.length).toBe(2);
    expect(spans[0].textContent).toBe('Line 1');
    expect(spans[1].textContent).toBe('Line 2');
  });
});
