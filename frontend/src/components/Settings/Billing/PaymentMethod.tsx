import { FaCcMastercard } from 'react-icons/fa';
import { FaCcVisa } from 'react-icons/fa';
import { SiAmericanexpress } from 'react-icons/si';
import { FaCcDiscover } from 'react-icons/fa';
import { CiCreditCard1 } from 'react-icons/ci';
import { BsTrash } from 'react-icons/bs';
import { useSelector } from 'react-redux';
import { TRootState, useDeletePaymentMethodMutation } from '../../../state/store';

export interface IPaymentMethodProps {
  data: { id: number; last4: string; displayBrand: string; expYear: number; expMonth: number; name: string };
  handleSetHasBillingMethod: (hasBillingMethod: boolean) => void;
}

const PaymentMethod = ({ data, handleSetHasBillingMethod }: IPaymentMethodProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deletePaymentMethod] = useDeletePaymentMethodMutation();

  const renderCard = () => {
    if (data.displayBrand.includes('visa')) {
      return <FaCcVisa className="text-gray-400 text-xl" />;
    } else if (data.displayBrand.includes('mastercard')) {
      return <FaCcMastercard className="text-gray-400 text-xl" />;
    } else if (data.displayBrand.includes('discover')) {
      return <FaCcDiscover className="text-gray-400 text-xl" />;
    } else if (data.displayBrand.includes('american express')) {
      return <SiAmericanexpress className="text-gray-400 text-xl" />;
    } else {
      return <CiCreditCard1 className="text-gray-400 text-xl" />;
    }
  };

  const handleOnDeleteCustomer = () => {
    deletePaymentMethod({ token, id: data.id })
      .unwrap()
      .then(() => handleSetHasBillingMethod(false));
  };

  return (
    <div>
      <div className="my-4">
        <h3 className="text-gray-400 text-xl">Your payment method</h3>
      </div>
      <div className="p-4 rounded bg-stone-950 flex justify-between">
        <div>
          <p>{data.name}</p>
          <div className="flex items-center my-1">
            {renderCard()}
            <p className="text-sm ml-1">************{data.last4}</p>
          </div>
          <p className="text-sm">
            Expiration date: {data.expMonth}/{data.expYear}
          </p>
        </div>
        <div className="cursor-pointer" onClick={handleOnDeleteCustomer}>
          <BsTrash />
        </div>
      </div>
    </div>
  );
};

export default PaymentMethod;
