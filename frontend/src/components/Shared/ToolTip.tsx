import { useState } from 'react';

export interface IToolTipProps {
  children: React.ReactNode;
  message: string;
}

const ToolTip = ({ children, message }: IToolTipProps) => {
  const [isShowing, setIsShowing] = useState(false);

  const handleOnMouseEnter = (): void => setIsShowing(true);

  const handleOnMouseLeave = (): void => setIsShowing(false);

  return (
    <div className="relative">
      <div onMouseEnter={handleOnMouseEnter} onMouseLeave={handleOnMouseLeave}>
        {children}
      </div>
      {isShowing && (
        <div className="absolute top-6 -right-6 text-xs text-gray-400 bg-gray-800 p-2 rounded-lg">{message}</div>
      )}
    </div>
  );
};

export default ToolTip;
