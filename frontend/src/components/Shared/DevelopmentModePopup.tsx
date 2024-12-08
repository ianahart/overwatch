import { AiOutlineClose } from 'react-icons/ai';

export interface IDevelopmentModePopupProps {
  handleCloseModal: () => void;
}

const DevelopmentModePopup = ({ handleCloseModal }: IDevelopmentModePopupProps) => {
  return (
    <div className="fixed inset-0 bg-gray-800 bg-opacity-90">
      <div className="flex items-center flex-col justify-center min-h-[60vh]">
        <div className="max-w-[600px] bg-gray-900 rounded shadow-lg p-2 w-full">
          <div className="m-2 flex justify-end">
            <div onClick={handleCloseModal} className="bg-black p-2 rounded-3xl cursor-pointer">
              <AiOutlineClose className="text-4xl text-white" />
            </div>
          </div>
          <h3 className="font-display tracking-wider text-gray-400 text-xl">Development Mode Notice</h3>
          <p className="text-gray-400 leading-7">
            Development Mode Notice This application is currently running in development mode. While we strive to
            provide a seamless experience, some features may be incomplete or might not function as expected during this
            phase. Please note that transactions will not work while in development mode. To simulate transactions you
            can use a stripe test card number like <span className="font-bold">4242 4242 4242 4242</span> We value your
            feedback and encourage you to reach out if you encounter any issues or have questions. Feel free to email us
            at
            <span className="font-bold text-blue-400"> codeoverwatch@coderoverwatch.com</span>. Thank you for your
            understanding and support as we work to improve the application!
          </p>
          <div className="my-4 flex justify-center">
            <button onClick={handleCloseModal} className="btn">
              Got it!
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DevelopmentModePopup;
