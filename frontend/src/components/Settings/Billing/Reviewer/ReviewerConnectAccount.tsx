export interface IReviewerConnectAccountProps {
  handleConnectReviewerAccount: () => void;
}

const ReviewerConnectAccount = ({ handleConnectReviewerAccount }: IReviewerConnectAccountProps) => {
  return (
    <div data-testid="ReviewerConnectAccount">
      <h3 className="text-xl">Connect your account</h3>
      <p className="mt-2">You need to connect your account in order to recieve payments.</p>
      <p className="mt-2">
        After clicking the button below you will be redirected to{' '}
        <a href="https://stripe.com/" className="font-bold text-blue-400">
          Stripe
        </a>{' '}
        to complete your onboarding setup.
      </p>
      <div className="my-6">
        <button onClick={handleConnectReviewerAccount} className="btn">
          Connect your account
        </button>
      </div>
    </div>
  );
};

export default ReviewerConnectAccount;
