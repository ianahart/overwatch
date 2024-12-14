import dayjs from 'dayjs';
import { IAdminAppTestimonial } from '../../../../../interfaces';
import Avatar from '../../../../Shared/Avatar';
import { initializeName } from '../../../../../util';
import { AiOutlineCheck } from 'react-icons/ai';
import ToolTip from '../../../../Shared/ToolTip';

export interface IAppTestimonialListItemProps {
  adminAppTestimonial: IAdminAppTestimonial;
  selectAdminAppTestimonial: (id: number, isSelected: boolean) => void;
}

const AppTestimonialListItem = ({ adminAppTestimonial, selectAdminAppTestimonial }: IAppTestimonialListItemProps) => {
  const handleOnClick = (): void => {
    selectAdminAppTestimonial(adminAppTestimonial.id, !adminAppTestimonial.isSelected);
  };

  return (
    <li className="my-6">
      <div className="flex items-center justify-around">
        <div className="flex-grow-[1] mr-4 flex justify-center">
          <ToolTip message={adminAppTestimonial.isSelected ? 'Unselect' : 'Select'}>
            <div
              onClick={handleOnClick}
              className={`w-6 h-6 rounded-full border border-gray-800 flex cursor-pointer flex-col justify-center items-center ${
                adminAppTestimonial.isSelected ? 'bg-green-400' : 'bg-transparent'
              }`}
            >
              <AiOutlineCheck className="text-black" />
            </div>
          </ToolTip>
        </div>
        <div className="flex-grow-[3] max-w-[600px] border border-gray-800 rounded-lg p-2">
          <div className="flex items-center">
            <Avatar
              height="h-9"
              width="w-9"
              initials={initializeName(adminAppTestimonial.firstName, '')}
              avatarUrl={adminAppTestimonial.avatarUrl}
            />
            <div className="ml-2">
              <p className="text-gray-400">{adminAppTestimonial.firstName}</p>
              <p className="text-xs">{dayjs(adminAppTestimonial.createdAt).format('MM/DD/YYYY')}</p>
            </div>
          </div>
          <div className="my-2">
            <p className="italic text-sm">{adminAppTestimonial.developerType}</p>
            <p className="text-gray-400">{adminAppTestimonial.content}</p>
          </div>
        </div>
      </div>
    </li>
  );
};

export default AppTestimonialListItem;
