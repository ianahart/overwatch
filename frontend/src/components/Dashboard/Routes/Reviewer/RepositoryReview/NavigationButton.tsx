import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { ERepositoryView } from '../../../../../enums';
import { TRootState, setRepositoryNavView } from '../../../../../state/store';

export interface INavigationButtonProps {
  text: string;
  keyword: ERepositoryView;
  icon: React.ReactNode;
}

const NavigationButton = ({ text, keyword, icon }: INavigationButtonProps) => {
  const dispatch = useDispatch();
  const { repositoryNavView } = useSelector((store: TRootState) => store.repositoryTree);
  const [isToolTipShowing, setIsToolTipShowing] = useState(false);

  const handleOnMouseOver = () => {
    if (!isToolTipShowing) {
      setIsToolTipShowing(true);
    }
  };

  const handleOnMouseLeave = () => {
    if (isToolTipShowing) {
      setIsToolTipShowing(false);
    }
  };

  const handleOnClick = () => {
    dispatch(setRepositoryNavView(keyword));
  };

  return (
    <div
      onMouseOver={handleOnMouseOver}
      onMouseLeave={handleOnMouseLeave}
      onClick={handleOnClick}
      className={`m-2 relative p-2 cursor-pointer rounded-full hover:bg-stone-900 ${
        keyword === repositoryNavView ? 'bg-green-400 text-black' : 'bg-gray-950 text-gray-400'
      }`}
    >
      {icon}
      {isToolTipShowing && (
        <div className="absolute -top-8 -right-8 bg-gray-950 p-1 rounded z-10">
          <p className="text-sm text-gray-400">{text}</p>
        </div>
      )}
    </div>
  );
};

export default NavigationButton;
