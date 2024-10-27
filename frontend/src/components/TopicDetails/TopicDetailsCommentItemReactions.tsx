import { nanoid } from 'nanoid';
import { IReaction } from '../../interfaces';
import { useState } from 'react';
import ClickAway from '../Shared/ClickAway';

export interface ITopicDetailsCommentItemReactionsProps {
  reactions: IReaction[];
}

const TopicDetailsCommentItemReactions = ({ reactions }: ITopicDetailsCommentItemReactionsProps) => {
  const [isOpen, setIsOpen] = useState(false);

  const handleCloseClickAway = (): void => {
    setIsOpen(false);
  };

  return (
    <div onClick={() => setIsOpen(true)} className="flex cursor-pointer ml-2 relative">
      {reactions.map((reaction) => {
        return (
          <div key={nanoid()}>
            <p>{reaction.emoji}</p>
          </div>
        );
      })}
      {isOpen && (
        <ClickAway onClickAway={handleCloseClickAway}>
          <div className="flex p-2 rounded bg-gray-800 absolute left-0">
            {reactions.map((reaction) => {
              return (
                <div key={nanoid()} className="mx-1">
                  <p>{reaction.emoji}</p>
                  <p className="text-xs text-blue-400 text-center">{reaction.count}</p>
                </div>
              );
            })}
          </div>
        </ClickAway>
      )}
    </div>
  );
};

export default TopicDetailsCommentItemReactions;
