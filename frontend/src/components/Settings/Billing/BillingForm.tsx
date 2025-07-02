import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useStripe, useElements, CardElement } from '@stripe/react-stripe-js'; // Import CardElement
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { FaMapPin, FaRegBuilding } from 'react-icons/fa';
import { IoIosPin } from 'react-icons/io';
import { useNavigate } from 'react-router-dom';
import { PaymentMethod } from '@stripe/stripe-js';
import Spinner from '../../Shared/Spinner';
import { countryCodes, savePaymentFormState } from '../../../data';
import { IPaymentMethod, ISavePaymentForm } from '../../../interfaces';
import FormInputField from '../../Form/FormInputField';
import { AiOutlineUser } from 'react-icons/ai';
import FormSelect from '../../Form/FormSelect';
import { TRootState, useCreatePaymentMethodMutation } from '../../../state/store';

export interface IBillingFormProps {
  handleSetView: (view: string) => void;
}

const BillingForm = ({ handleSetView }: IBillingFormProps) => {
  const cardElementStyle = {
    base: {
      color: '#6b7280',
    },
  };

  const navigate = useNavigate();
  const [createPaymentMethod] = useCreatePaymentMethodMutation();
  const { user, token } = useSelector((store: TRootState) => store.user);
  const stripe = useStripe();
  const elements = useElements();
  const [form, setForm] = useState(savePaymentFormState);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleUpdateField = (name: string, value: string, attribute: string) => {
    setForm((prevState) => ({
      ...prevState,
      [name]: { ...prevState[name as keyof ISavePaymentForm], [attribute]: value },
    }));
  };
  const updateSelectField = (name: string, value: string) => {
    handleUpdateField(name, value, 'value');
  };

  const clearErrors = () => {
    setError('');
    for (const key of Object.keys(form)) {
      handleUpdateField(key, '', 'error');
    }
  };

  const validateForm = () => {
    let errors = false;

    for (const key of Object.keys(form)) {
      const exclude = ['line2'];
      const { value, error } = form[key as keyof ISavePaymentForm];
      if ((value.trim().length === 0 || error.length) && !exclude.includes(key)) {
        console.log('run', key);
        handleUpdateField(key, 'Please make sure to fill out field', 'error');
        errors = true;
      }
    }
    return !errors;
  };

  const createRequestData = (data: PaymentMethod) => {
    const body = {} as IPaymentMethod;
    body.id = data?.id;
    body.city = data?.billing_details?.address?.city as string;
    body.country = data?.billing_details?.address?.country as string;
    body.line1 = data?.billing_details?.address?.line1 as string;
    body.line2 = data?.billing_details?.address?.line2 as string;
    body.postalCode = data?.billing_details?.address?.postal_code as string;
    body.name = data?.billing_details?.name as string;
    body.displayBrand = data.card?.brand as string;
    body.type = data.type as string;
    body.expMonth = data.card?.exp_month as number;
    body.expYear = data.card?.exp_year as number;
    return body;
  };

  const initiateToast = () => {
    toast.success('Your payment info was successfully saved!', {
      position: 'bottom-center',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: 'dark',
      onClose: () => navigate(`/settings/${user.slug}/profile`),
    });
  };

  const handleOnSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    clearErrors();
    if (!validateForm()) {
      return;
    }
    if (!stripe || !elements) {
      return;
    }

    setLoading(true);

    const cardElement = elements.getElement(CardElement);

    const { error, paymentMethod } = await stripe.createPaymentMethod({
      type: 'card',
      card: cardElement!,
      billing_details: {
        name: `${form.firstName.value} ${form.lastName.value}`,
        address: {
          country: form.country.value,
          city: form.city.value,
          line1: form.line1.value,
          line2: form.line2.value,
          state: form.state.value,
          postal_code: form.postalCode.value,
        },
      },
    });

    if (error) {
      console.error(error);
      setLoading(false);
      return;
    }

    const body = createRequestData(paymentMethod);

    createPaymentMethod({ body, token, userId: user.id })
      .unwrap()
      .then(() => {
        setLoading(false);
        initiateToast();
      })
      .catch((err) => {
        console.log(err);
        setError('Error saving payment method. Please try again.');
        setLoading(false);
      });
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
        <CardElement data-testid="CardElement" options={{ style: cardElementStyle }} />
      </div>

      <div className="my-4 md:flex justify-between">
        <div className="md:w-[47%] w-full">
          <FormInputField
            handleUpdateField={handleUpdateField}
            name={form.firstName.name}
            value={form.firstName.value}
            error={form.firstName.error}
            type={form.firstName.type}
            label="First Name"
            id="firstName"
            errorField="First name"
            placeholder="Enter your first name"
            icon={<AiOutlineUser />}
          />
        </div>
        <div className="md:w-[47%] w-full">
          <FormInputField
            handleUpdateField={handleUpdateField}
            name={form.lastName.name}
            value={form.lastName.value}
            error={form.lastName.error}
            type={form.lastName.type}
            label="Last Name"
            id="lastName"
            errorField="Last name"
            placeholder="Enter your last name"
            icon={<AiOutlineUser />}
          />
        </div>
      </div>
      <div className="mt-10 mb-4">
        <h3 className="text-xl text-gray-400">Billing address</h3>
      </div>
      <div className="my-4 md:w-[80%] w-full">
        <FormSelect data={countryCodes} country={form.country.value} updateField={updateSelectField} />
      </div>
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.line1.name}
          value={form.line1.value}
          error={form.line1.error}
          type={form.line1.type}
          label="Address line 1"
          id="line1"
          errorField="Address 1"
          placeholder="Enter your address"
          icon={<FaRegBuilding />}
        />
      </div>
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.line2.name}
          value={form.line2.value}
          error={form.line2.error}
          type={form.line2.type}
          label="Address line 2 (optional)"
          id="line2"
          errorField="Address 2"
          placeholder=""
          icon={<FaRegBuilding />}
        />
      </div>
      <div className="my-4 md:flex justify-between">
        <div className="md:w-[47%] w-full">
          <FormInputField
            handleUpdateField={handleUpdateField}
            name={form.city.name}
            value={form.city.value}
            error={form.city.error}
            type={form.city.type}
            label="City"
            id="city"
            errorField="City"
            placeholder="Enter your city"
            icon={<FaMapPin />}
          />
        </div>
        <div className="md:w-[47%] w-full">
          <FormInputField
            handleUpdateField={handleUpdateField}
            name={form.state.name}
            value={form.state.value}
            error={form.state.error}
            type={form.state.type}
            label="State"
            id="state"
            errorField="State"
            placeholder="Enter your state"
            icon={<IoIosPin />}
          />
        </div>
      </div>
      <div className="my-4">
        <FormInputField
          handleUpdateField={handleUpdateField}
          name={form.postalCode.name}
          value={form.postalCode.value}
          error={form.postalCode.error}
          type={form.postalCode.type}
          label="Postal code"
          id="postalCode"
          errorField="Postal code"
          placeholder="Enter your postal code"
          icon={<IoIosPin />}
        />
      </div>

      {error && <div className="text-red-500">{error}</div>}
      {loading && <Spinner message="Saving..." />}
      {!loading && (
        <button className="btn" type="submit" disabled={!stripe || loading}>
          Save Payment Method
        </button>
      )}
      <ToastContainer />
    </form>
  );
};

export default BillingForm;
