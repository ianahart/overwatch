import TransactionList from './TransactionList';
import TransactionTitle from './TransactionTitle';

const Transaction = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="bg-gray-900 p-2 rounded">
        <TransactionTitle />
        <TransactionList />
      </div>
    </div>
  );
};

export default Transaction;
