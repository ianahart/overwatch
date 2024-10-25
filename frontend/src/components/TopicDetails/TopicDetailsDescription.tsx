import { ITag } from '../../interfaces';

export interface ITopicDetailsDescriptionProps {
  tags: ITag[];
  description: string;
}

const TopicDetailsDescription = ({ tags, description }: ITopicDetailsDescriptionProps) => {
  return (
    <div className="my-4 mx-auto w-full flex items-center flex-col">
      <p className="leading-7">{description}</p>
      <div className="my-4 flex flex-wrap">
        {tags.map((tag) => {
          return (
            <div key={tag.id} className="mx-2 bg-blue-400 rounded-lg p-2 cursor-pointer">
              <p className="text-black text-xs">#{tag.name}</p>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default TopicDetailsDescription;