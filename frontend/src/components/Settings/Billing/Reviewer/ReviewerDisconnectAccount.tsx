import { useSelector } from 'react-redux';
import { TRootState, useDeletePaymentMethodMutation } from '../../../../state/store';

export interface IReviewerDisconnectAccountProps {
  handleSetStripeEnabled: (stripeEnabled: boolean) => void;
  paymentMethodId: number;
}

const ReviewerDisconnectAccount = ({ handleSetStripeEnabled, paymentMethodId }: IReviewerDisconnectAccountProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const [deletePaymentMethod] = useDeletePaymentMethodMutation();

  const disconnectAccount = (): void => {
    deletePaymentMethod({ token, id: paymentMethodId })
      .unwrap()
      .then(() => handleSetStripeEnabled(false));
  };

  return (
    <div>
      <h3 className="text-xl">Disconnect your Stripe account</h3>
      <p className="mt-2">
        To disconnect your{' '}
        <a href="https://stripe.com/" className="font-bold text-blue-400">
          Stripe
        </a>{' '}
        account click on the button below.
      </p>
      <p className="mt-2">If you have negative account balances you will not be able to delete your account.</p>
      <div className="my-8">
        <button onClick={disconnectAccount} className="text-yellow-400 p-2 rounded border border-yellow-400">
          Disconnect Account
        </button>
      </div>
    </div>
  );
};

export default ReviewerDisconnectAccount;
