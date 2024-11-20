import AnimatedContainer from '../AnimatedContainer';
import SectionContainer from '../SectionContainer';
import Step from './Step';

const StepsSection = () => {
  return (
    <SectionContainer>
      <div className="my-8">
        <AnimatedContainer side="right">
          <Step
            stepNumber="1"
            stepTitle="Choose a Reviewer"
            stepDetails="Pick from a list of available experts based on experience and reviews."
          />
          <Step
            stepNumber="2"
            stepTitle="Submit Your Code"
            stepDetails="Create a request and provide the code you want reviewed."
          />
        </AnimatedContainer>
      </div>
      <div className="my-8">
        <AnimatedContainer side="left">
          <Step
            stepNumber="3"
            stepTitle="Receive Feedback"
            stepDetails="Get detailed actionable feedback on your code."
          />

          <Step
            stepNumber="4"
            stepTitle="Implement Suggestions"
            stepDetails="Improve your project based on expert recommendations."
          />
        </AnimatedContainer>
      </div>
    </SectionContainer>
  );
};

export default StepsSection;
