import { useSelector } from 'react-redux';
import Header from '../Header';
import { TRootState } from '../../../state/store';
import BubbleInputList from './BubbleInputList';

const Skills = () => {
  const { languages, programmingLanguages, qualifications } = useSelector((store: TRootState) => store.skills);

  return (
    <div>
      <Header heading="Skills & Expertise" />
      <div className="my-8">
        <BubbleInputList
          listName="languages"
          data={languages}
          label="Languages spoken"
          htmlFor="language"
          id="language"
        />
      </div>
      <div className="my-8">
        <BubbleInputList
          listName="programmingLanguages"
          data={programmingLanguages}
          label="Fluent programming languages"
          htmlFor="programmingLanguage"
          id="programmingLanguage"
        />
      </div>
      <div className="my-8">
        <BubbleInputList
          listName="qualifications"
          data={qualifications}
          label="Your qualifications"
          htmlFor="qualification"
          id="qualification"
        />
      </div>
    </div>
  );
};

export default Skills;
