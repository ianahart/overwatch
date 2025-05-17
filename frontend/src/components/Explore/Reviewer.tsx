import { IoPin } from 'react-icons/io5';
import { AiFillStar, AiOutlineLike } from 'react-icons/ai';
import { FaHeart } from 'react-icons/fa';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router-dom';

import { IMinProfile } from '../../interfaces';
import Avatar from '../Shared/Avatar';
import { useSelector } from 'react-redux';
import { TRootState, useToggleFavoriteMutation } from '../../state/store';

export interface IReviewerProps {
  reviewer: IMinProfile;
  filterValue: string;
  updateFavoritedReviewer: (id: number, isFavorited: boolean) => void;
}

const Reviewer = ({ reviewer, filterValue, updateFavoritedReviewer }: IReviewerProps) => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [toggleFavorite] = useToggleFavoriteMutation();
  const navigate = useNavigate();

  const isNotActive = dayjs(reviewer.lastActive).isBefore(dayjs().subtract(1, 'day'));

  const goToProfile = (profileId: number) => {
    navigate(`/profiles/${profileId}`);
  };

  const handleToggleFavorite = () => {
    const payload = { profileId: reviewer.id, userId: user.id, isFavorited: reviewer.isFavorited, token };
    toggleFavorite(payload)
      .unwrap()
      .then(() => {
        updateFavoritedReviewer(reviewer.id, !reviewer.isFavorited);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div data-testid="reviewer-article" className="my-4 border border-gray-800 rounded-lg p-4">
      <div className="header md:flex md:justify-between">
        <div className="flex items-center">
          <Avatar width="w-16" height="h-16" avatarUrl={reviewer.avatarUrl} initials="??" />
          <div>
            <h3 className="ml-2 text-lg font-bold text-gray-400">{reviewer.fullName}</h3>
            <p className="ml-2 text-xs">
              Member since<span className="ml-1">{dayjs(reviewer.createdAt).format('MMMM, YYYY')}</span>
            </p>
            <div className="flex items-center">
              <IoPin />
              <p className="text-lg font-bold">{reviewer.country}</p>
            </div>
          </div>
        </div>
        <div className="actions">
          <div className="flex justify-end">
            <AiOutlineLike className="text-xl mx-1 cursor-pointer" />
            <FaHeart
              data-testid="favorite-reviewer"
              onClick={handleToggleFavorite}
              className={`${reviewer.isFavorited ? 'text-red-400' : 'text-gray-400'}  text-xl mx-1 cursor-pointer`}
            />
          </div>
          <div className=" my-2 flex items-center justify-end">
            <AiFillStar className="text-xl text-yellow-400" />
            <p className="text-lg">{reviewer.reviewAvgRating ?? 0}</p>
          </div>
          <p className="text-xs my-2">
            Number of reviews: <span className="text-lg">{reviewer.numOfReviews ?? 0}</span>
          </p>
        </div>
      </div>
      <div className="my-4">
        {reviewer.weekendsAvailable && (
          <p className="text-sm font-bold bg-blue-500 bg-opacity-55 w-36 text-blue-300 shadow-md p-1 rounded">
            Weekends Available
          </p>
        )}
      </div>
      <h3 className="text-gray-400">Availability</h3>
      <p className={`text-sm ${isNotActive ? 'text-gray-400' : 'text-green-400'}`}>{reviewer.lastActiveReadable}</p>
      <div className="flex flex-wrap mb-4">
        {reviewer.availability.map((availability) => {
          return (
            <div className="m-2" key={availability.day}>
              {availability.slots.length > 0 && <p>{availability.day}</p>}
            </div>
          );
        })}
      </div>
      <div className="flex flex-wrap my-4">
        {reviewer.programmingLanguages.map((programmingLanguage) => {
          return (
            <div
              className={`m-2  ${
                filterValue === 'most-relevant' && programmingLanguage.isCompatible ? 'bg-green-400' : 'bg-blue-500'
              } text-white bg-opacity-55 p-2 rounded-lg`}
              key={programmingLanguage.id}
            >
              <p>{programmingLanguage.name}</p>
            </div>
          );
        })}
      </div>
      <div className="flex justify-end">
        <button onClick={() => goToProfile(reviewer.id)} className="outline-btn bg-gray-400">
          View
        </button>
      </div>
    </div>
  );
};

export default Reviewer;
