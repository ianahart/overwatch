import { FaSpinner } from 'react-icons/fa';

interface ISpinnerProps {
  message: string;
}

const Spinner = ({ message }: ISpinnerProps) => {
  return (
    <div className="flex justify-center flex-col items-center">
      <div className="animate-spin">
        <FaSpinner className="text-green-400 text-3xl" />
      </div>
      <p className="text-slate-400 text-sm">{message}</p>
    </div>
  );
};

export default Spinner;
