import { render } from '@testing-library/react';
import Leaf from '../../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/Leaf';
import type { RenderLeafProps } from 'slate-react';
import { ReactNode } from 'react';

const createProps = (leaf: Partial<RenderLeafProps['leaf']> = {}, children: ReactNode = 'Leaf text'): RenderLeafProps =>
  ({
    leaf: {
      text: '',
      bold: false,
      italic: false,
      underline: false,
      ...leaf,
    },
    children,
    attributes: {
      ...({ 'data-testid': 'leaf' } as any),
    },
  } as RenderLeafProps);

describe('<Leaf />', () => {
  it('renders a <span> with default styles', () => {
    const props = createProps();
    const { getByTestId } = render(<Leaf {...props} />);
    const span = getByTestId('leaf');

    expect(span.tagName).toBe('SPAN');
    expect(span).toHaveStyle('font-weight: normal');
    expect(span).toHaveStyle('font-style: normal');
    expect(span.style.textDecoration === 'unset' || span.style.textDecoration === '').toBe(true);
    expect(span.textContent).toBe('Leaf text');
  });

  it('applies bold style when leaf.bold is true', () => {
    const props = createProps({ bold: true });
    const { getByTestId } = render(<Leaf {...props} />);
    expect(getByTestId('leaf')).toHaveStyle({ fontWeight: 'bold' });
  });

  it('applies italic style when leaf.italic is true', () => {
    const props = createProps({ italic: true });
    const { getByTestId } = render(<Leaf {...props} />);
    expect(getByTestId('leaf')).toHaveStyle({ fontStyle: 'italic' });
  });

  it('applies underline style when leaf.underline is true', () => {
    const props = createProps({ underline: true });
    const { getByTestId } = render(<Leaf {...props} />);
    expect(getByTestId('leaf')).toHaveStyle({ textDecoration: 'underline' });
  });

  it('applies all styles when all are true', () => {
    const props = createProps({ bold: true, italic: true, underline: true });
    const { getByTestId } = render(<Leaf {...props} />);
    expect(getByTestId('leaf')).toHaveStyle({
      fontWeight: 'bold',
      fontStyle: 'italic',
      textDecoration: 'underline',
    });
  });

  it('renders nested children correctly', () => {
    const props = createProps(
      { bold: true },
      <>
        <strong>One</strong>
        <em>Two</em>
      </>
    );
    const { container } = render(<Leaf {...props} />);
    const strong = container.querySelector('span > strong');
    const em = container.querySelector('span > em');

    expect(strong?.textContent).toBe('One');
    expect(em?.textContent).toBe('Two');
  });

  it('forwards attributes to the span', () => {
    const props = createProps();
    const { getByTestId } = render(<Leaf {...props} />);
    const span = getByTestId('leaf');

    expect(span).toHaveAttribute('data-testid', 'leaf');
  });
});
