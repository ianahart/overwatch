export interface ISkillProps {
  data: { id: string; name: string }[];
  title: string;
}

const Skill = ({ data, title }: ISkillProps) => {
  return (
    <div className="my-4">
      <h3 className="text-md text-gray-400">{title}</h3>
      <div className="flex flex-wrap">
        {data.map(({ id, name }) => {
          return (
            <div key={id} className="m-1 border border-gray-800 p-2 rounded-xl">
              <p>{name}</p>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default Skill;
