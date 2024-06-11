import { useSelector } from 'react-redux';
import Header from '../Header';
import BubbleInputList from './BubbleInputList';
import { TRootState } from '../../../state/store';

const YourLanguages = () => {
  const { programmingLanguages } = useSelector((store: TRootState) => store.skills);

  return (
    <div>
      <Header heading="Your Programming Languages" />
      <div className="my-8">
        <BubbleInputList
          listName="programmingLanguages"
          data={programmingLanguages}
          label="Fluent programming languages"
          htmlFor="programmingLanguage"
          id="programmingLanguage"
        />
      </div>
    </div>
  );
};

export default YourLanguages;
