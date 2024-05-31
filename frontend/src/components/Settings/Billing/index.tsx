import { useState } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';
import Header from '../Header';
import NoBillingMethod from './NoBillingMethod';
import AddBillingMethod from './AddBillingMethod';
import BillingForm from './BillingForm';

const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_API_KEY);

const Billing = () => {
  const [hasBillingMethod, setHasBillingMethod] = useState(false);
  const [clientSecret, setClientSecret] = useState('');
  const [view, setView] = useState('main');

  const handleSetView = (newView: string) => {
    setView(newView);
  };

  return (
    <div className="p-4">
      <Header heading="Billing & payments" />
      <div className=" my-4 rounded-lg border 1px solid border-gray-800 min-h-[200px] p-4">
        {!hasBillingMethod && view === 'main' && <NoBillingMethod handleSetView={handleSetView} />}
        {!hasBillingMethod && view === 'add' && <AddBillingMethod handleSetView={handleSetView} />}
        {!hasBillingMethod && view === 'billing' && (
          <Elements stripe={stripePromise} options={{ mode: 'setup', currency: 'usd' }}>
            <BillingForm handleSetView={handleSetView} />
          </Elements>
        )}
      </div>
    </div>
  );
};

export default Billing;
