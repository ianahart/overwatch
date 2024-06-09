import { ILanguage, IProgrammingLanguage, IQualification } from '../../interfaces';
import Skill from './Skill';

export interface ISkillsProps {
  languages: ILanguage[];
  programmingLanguages: IProgrammingLanguage[];
  qualifications: IQualification[];
}

const Skills = ({ languages, programmingLanguages, qualifications }: ISkillsProps) => {
  return (
    <div className="p-4 border border-l border-b border-r-0 border-t-0 border-gray-800">
      <h3 className="text-gray-400 text-lg">Skills</h3>
      <Skill data={languages} title="Languages" />
      <Skill data={programmingLanguages} title="Programming Languages" />
      <Skill data={qualifications} title="Education" />
    </div>
  );
};

export default Skills;
