import { useLocation } from 'react-router-dom';

const NotFoundRoute = () => {
  const location = useLocation();
  const { status, data } = location.state || { status: '', data: { message: '' } };
  return (
    <h1>
      {status} - {data.message}
    </h1>
  );
};

export default NotFoundRoute;
