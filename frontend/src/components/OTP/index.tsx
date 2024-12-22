import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useLocation, useNavigate } from 'react-router-dom';
import { useFetchOTPQuery, useVerifyOTPMutation } from '../../state/store';
import { updateUserAndTokens } from '../../state/store';

const OTP = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [otpCode, setOtpCode] = useState('');
  const location = useLocation();
  useFetchOTPQuery(location.state.userId);
  const [verifyOTP] = useVerifyOTPMutation();
  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setOtpCode(value);
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    if (otpCode.trim().length === 0) {
      return;
    }

    verifyOTP({ userId: location.state.userId, otpCode })
      .unwrap()
      .then(({ user, token, refreshToken }) => {
        const tokens = { token, refreshToken };
        dispatch(updateUserAndTokens({ user, tokens }));
        navigate(`/dashboard/${user.slug}`);
      })
      .catch((err) => {
        setError(err.data.message);
      });
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-[90vh]">
      <div className="rounded bg-stone-950 md:min-w-[400px] min-w-full min-h-96">
        <form onSubmit={handleSubmit} className="p-4 flex flex-col justify-between min-h-96">
          <div>
            <h1 className="text-gray-400 text-2xl">Enter Code</h1>
            <p className="text-sm">Check your phone for a text that includes a one time pass code</p>
            {error.length > 0 && (
              <div className="flex justify-center my-2">
                <p className="text-sm text-red-400">{error}</p>
              </div>
            )}
            <div className="my-4">
              <label htmlFor="otpCode">OTP Code</label>
              <input
                value={otpCode}
                onChange={handleChange}
                id="otpCode"
                name="otpCode"
                placeholder="Enter OTP code..."
                className="h-9 rounded bg-transparent border border-gray-800 w-full pl-2 shadow"
              />
            </div>
          </div>
          <div className="justify-center flex my-10 w-full p-4">
            <button className="btn w-full" type="submit">
              Proceed
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default OTP;
