import React, { useState } from 'react';
import { useStripe, useElements, CardElement } from '@stripe/react-stripe-js'; // Import CardElement
import axios from 'axios';
import Spinner from '../../Shared/Spinner';

export interface IBillingFormProps {
  handleSetView: (view: string) => void;
}

const BillingForm = ({ handleSetView }: IBillingFormProps) => {
  const stripe = useStripe();
  const elements = useElements();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const cardElementStyle = {
    base: {
      color: '#6b7280',
    },
  };

  const handleOnSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!stripe || !elements) {
      return;
    }

    setLoading(true);

    // Collect card details using CardElement (previously known as PaymentElement)
    const cardElement = elements.getElement(CardElement);

    // Create Payment Method
    const { error, paymentMethod } = await stripe.createPaymentMethod({
      type: 'card',
      card: cardElement!,
      billing_details: {
        email: '',
        name: '',
        address: {
          country: 'US',
          city: 'meredith',
          line1: '43 bluewater road',
          line2: '',
          state: 'nh',
        },
      },
    });

    console.log(paymentMethod);
    if (error) {
      console.error(error);
      setLoading(false);
      return;
    }

    try {
      // Send payment method details to your server to save for future payments
      const response = await axios.post('/api/payment/save', {
        paymentMethod: paymentMethod,
        // Add additional user information here if needed
      });
      console.log('Payment Method Saved:', response.data);
      // Clear any previous errors
      setError('');
    } catch (error) {
      console.error('Error saving payment method', error);
      setError('Error saving payment method. Please try again.');
    }

    setLoading(false);
  };

  return (
    <form onSubmit={handleOnSubmit}>
      <div className="flex justify-between items-center">
        <h3 className="text-xl text-gray-400">Add a billing method</h3>
        <button onClick={() => handleSetView('add')} className="outline-btn border border-gray-800 !text-green-400">
          Cancel
        </button>
      </div>
      <div className="mb-1">
        <p>Payment card</p>
      </div>
      <div className="border border-gray-800 p-2 my-2 rounded" id="card-element" style={{ marginBottom: '20px' }}>
        <CardElement options={{ style: cardElementStyle }} />
      </div>
      {error && <div className="text-red-500">{error}</div>}
      {loading && <Spinner message="Saving..." />}
            {!loading && 
      <button className="btn" type="submit" disabled={!stripe || loading}>
        Save Payment Method
      </button>}
    </form>
  );
};

export default BillingForm;
