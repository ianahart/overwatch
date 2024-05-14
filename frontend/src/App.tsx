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
import ResetPasswordRoute from './routes/ResetPasswordRoute';
import RequireAuth from './components/Guard/RequireAuth';
import SettingsRoute from './routes/SettingsRoute';
import BillingRoute from './routes/Settings/BillingRoute';
import ConnectsRoute from './routes/Settings/ConnectsRoute';
import ContactInfoRoute from './routes/Settings/ContactInfoRoute';
import ProfileRoute from './routes/Settings/ProfileRoute';
import EditProfileRoute from './routes/Settings/EditProfileRoute';
import ProfileSettingsRoute from './routes/Settings/ProfileSettingsRoute';
import GetPaidRoute from './routes/Settings/GetPaidRoute';
import TeamsRoute from './routes/Settings/TeamsRoute';
import SecurityRoute from './routes/Settings/SecurityRoute';
import NotificationSettingsRoute from './routes/Settings/NotificationSettingsRoute';
import OTPRoute from './routes/OTPRoute';

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
      <Route path="auth/otp" element={<OTPRoute />} />
      <Route
        path="reset-password"
        element={
          <RequireGuest>
            <ResetPasswordRoute />
          </RequireGuest>
        }
      />
      <Route
        path="settings/:slug"
        element={
          <RequireAuth>
            <SettingsRoute />
          </RequireAuth>
        }
      >
        <Route
          path="billing"
          element={
            <RequireAuth>
              <BillingRoute />
            </RequireAuth>
          }
        />
        <Route
          path="connects"
          element={
            <RequireAuth>
              <ConnectsRoute />
            </RequireAuth>
          }
        />
        <Route
          path="contact-info"
          element={
            <RequireAuth>
              <ContactInfoRoute />
            </RequireAuth>
          }
        />
        <Route
          path="profile"
          element={
            <RequireAuth>
              <ProfileRoute />
            </RequireAuth>
          }
        />
        <Route
          path="profile/edit"
          element={
            <RequireAuth>
              <EditProfileRoute />
            </RequireAuth>
          }
        />
        <Route
          path="profile/settings"
          element={
            <RequireAuth>
              <ProfileSettingsRoute />
            </RequireAuth>
          }
        />
        <Route
          path="pay"
          element={
            <RequireAuth>
              <GetPaidRoute />
            </RequireAuth>
          }
        />
        <Route
          path="teams"
          element={
            <RequireAuth>
              <TeamsRoute />
            </RequireAuth>
          }
        />
        <Route
          path="security"
          element={
            <RequireAuth>
              <SecurityRoute />
            </RequireAuth>
          }
        />
        <Route
          path="notifications/settings"
          element={
            <RequireAuth>
              <NotificationSettingsRoute />
            </RequireAuth>
          }
        />
      </Route>
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
