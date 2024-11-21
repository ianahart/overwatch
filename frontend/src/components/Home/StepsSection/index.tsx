import HowItWorksOne from '../../../assets/how-it-works-01.svg?react';
import HowItWorksTwo from '../../../assets/how-it-works-02.svg?react';
import HowItWorksThree from '../../../assets/how-it-works-03.svg?react';
import HowItWorksFour from '../../../assets/how-it-works-04.svg?react';

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
            Icon={HowItWorksOne}
          />
          <Step
            stepNumber="2"
            stepTitle="Submit Your Code"
            stepDetails="Create a request and provide the code you want reviewed."
            Icon={HowItWorksTwo}
          />
        </AnimatedContainer>
      </div>
      <div className="my-8">
        <AnimatedContainer side="left">
          <Step
            stepNumber="3"
            stepTitle="Receive Feedback"
            stepDetails="Get detailed actionable feedback on your code."
            Icon={HowItWorksThree}
          />

          <Step
            stepNumber="4"
            stepTitle="Implement Suggestions"
            stepDetails="Improve your project based on expert recommendations."
            Icon={HowItWorksFour}
          />
        </AnimatedContainer>
      </div>
    </SectionContainer>
  );
};

export default StepsSection;
