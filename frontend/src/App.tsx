import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom';
import RootLayout from './layouts/RootLayout';
import AboutRoute from './routes/AboutRoute';
import ExploreRoute from './routes/ExploreRoute';
import CommunityRoute from './routes/CommunityRoute';
import SignInRoute from './routes/SignInRoute';
import SignUpRoute from './routes/SignUpRoute';
import { updateUserAndTokens, useSyncUserQuery } from './state/store';
import { retrieveTokens } from './util';
import { useDispatch } from 'react-redux';
import { useEffect } from 'react';
import ForgotPasswordRoute from './routes/ForgotPasswordRoute';
import RequireGuest from './components/Guard/RequireGuest';

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<RootLayout />}>
      <Route path="about" element={<AboutRoute />} />
      <Route path="explore" element={<ExploreRoute />} />
      <Route path="community" element={<CommunityRoute />} />
      <Route
        path="signin"
        element={
          <RequireGuest>
            <SignInRoute />
          </RequireGuest>
        }
      />
      <Route
        path="signup"
        element={
          <RequireGuest>
            <SignUpRoute />
          </RequireGuest>
        }
      />
      <Route
        path="forgot-password"
        element={
          <RequireGuest>
            <ForgotPasswordRoute />
          </RequireGuest>
        }
      />
    </Route>
  )
);

const App = () => {
  const dispatch = useDispatch();
  const token = retrieveTokens()?.token;
  const { data } = token ? useSyncUserQuery(token) : { data: null };

  useEffect(() => {
    if (data) {
      dispatch(updateUserAndTokens({ user: data, tokens: retrieveTokens() }));
    }
  }, [data, dispatch, token]);

  return <RouterProvider router={router}></RouterProvider>;
};

export default App;
