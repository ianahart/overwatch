import Header from '../Header';
import GetPaidCompleteRepositoryList from './GetPaidCompleteRepositoryList';

const GetPaid = () => {
  return (
    <div className="p-4">
      <Header heading="Get Paid" />
      <div className="my-8 max-w-[480px] w-full leading-7">
        <p>
          Here you can finalize payments on reviews you have completed. Just click the get paid button next to a review
          you have completed.
        </p>
      </div>
      <div className="my-8">
        <GetPaidCompleteRepositoryList />
      </div>
    </div>
  );
};

export default GetPaid;
