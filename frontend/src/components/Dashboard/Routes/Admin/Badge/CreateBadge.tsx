import BadgeForm from './BadgeForm';

const CreateBadge = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="max-w-[760px] p-2 rounded text-gray-400">
        <h3 className="text-xl">Create a Badge</h3>
        <p>
          Here you can create badges by uploading a badge, title, and description. Reviewers can then earn those badges
          if the meet the requirements.
        </p>
        <div className="my-12">
          <BadgeForm formType="create" />
        </div>
      </div>
    </div>
  );
};

export default CreateBadge;
