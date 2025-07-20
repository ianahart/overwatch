import { render } from '@testing-library/react';
import { ReactNode } from 'react';

import HeadingElement from '../../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/HeadingElement';
import { TCustomElement, THeadingElement } from '../../../../../../../src/types';

const createProps = (element: TCustomElement, children: ReactNode = 'Heading') => ({
  element,
  children,
  attributes: {
    ...({ 'data-testid': 'heading-element' } as any),
  },
});

describe('<HeadingElement />', () => {
  const headingSizes = [
    { size: 'h1', tag: 'H1', className: 'text-4xl' },
    { size: 'h2', tag: 'H2', className: 'text-3xl' },
    { size: 'h3', tag: 'H3', className: 'text-2xl' },
    { size: 'h4', tag: 'H4', className: 'text-xl' },
  ] as const;

  it.each(headingSizes)('renders %s with correct tag and class', ({ size, tag, className }) => {
    const element: THeadingElement = { type: 'heading', size, children: [] };
    const props = createProps(element);

    const { getByTestId } = render(<HeadingElement {...props} />);
    const heading = getByTestId('heading-element');

    expect(heading.tagName).toBe(tag);
    expect(heading).toHaveClass(className);
    expect(heading.textContent).toBe('Heading');
  });

  it('renders with default <h1> for invalid heading size', () => {
    const element: THeadingElement = {
      type: 'heading',
      // @ts-expect-error on purpose for test
      size: 'h9',
      children: [],
    };
    const props = createProps(element);

    const { getByTestId } = render(<HeadingElement {...props} />);
    const heading = getByTestId('heading-element');

    expect(heading.tagName).toBe('H1');
    expect(heading).toHaveClass('text-4xl');
  });

  it('does not render heading if element type is not "heading"', () => {
    const nonHeading: TCustomElement = { type: 'code', children: [] };
    const props = createProps(nonHeading);

    const { queryByTestId } = render(<HeadingElement {...props} />);
    const heading = queryByTestId('heading-element');

    expect(heading).not.toBeInTheDocument();
  });

  it('renders nested children correctly', () => {
    const element: THeadingElement = { type: 'heading', size: 'h2', children: [] };
    const props = createProps(
      element,
      <>
        <span>Nested</span>
        <strong>Children</strong>
      </>
    );

    const { container } = render(<HeadingElement {...props} />);
    const span = container.querySelector('h2 > span');
    const strong = container.querySelector('h2 > strong');

    expect(span?.textContent).toBe('Nested');
    expect(strong?.textContent).toBe('Children');
  });
});
