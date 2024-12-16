/// <reference types="vite-plugin-svgr/client" />
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
import TestimonialRoute from './routes/Settings/TestimonialRoute';
import NotificationSettingsRoute from './routes/Settings/NotificationSettingsRoute';
import CreateReviewRoute from './routes/CreateReviewRoute';
import OTPRoute from './routes/OTPRoute';
import ExploreProfileRoute from './routes/ExploreProfileRoute';
import EditReviewRoute from './routes/EditReviewRoute';
import DashboardRoute from './routes/Dashboard/DashboardRoute';
import AddReviewRoute from './routes/Dashboard/User/AddReviewRoute';
import ReviewsRoute from './routes/Dashboard/User/ReviewsRoute';
import StatisticRoute from './routes/Dashboard/User/StatisticRoute';
import GuideRoute from './routes/Dashboard/User/GuideRoute';
import GitHubSuccessRoute from './routes/GitHubSuccessRoute';
import EditRepositoryReviewRoute from './routes/Dashboard/User/EditRepositoryReviewRoute';
import ReviewerStatisticRoute from './routes/Dashboard/Reviewer/StatistictRoute';
import ReviewerReviewsRoute from './routes/Dashboard/Reviewer/ReviewsRoute';
import RepositoryReviewRoute from './routes/Dashboard/Reviewer/RepositoryReviewRoute';
import RequireAuthUser from './components/Guard/RequireAuthUser';
import RequireAuthReviewer from './components/Guard/RequireAuthReviewer';
import WorkSpaceContainerRoute from './routes/Dashboard/Reviewer/WorkSpaceContainerRoute';
import WorkSpaceRoute from './routes/Dashboard/Reviewer/WorkSpaceRoute';
import ReviewFeedbackRoute from './components/Dashboard/Routes/User/Reviews/ReviewFeedbackRoute';
import CreateCommunityTopicRoute from './routes/CreateCommunityTopicRoute';
import NotFoundRoute from './routes/NotFoundRoute';
import TopicDetailsRoute from './routes/TopicDetailsRoute';
import TagsRoute from './routes/TagsRoute';
import SingleReplyCommentRoute from './routes/SingleReplyCommentRoute';
import TopicEditRoute from './routes/TopicEditRoute';
import AdminDashboardRoute from './routes/Dashboard/AdminDashboardRoute';
import RequireAdminUser from './components/Guard/RequireAuthAdmin';
import RefundRoute from './routes/Dashboard/Admin/RefundRoute';
import FlaggedCommentRoute from './routes/Dashboard/Admin/FlaggedCommentRoute';
import HomeRoute from './routes/HomeRoute';
import AppTestimonialRoute from './routes/Dashboard/User/AppTestimonialRoute';
import ContactRoute from './routes/ContactRoute';
import PrivacyPolicyRoute from './routes/PrivacyPolicyRoute';
import TermsRoute from './routes/TermsRoute';
import TransactionRoute from './routes/Dashboard/Admin/TransactionRoute';
import InvitationRoute from './routes/Settings/Teams/InvitationRoute';
import AddTeamMemberRoute from './routes/Settings/Teams/AddTeamMemberRoute';
import TeamMessageRoute from './routes/Settings/Teams/TeamMessageRoute';
import TeamPostRoute from './routes/Settings/Teams/TeamPostRoute';
import TeamMemberRoute from './routes/Settings/Teams/TeamMemberRoute';
import BannedUserRoute from './routes/Dashboard/Admin/BannedUserRoute';
import MFAuthenticationRoute from './routes/Dashboard/Admin/MFAuthenticationRoute';
import AdminAppTestimonialRoute from './routes/Dashboard/Admin/AdminAppTestimonialRoute';
import SuggestionRoute from './routes/Dashboard/Shared/SuggestionRoute';

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<RootLayout />}>
      <Route index element={<HomeRoute />} />
      <Route path="about" element={<AboutRoute />} />
      <Route path="contact" element={<ContactRoute />} />
      <Route path="privacy-policy" element={<PrivacyPolicyRoute />} />
      <Route path="terms-of-service" element={<TermsRoute />} />
      <Route
        path="github/success"
        element={
          <RequireAuth>
            <GitHubSuccessRoute />
          </RequireAuth>
        }
      />
      <Route
        path="explore/:filter"
        element={
          <RequireAuth>
            <ExploreRoute />
          </RequireAuth>
        }
      />
      <Route
        path="community/create-topic"
        element={
          <RequireAuth>
            <CreateCommunityTopicRoute />
          </RequireAuth>
        }
      />
      <Route
        path="comments/:commentId"
        element={
          <RequireAuth>
            <SingleReplyCommentRoute />
          </RequireAuth>
        }
      />
      <Route path="community/tags" element={<TagsRoute />} />
      <Route
        path="community/topics/edit/:topicId"
        element={
          <RequireAuth>
            <TopicEditRoute />
          </RequireAuth>
        }
      />
      <Route path="community/topics/:topicId" element={<TopicDetailsRoute />} />
      <Route
        path="/reviewer/repositories/:id"
        element={
          <RequireAuth>
            <RepositoryReviewRoute />
          </RequireAuth>
        }
      />
      <Route
        path="/admin/dashboard/:slug"
        element={
          <RequireAdminUser>
            <AdminDashboardRoute />
          </RequireAdminUser>
        }
      >
        <Route
          path="testimonials"
          element={
            <RequireAdminUser>
              <AdminAppTestimonialRoute />
            </RequireAdminUser>
          }
        />
        <Route
          path="banned-users"
          element={
            <RequireAdminUser>
              <BannedUserRoute />
            </RequireAdminUser>
          }
        />
        <Route
          path="multifactor-authentication"
          element={
            <RequireAdminUser>
              <MFAuthenticationRoute />
            </RequireAdminUser>
          }
        />
        <Route
          path="refunds"
          element={
            <RequireAdminUser>
              <RefundRoute />
            </RequireAdminUser>
          }
        />
        <Route
          path="flagged-comments"
          element={
            <RequireAdminUser>
              <FlaggedCommentRoute />
            </RequireAdminUser>
          }
        />
        <Route
          path="transactions"
          element={
            <RequireAdminUser>
              <TransactionRoute />
            </RequireAdminUser>
          }
        />
      </Route>
      <Route
        path="dashboard/:slug"
        element={
          <RequireAuth>
            <DashboardRoute />
          </RequireAuth>
        }
      >
        <Route
          path="reviewer/reviews"
          element={
            <RequireAuthReviewer>
              <ReviewerReviewsRoute />
            </RequireAuthReviewer>
          }
        />
        <Route
          path="reviewer/suggestions/create"
          element={
            <RequireAuthReviewer>
              <SuggestionRoute />
            </RequireAuthReviewer>
          }
        />
        <Route
          path="reviewer/stats"
          element={
            <RequireAuthReviewer>
              <ReviewerStatisticRoute />
            </RequireAuthReviewer>
          }
        />
        <Route
          path="reviewer/workspaces"
          element={
            <RequireAuthReviewer>
              <WorkSpaceContainerRoute />
            </RequireAuthReviewer>
          }
        >
          <Route
            path=":workspaceId"
            element={
              <RequireAuthReviewer>
                <WorkSpaceRoute />
              </RequireAuthReviewer>
            }
          />
        </Route>

        <Route
          path="user/add-review"
          element={
            <RequireAuthUser>
              <AddReviewRoute />
            </RequireAuthUser>
          }
        />
        <Route
          path="user/reviews"
          element={
            <RequireAuthUser>
              <ReviewsRoute />
            </RequireAuthUser>
          }
        />
        <Route
          path="user/suggestions/create"
          element={
            <RequireAuthUser>
              <SuggestionRoute />
            </RequireAuthUser>
          }
        />
        <Route
          path="user/testimonials"
          element={
            <RequireAuthUser>
              <AppTestimonialRoute />
            </RequireAuthUser>
          }
        />
        <Route
          path="user/reviews/feedback"
          element={
            <RequireAuthUser>
              <ReviewFeedbackRoute />
            </RequireAuthUser>
          }
        />
        <Route
          path="user/reviews/:repositoryId/edit"
          element={
            <RequireAuthUser>
              <EditRepositoryReviewRoute />
            </RequireAuthUser>
          }
        />
        <Route
          path="user/stats"
          element={
            <RequireAuthUser>
              <StatisticRoute />
            </RequireAuthUser>
          }
        />
        <Route
          path="user/guides"
          element={
            <RequireAuthUser>
              <GuideRoute />
            </RequireAuthUser>
          }
        />
      </Route>
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
      <Route
        path="profiles/:profileId"
        element={
          <RequireAuth>
            <ExploreProfileRoute />
          </RequireAuth>
        }
      />
      <Route
        path="/reviews/create"
        element={
          <RequireAuth>
            <CreateReviewRoute />
          </RequireAuth>
        }
      />
      <Route
        path="/reviews/:reviewId/edit"
        element={
          <RequireAuth>
            <EditReviewRoute />
          </RequireAuth>
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
        >
          <Route
            path="invitations"
            element={
              <RequireAuthReviewer>
                <InvitationRoute />
              </RequireAuthReviewer>
            }
          />
          <Route
            path=":teamId/add"
            element={
              <RequireAuthReviewer>
                <AddTeamMemberRoute />
              </RequireAuthReviewer>
            }
          />
          <Route
            path=":teamId/members"
            element={
              <RequireAuthReviewer>
                <TeamMemberRoute />
              </RequireAuthReviewer>
            }
          />
          <Route
            path=":teamId/messages"
            element={
              <RequireAuthReviewer>
                <TeamMessageRoute />
              </RequireAuthReviewer>
            }
          />
          <Route
            path=":teamId/posts"
            element={
              <RequireAuthReviewer>
                <TeamPostRoute />
              </RequireAuthReviewer>
            }
          />
        </Route>
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
        <Route
          path="testimonials"
          element={
            <RequireAuth>
              <TestimonialRoute />
            </RequireAuth>
          }
        />
      </Route>
      <Route path="*" element={<NotFoundRoute />} />
    </Route>
  )
);

const App = () => {
  const dispatch = useDispatch();
  const token = retrieveTokens()?.token;
  const { data } = useSyncUserQuery(token);

  useEffect(() => {
    if (data) {
      dispatch(updateUserAndTokens({ user: data, tokens: retrieveTokens() }));
    }
  }, [data, dispatch, token]);

  return <RouterProvider router={router}></RouterProvider>;
};

export default App;
