import SuggestionHeader from './SuggestionHeader';
import SuggestionList from './SuggestionList';

const Suggestion = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="bg-gray-900 p-2 rounded">
        <SuggestionHeader />
        <SuggestionList />
      </div>
    </div>
  );
};

export default Suggestion;
