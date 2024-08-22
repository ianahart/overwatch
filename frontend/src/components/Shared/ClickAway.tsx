import { useEffect, useRef } from 'react';

export interface IClickAwayProps {
  onClickAway: () => void;
  children: React.ReactNode;
}

const ClickAway = ({ onClickAway, children }: IClickAwayProps) => {
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (ref.current && !ref.current.contains(event.target as Node)) {
        onClickAway();
      }
    };

    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [onClickAway]);

  return <div ref={ref}>{children}</div>;
};

export default ClickAway;
