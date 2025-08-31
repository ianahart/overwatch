import { useState } from 'react';
import { CiShoppingTag } from 'react-icons/ci';
import { AiOutlineClose } from 'react-icons/ai';

import { ITodoCard } from '../../../../../../interfaces';
import ClickAway from '../../../../../Shared/ClickAway';
import CardLabelForm from './CardLabelForm';
import CardLabels from './CardLabels';

export interface ICardLabelsBtnProps {
  card: ITodoCard;
}

const CardLabelsBtn = ({ card }: ICardLabelsBtnProps) => {
  const [clickAwayOpen, setClickAwayOpen] = useState(false);
  const [labelFormShowing, setLabelFormShowing] = useState(false);

  const handleOnClickAwayOpen = () => setClickAwayOpen(true);

  const handleOnClickAwayClose = () => setClickAwayOpen(false);

  const handleOnCloseLabelForm = () => setLabelFormShowing(false);

  const handleOnOpenLabelForm = () => setLabelFormShowing(true);

  return (
    <div>
      <div className="p-1 bg-gray-800 rounded w-32 my-2 hover:bg-gray-700 relative">
        <button onClick={handleOnClickAwayOpen} className="flex items-center text-sm">
          <CiShoppingTag className="mr-2" />
          Labels
        </button>
        {clickAwayOpen && (
          <ClickAway onClickAway={handleOnClickAwayClose}>
            <div className="p-2 rounded bg-gray-700 overflow-x-hidden absolute z-20 right-0 w-[250px] h-56 overflow-y-auto top-0">
              <div className="flex justify-between">
                <div>&nbsp;</div>
                <h3 className="text-center">Labels</h3>
                <AiOutlineClose
                  data-testid="card-labels-close-btn"
                  onClick={handleOnClickAwayClose}
                  className="cursor-pointer hover:opacity-70"
                />
              </div>
              {labelFormShowing ? (
                <CardLabelForm handleOnCloseLabelForm={handleOnCloseLabelForm} />
              ) : (
                <CardLabels card={card} handleOnOpenLabelForm={handleOnOpenLabelForm} />
              )}
            </div>
          </ClickAway>
        )}
      </div>
    </div>
  );
};

export default CardLabelsBtn;
