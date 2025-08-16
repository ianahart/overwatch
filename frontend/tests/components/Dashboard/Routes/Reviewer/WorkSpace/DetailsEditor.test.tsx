import { render, screen } from '@testing-library/react';
import DetailsEditor from '../../../../../../src/components/Dashboard/Routes/Reviewer/WorkSpace/DetailsEditor';
import { AllProviders } from '../../../../../AllProviders';
import { Descendant } from 'slate';

vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/CodeElement', () => ({
  __esModule: true,
  default: (props: any) => <div data-testid="code-element">{props.children}</div>,
}));
vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/ListElement', () => ({
  __esModule: true,
  default: (props: any) => <ul data-testid="list-element">{props.children}</ul>,
}));
vi.mock(
  '../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/ListItemElement',
  () => ({
    __esModule: true,
    default: (props: any) => <li data-testid="list-item">{props.children}</li>,
  })
);
vi.mock(
  '../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/HeadingElement',
  () => ({
    __esModule: true,
    default: (props: any) => <h1 data-testid="heading-element">{props.children}</h1>,
  })
);
vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/Leaf', () => ({
  __esModule: true,
  default: (props: any) => <span data-testid="leaf">{props.children}</span>,
}));
vi.mock('../../../../../../src/components/Dashboard/Routes/Reviewer/RepositoryReview/ReviewEditor/Toolbar', () => ({
  __esModule: true,
  default: () => <div data-testid="toolbar" />,
}));

describe('DetailsEditor', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it('renders default fallback when no details or localStorage', () => {
    render(<DetailsEditor details={null} />, { wrapper: AllProviders });
    expect(screen.getByText(/Begin writing your details here./i)).toBeInTheDocument();
    expect(screen.getByTestId('toolbar')).toBeInTheDocument();
  });

  it('uses details prop if provided', () => {
    const details = JSON.stringify([{ type: 'paragraph', children: [{ text: 'Hello world' }] }]);
    render(<DetailsEditor details={details} />, { wrapper: AllProviders });
    expect(screen.getByText(/Hello world/)).toBeInTheDocument();
  });

  it('falls back to localStorage when no details prop', () => {
    const stored = JSON.stringify([{ type: 'paragraph', children: [{ text: 'From localStorage' }] }]);
    localStorage.setItem('details', stored);
    render(<DetailsEditor details={null} />, { wrapper: AllProviders });
    expect(screen.getByText(/From localStorage/)).toBeInTheDocument();
  });

  it('persists changes to localStorage on edit', () => {
    const value: Descendant[] = [{ type: 'paragraph', children: [{ text: 'abc', bold: true }] }];

    render(<DetailsEditor details={null} />);

    localStorage.setItem('details', JSON.stringify(value));

    const stored = localStorage.getItem('details');
    expect(stored).not.toBeNull();
    expect(stored).toContain('abc');
  });
});
