import { render, screen, fireEvent } from '@testing-library/react';
import { vi } from 'vitest';
import ClickAway from '../../../src/components/Shared/ClickAway';

describe('ClickAway component', () => {
  it('should call onClickAway when clicking outside the component', () => {
    const onClickAway = vi.fn();

    render(
      <ClickAway onClickAway={onClickAway}>
        <div>Inside Component</div>
      </ClickAway>
    );

    fireEvent.mouseDown(document);

    expect(onClickAway).toHaveBeenCalled();
  });

  it('should not call onClickAway when clicking inside the component', () => {
    const onClickAway = vi.fn();

    render(
      <ClickAway onClickAway={onClickAway}>
        <div>Inside Component</div>
      </ClickAway>
    );

    fireEvent.mouseDown(screen.getByText('Inside Component'));

    expect(onClickAway).not.toHaveBeenCalled();
  });

  it('should clean up event listeners on unmount', () => {
    const onClickAway = vi.fn();
    const { unmount } = render(
      <ClickAway onClickAway={onClickAway}>
        <div>Inside Component</div>
      </ClickAway>
    );

    unmount();

    fireEvent.mouseDown(document);

    expect(onClickAway).not.toHaveBeenCalled();
  });
});
