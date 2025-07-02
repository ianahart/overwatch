import { useEffect, useState } from 'react';

export interface IAddBillingMethodProps {
  handleSetView: (view: string) => void;
}

const AddBillingMethod = ({ handleSetView }: IAddBillingMethodProps) => {
  const [inputValue, setInputValue] = useState('');
  useEffect(() => {
    if (inputValue.length) {
      handleSetView('billing');
    }
  }, [inputValue]);

  return (
    <div data-testid="AddBillingMethod">
      <div className="flex justify-between items-center">
        <h3 className="text-xl text-gray-400">Add a billing method</h3>
        <button onClick={() => handleSetView('main')} className="outline-btn border border-gray-800 !text-green-400">
          Cancel
        </button>
      </div>
      <div>
        <input value="next" onChange={(e) => setInputValue(e.target.value)} id="billing" name="billing" type="radio" />
        <label className="mx-2" htmlFor="billing">
          Payment card
        </label>
        <p className="inline text-sm">Visa, Mastercard, American Express, Discover</p>
      </div>
      <div className="my-4">
        <p className="text-sm">More payment methods coming soon.</p>
      </div>
    </div>
  );
};

export default AddBillingMethod;
