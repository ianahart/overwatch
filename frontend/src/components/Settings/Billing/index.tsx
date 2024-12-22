import { useEffect, useState } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';
import { ToastContainer, toast } from 'react-toastify';
import { useSearchParams } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';
import { useSelector } from 'react-redux';
import Header from '../Header';
import NoBillingMethod from './NoBillingMethod';
import AddBillingMethod from './AddBillingMethod';
import BillingForm from './BillingForm';
import { TRootState, useConnectAccountMutation, useFetchPaymentMethodQuery } from '../../../state/store';
import PaymentMethod from './PaymentMethod';
import ReviewerConnectAccount from './Reviewer/ReviewerConnectAccount';
import ReviewerDisconnectAccount from './Reviewer/ReviewerDisconnectAccount';
import CompletedPayments from './User/CompletedPayments';

const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_API_KEY);

const paymentMethodState = {
  id: 0,
  last4: '',
  displayBrand: '',
  expMonth: 0,
  expYear: 0,
  name: '',
  stripeEnabled: false,
};

const Billing = () => {
  const [searchParams] = useSearchParams();
  const showToast = searchParams.get('toast') === 'show' ? true : false;
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [paymentMethod, setPaymentMethod] = useState(paymentMethodState);
  const { data } = useFetchPaymentMethodQuery({ userId: user.id, token }, { skip: !user.id || !token });
  const [connectReviewerAccount] = useConnectAccountMutation();
  const [view, setView] = useState('main');

  useEffect(() => {
    if (showToast) {
      initiateToast();
    }
  }, [showToast]);

  const initiateToast = () => {
    toast.success('You need to add a payment method before adding a review.', {
      position: 'top-center',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      type: 'warning',
      theme: 'dark',
    });
  };

  const handleSetView = (newView: string): void => {
    setView(newView);
  };

  const handleSetStripeEnabled = (stripeEnabled: boolean): void => {
    setPaymentMethod((prevState) => ({
      ...prevState,
      stripeEnabled,
    }));
  };

  useEffect(() => {
    if (data) {
      setPaymentMethod(data.data);
    }
  }, [data]);

  const completeOnBoarding = (onBoardingUrl: string): void => {
    window.location.href = onBoardingUrl;
  };

  const handleConnectReviewerAccount = (): void => {
    const payload = { userId: user.id, email: user.email, token };
    connectReviewerAccount(payload)
      .unwrap()
      .then((res) => {
        if (res.message === 'success') {
          completeOnBoarding(res.data);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="p-4">
      <Header heading="Billing & payments" />
      <div className=" my-4 rounded-lg min-h-[200px] p-4">
        {user.role === 'USER' && (
          <>
            <div className="rounded-lg border 1pb xolis border-gray-800 p-2">
              {!paymentMethod.stripeEnabled && view === 'main' && <NoBillingMethod handleSetView={handleSetView} />}
              {!paymentMethod.stripeEnabled && view === 'add' && <AddBillingMethod handleSetView={handleSetView} />}
              {!paymentMethod.stripeEnabled && view === 'billing' && (
                <Elements stripe={stripePromise} options={{ mode: 'setup', currency: 'usd' }}>
                  <BillingForm handleSetView={handleSetView} />
                </Elements>
              )}
              {paymentMethod.stripeEnabled && paymentMethod.id !== 0 && (
                <PaymentMethod handleSetStripeEnabled={handleSetStripeEnabled} data={paymentMethod} />
              )}
            </div>
            <CompletedPayments userId={user.id} token={token} />
          </>
        )}
        {user.role === 'REVIEWER' && (
          <>
            {!paymentMethod.stripeEnabled && (
              <ReviewerConnectAccount handleConnectReviewerAccount={handleConnectReviewerAccount} />
            )}
            {paymentMethod.stripeEnabled && paymentMethod.id !== 0 && (
              <ReviewerDisconnectAccount
                handleSetStripeEnabled={handleSetStripeEnabled}
                paymentMethodId={paymentMethod.id}
              />
            )}
          </>
        )}
      </div>
      <ToastContainer />
    </div>
  );
};

export default Billing;
