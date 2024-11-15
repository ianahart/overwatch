import RefundList from './RefundList';
import RefundTitle from './RefundTitle';

const Refund = () => {
  return (
    <div className="md:max-w-[1450px] w-full mx-auto mt-8">
      <div className="bg-gray-900 p-2 rounded">
        <RefundTitle />
        <RefundList />
      </div>
    </div>
  );
};
export default Refund;
