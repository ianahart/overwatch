export interface IProfileContainerProps {
  children: JSX.Element;
}

const ProfileContainer = ({ children }: IProfileContainerProps) => {
  return <div className="p-4 border rounded-t border-gray-800">{children}</div>;
};

export default ProfileContainer;
