import { useSelector } from 'react-redux';
import Header from '../Header';
import Package from './Package';
import { TRootState } from '../../../state/store';

const Packages = () => {
  const { basic, standard, pro } = useSelector((store: TRootState) => store.package);

  return (
    <div>
      <Header heading="Service & Offerings" />
      <Package data={basic} name="basic" title="Basic" />
      <Package data={standard} name="standard" title="Standard" />
      <Package data={pro} name="pro" title="Pro" />
    </div>
  );
};

export default Packages;
