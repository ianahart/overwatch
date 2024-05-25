export interface IAvatarProps {
  initials: string;
  avatarUrl: string;
  width: string;
  height: string;
}

const Avatar = ({ initials, avatarUrl, width, height }: IAvatarProps) => {
  return (
    <>
      {avatarUrl ? (
        <img className={`${width} ${height} rounded-full`} src={avatarUrl} alt={initials} />
      ) : (
        <div
          className={`${width} ${height} rounded-full flex-col items-center justify-center flex bg-green-400 text-slate-800`}
        >
          <p className="font-bold">{initials}</p>
        </div>
      )}
    </>
  );
};

export default Avatar;
