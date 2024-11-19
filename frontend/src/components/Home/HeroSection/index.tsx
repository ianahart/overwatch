import { Link } from 'react-router-dom';
import SectionContainer from '../SectionContainer';

const HeroSection = () => {
  return (
    <SectionContainer>
      <div className="text-gray-400 mt-10">
        <div className="flex justify-center">
          <h2 className="text-green-400 text-4xl font-bold font-display tracking-wider">OverWatch</h2>
        </div>
        <div className="flex md:flex-row flex-col justify-evenly">
          <div className="my-8 max-w-[600px] w-full">
            <h3 className="text-xl">Get Professional Code Reviews and Level Up Your Skills</h3>
            <p>
              Connect with top industry experts for in-depth code reviews and improve your projects with confidence.
            </p>
          </div>
          <div className="mt-10">
            <Link className="mx-2 btn" to="/signin">
              Get Started
            </Link>
            <Link className="mx-2 btn border !bg-transparent border-green-400 !text-green-400" to="/about">
              Learn More
            </Link>
          </div>
        </div>
        <div className="flex my-8 justify-center">
          <img
            className="max-w-[800px] w-full"
            src="https://res.cloudinary.com/dap5r6vfu/image/upload/v1732043898/overwatch/review_vb26og.png"
            alt="reviewer's dashboard code review"
          />
        </div>
      </div>
    </SectionContainer>
  );
};

export default HeroSection;
