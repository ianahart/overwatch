const RefundTitle = () => {
  return (
    <div data-testid="RefundTitle" className="flex flex-col items-center my-6">
      <h2 className="text-gray-400 text-xl">Refund Requests</h2>
      <p>Here is where users have requested refunds.</p>
      <p>You have the ability to fulfill their refunds or deny them, based on their reasoning for them.</p>
    </div>
  );
};

export default RefundTitle;
