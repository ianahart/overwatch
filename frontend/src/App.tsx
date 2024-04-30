import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom';
import RootLayout from './layouts/RootLayout';
import AboutRoute from './routes/AboutRoute';
import ExploreRoute from './routes/ExploreRoute';
import CommunityRoute from './routes/CommunityRoute';
import SignInRoute from './routes/SignInRoute';
import SignUpRoute from './routes/SignUpRoute';

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<RootLayout />}>
      <Route path="about" element={<AboutRoute />} />
      <Route path="explore" element={<ExploreRoute />} />
      <Route path="community" element={<CommunityRoute />} />
      <Route path="signin" element={<SignInRoute />} />
      <Route path="signup" element={<SignUpRoute />} />
    </Route>
  )
);

const App = () => {
  return <RouterProvider router={router}></RouterProvider>;
};

export default App;
