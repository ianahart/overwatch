import { FaGithub } from 'react-icons/fa';
import { useSelector } from 'react-redux';
import { TRootState } from '../../../../../state/store';

const GitHubLogin = () => {
  const { selectedReviewer } = useSelector((store: TRootState) => store.addReview);
  const handleSignInWithGitHub = () => {
    if (selectedReviewer.id === 0) return;
    const gitHubClientId = import.meta.env.VITE_GITHUB_CLIENT_ID;
    const githubLoginUrl = `https://github.com/login/oauth/authorize?client_id=${gitHubClientId}`;
    window.location.assign(githubLoginUrl);
  };

  return (
    <div className="text-gray-400 md:w-[250px] w-full">
      <p>Please sign in with GitHub so we can access your repositories.</p>
      <div className="my-4">
        <button onClick={handleSignInWithGitHub} className="flex items-center border-gray-800 border p-2 rounded">
          <FaGithub className="mr-2" />
          Sign in with GitHub
        </button>
      </div>
    </div>
  );
};

export default GitHubLogin;
