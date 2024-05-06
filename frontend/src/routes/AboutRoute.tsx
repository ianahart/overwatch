import { useSelector } from 'react-redux';
import { TRootState, useLazyFetchHeartBeatQuery } from '../state/store';

const AboutRoute = () => {
  const [getHeartBeat] = useLazyFetchHeartBeatQuery();
  const { token } = useSelector((store: TRootState) => store.user);

  const handleClick = () => {
    getHeartBeat(token);
  };

  return (
    <>
      <h1>about route</h1>
      <button onClick={handleClick}>get heartbeat</button>
    </>
  );
};

export default AboutRoute;
