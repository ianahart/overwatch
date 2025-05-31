export interface IAdditionalInfoProps {
  tagLine: string;
  bio: string;
  moreInfo: string;
}

const AdditionalInfo = ({ tagLine, bio, moreInfo }: IAdditionalInfoProps) => {
  return (
    <div className="p-4 border-b border-gray-800">
      <h3 className="text-gray-400 text-lg">Additional Info</h3>
      <div className="my-4">
        <h4 className="text-gray-400">Bio</h4>
        <p className="text-sm">{bio}</p>
      </div>
      <div className="my-4">
        <h4 className="text-gray-400">Tagline</h4>
        <p className="text-sm">"{tagLine}"</p>
      </div>
      <div className="my-4">
        <h4 className="text-gray-400">More Info</h4>
        <p className="text-sm">{moreInfo}</p>
      </div>
    </div>
  );
};
export default AdditionalInfo;
