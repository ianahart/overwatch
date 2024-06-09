import { IWorkExperience } from '../../interfaces';

export interface IWorkExpProps {
  workExps: IWorkExperience[];
}

const WorkExp = ({ workExps }: IWorkExpProps) => {
  return (
    <div className="p-4">
      <h3 className="text-gray-400 text-lg">Work Experience</h3>
      <div>
        {workExps.map(({ id, title, desc }) => {
          return (
            <div className="my-4" key={id}>
              <h3 className="text-gray-400 text-md">{title}</h3>
              <p>{desc}</p>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default WorkExp;
