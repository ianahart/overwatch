import SuggestionForm from './SuggestionForm';

const Suggestion = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="max-w-[750px] mx-auto p-2 rounded text-gray-400">
        <div>
          <h2 className="my-4 text-center text-xl">Add a Suggestion</h2>
          <p className="leading-7">
            Have a suggestion or feedback? Let us know how we can improve! Choose a feedback type from the dropdown,
            provide a clear title and detailed description of your suggestion, and feel free to upload any supporting
            files, like screenshots or documents. Your input helps us make Overwatch better for everyone!
          </p>
        </div>
        <div className="my-12">
          <SuggestionForm />
        </div>
      </div>
    </div>
  );
};

export default Suggestion;
