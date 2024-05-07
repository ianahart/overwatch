import { useSelector } from 'react-redux';
import { TRootState, useLazyFetchHeartBeatQuery } from '../state/store';
const AboutRoute = () => {
  const [getHeartBeat, { data }] = useLazyFetchHeartBeatQuery();
  const { token } = useSelector((store: TRootState) => store.user);

  const handleClick = async () => {
    if (token) {
      try {
        const response = await getHeartBeat(token).unwrap();
        console.log(response, ' test');
      } catch (error) {
        console.log(error, 'hi');
      }
    } else {
      console.log('Token is not available');
    }
  };

  return (
    <>
      {data && data.message && <p>{data.message}</p>}
      <h1>About Route</h1>
      <button onClick={handleClick}>Get Heartbeat</button>
    </>
  );
};

export default AboutRoute;
