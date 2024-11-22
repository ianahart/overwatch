import SectionContainer from '../SectionContainer';
import logo from '../../../assets/overwatch_logo.png';
import { Link } from 'react-router-dom';

const SignUpSection = () => {
  return (
    <SectionContainer>
      <div>
        <div className="flex flex-col items-center">
          <div className="text-gray-400">
            <p className="text-lg">Ready to improve your code? Start your first review today!</p>
          </div>
          <div className="my-2">
            <img className="h-12 w-12 rounded-lg" src={logo} alt="overwatch logo" />
          </div>
          <div className="my-2">
            <Link to="/signup" className="btn">
              Sign Up Now
            </Link>
          </div>
        </div>
      </div>
    </SectionContainer>
  );
};

export default SignUpSection;
