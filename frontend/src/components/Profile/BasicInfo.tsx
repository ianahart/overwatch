import { IoLocationOutline } from 'react-icons/io5';
import Avatar from '../Shared/Avatar';
import ProfileContainer from './ProfileContainer';
import { useSelector } from 'react-redux';
import { TRootState } from '../../state/store';
import { BsThreeDots } from 'react-icons/bs';
import Connect from './Connect';
import Disconnect from './Disconnect';

export interface IBasicInfoProps {
  userId: number;
  avatarUrl: string;
  fullName: string;
  country: string;
  abbreviation: string;
  city: string;
}

const BasicInfo = ({ userId, city, abbreviation, avatarUrl, fullName, country }: IBasicInfoProps) => {
  const { user } = useSelector((store: TRootState) => store.user);

  return (
    <ProfileContainer>
      <>
        <div className="flex justify-between text-gray-400">
          <div className="flex items-center">
            <div className="avatar mr-5">
              <Avatar initials={abbreviation} avatarUrl={avatarUrl} width="w-24" height="h-24" />
            </div>
            <div className="words">
              <h3 className="text-2xl">{fullName}</h3>
              <div className="flex items-center">
                <IoLocationOutline className="mr-1 text-red-400" />
                <p>
                  {city}, {country}
                </p>
              </div>
            </div>
          </div>
          {user.id === userId && (
            <div className="options">
              <BsThreeDots className="text-gray-400" />
            </div>
          )}
        </div>
      {/*
        {userId !== user.id && user.role === 'REVIEWER' && <Disconnect senderId={userId} receiverId={user.id} />}
        {userId !== user.id && user.role !== 'REVIEWER' && (
          <Connect
            avatarUrl={avatarUrl}
            abbreviation={abbreviation}
            fullName={fullName}
            receiverId={userId}
            senderId={user.id}
          />
        )}
                */}
      </>
    </ProfileContainer>
  );
};

export default BasicInfo;
