export interface IDashboardAvatarProps {
  url: string;
  abbreviation: string;
  width: string;
  height: string;
}

const DashboardAvatar = ({ url = '', abbreviation, width, height }: IDashboardAvatarProps) => {
  if (url !== null) {
    return (
      <div data-testid="DashboardAvatar">
        <img className={`${height} ${width} rounded-lg`} src={url} alt={`A profile picture of ${abbreviation}`} />
      </div>
    );
  } else {
    return (
      <div className={`${height} bg-green-400 font-bold ${width} rounded-lg flex flex-col items-center justify-center`}>
        <p>{abbreviation}</p>
      </div>
    );
  }
};

export default DashboardAvatar;
