import { AiOutlinePlus } from 'react-icons/ai';
import CardHeaderCustomField from './CardHeaderCustomField';

export interface ICardStartCustomFieldProps {
  page: number;
  navigateNextPage: () => void;
  navigatePrevPage: () => void;
  handleCloseClickAway: () => void;
}

const CardStartCustomField = ({
  page,
  navigatePrevPage,
  navigateNextPage,
  handleCloseClickAway,
}: ICardStartCustomFieldProps) => {
  return (
    <div className="min-h-[400px] flex flex-col">
      <CardHeaderCustomField
        page={page}
        handleCloseClickAway={handleCloseClickAway}
        navigatePrevPage={navigatePrevPage}
      />
      <div className="flex flex-col flex-grow justify-between my-6">
        <div>List of existing custom fields</div>
        <div>Suggested fields</div>
      </div>
      <div className="mt-auto">
        <button
          onClick={navigateNextPage}
          className="flex items-center justify-center hover:bg-gray-950 py-1 px-2 w-full rounded bg-gray-900"
        >
          <AiOutlinePlus className="mr-2" /> New field
        </button>
      </div>
    </div>
  );
};
export default CardStartCustomField;
