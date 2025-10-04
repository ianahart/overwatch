const TransactionTitle = () => {
  return (
    <div data-testid="TransactionTitle" className="flex flex-col items-center my-6">
      <h2 className="text-gray-400 text-xl">Transactions</h2>
      <p>Here is where users and reviewers have made transactions.</p>
      <p>
        You have the ability to search for transactions by name and export transactions by{' '}
        <span className="text-gray-400 font-bold">CSV</span> and <span className="text-gray-400 font-bold">PDF</span>.
      </p>
    </div>
  );
};
export default TransactionTitle;
