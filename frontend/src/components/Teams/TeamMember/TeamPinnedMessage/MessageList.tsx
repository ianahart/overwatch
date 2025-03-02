import { useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import { DndContext, PointerSensor, useSensor, useSensors, closestCorners, DragEndEvent } from '@dnd-kit/core';
import { SortableContext, arrayMove, horizontalListSortingStrategy } from '@dnd-kit/sortable';

import { ITeam, ITeamPinnedMessage } from '../../../../interfaces';
import {
  TRootState,
  useFetchTeamPinnedMessagesQuery,
  useReorderTeamPinnedMessagesMutation,
} from '../../../../state/store';
import MessageListItem from './MessageListItem';

export interface IMessageListProps {
  team: ITeam;
}

const MessageList = ({ team }: IMessageListProps) => {
  const { token, user } = useSelector((store: TRootState) => store.user);
  const [messages, setMessages] = useState<ITeamPinnedMessage[]>([]);
  const { data } = useFetchTeamPinnedMessagesQuery({ token, teamId: team.id }, { skip: !token || !team.id });
  const [reorderTeamPinnedMessagesMut] = useReorderTeamPinnedMessagesMutation();

  useEffect(() => {
    if (data !== undefined) {
      setMessages(data.data);
    }
  }, [data]);

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 5,
      },
    })
  );

  const handleDragEnd = (e: DragEndEvent) => {
    if (team.userId !== user.id) return;
    const { active, over } = e;

    if (!active || !over) return;

    const activeId = active.id.toString();
    const overId = over.id.toString();

    const isList = activeId.startsWith('list-');

    if (isList) {
      const oldIndex = messages.findIndex((tl) => `list-${tl.id}` === activeId);
      const newIndex = messages.findIndex((tl) => `list-${tl.id}` === overId);

      if (newIndex !== -1) {
        const newMessages = arrayMove(messages, oldIndex, newIndex);
        reorderTeamPinnedMessages(newMessages, team.id);
      }
    }
  };

  const reorderTeamPinnedMessages = (teamPinnedMessages: ITeamPinnedMessage[], teamId: number) => {
    reorderTeamPinnedMessagesMut({ token, teamPinnedMessages, teamId })
      .unwrap()
      .then((res) => {
        console.log(res);
        setMessages(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <DndContext sensors={sensors} collisionDetection={closestCorners} onDragEnd={handleDragEnd}>
        <SortableContext
          id="todo-lists"
          items={messages.map((message) => `list-${message.id}`)}
          strategy={horizontalListSortingStrategy}
        >
          <div>
            {messages.map((message) => {
              return <MessageListItem team={team} user={user} key={message.id} message={message} />;
            })}
          </div>
        </SortableContext>
      </DndContext>
    </div>
  );
};

export default MessageList;
