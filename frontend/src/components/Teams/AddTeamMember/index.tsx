import AddTeamMemberSearchBar from './AddTeamMemberSearchBar';

const AddTeamMember = () => {
  return (
    <div className="my-6 text-gray-400">
      <div className="flex flex-col items-start">
        <h3 className="text-xl">Add Team Member</h3>
        <p>Here you can look up reviewers to add to your team.</p>
        <p>To add a reviewer to your team simply click on them.</p>
      </div>
      <div className="my-4">
        <AddTeamMemberSearchBar />
      </div>
    </div>
  );
};

export default AddTeamMember;
