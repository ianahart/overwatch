import { AiOutlinePlus } from 'react-icons/ai';

export interface INoBillingMethodProps {
  handleSetView: (view: string) => void;
}

const NoBillingMethod = ({ handleSetView }: INoBillingMethodProps) => {
  return (
    <div data-testid="NoBillingMethod">
      <h3 className="text-gray-400">Billing methods</h3>
      <p>
        You haven't setup any billing methods yet for OverWatch. You will not be able to pay for services until you
        provide a billing method.
      </p>
      <div
        data-testid="setting-billing-add-payment-method"
        onClick={() => handleSetView('add')}
        className="cursor-pointer my-4 flex items-center text-green-400"
      >
        <AiOutlinePlus className="text-xl" />
        <p className="text-xl">Add a billing method</p>
      </div>
    </div>
  );
};

export default NoBillingMethod;
