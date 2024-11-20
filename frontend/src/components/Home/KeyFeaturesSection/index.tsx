/// <reference types="vite-plugin-svgr/client" />
import KeyFeatureSvgOne from '../../../assets/key-features-01.svg?react';
import KeyFeatureSvgTwo from '../../../assets/key-features-02.svg?react';
import KeyFeatureSvgThree from '../../../assets/key-features-03.svg?react';
import KeyFeatureSvgFour from '../../../assets/key-features-04.svg?react';
import SectionContainer from '../SectionContainer';
import AnimatedContainer from '../AnimatedContainer';

const KeyFeaturesSection = () => {
  return (
    <SectionContainer>
      <div className="text-gray-400">
        <AnimatedContainer side="left">
          <div className="max-w-[290px]">
            <h3 className="text-xl">Expert Reviewers</h3>
            <p>Get feedback from experienced professionals across various tech stacks.</p>
            <KeyFeatureSvgOne width={150} height={150} />
          </div>
          <div className="max-w-[290px]">
            <h3 className="text-xl">Quick Turnaround</h3>
            <p>Receive detailed feedbacks within hours</p>
            <KeyFeatureSvgTwo width={150} height={150} />
          </div>
        </AnimatedContainer>
        <AnimatedContainer side="right">
          <div className="max-w-[290px]">
            <h3 className="text-xl">Comprehensive Reviews</h3>
            <p>Bug fixes, feature suggestions, and code optimizations tailored to your needs.</p>
            <KeyFeatureSvgThree width={150} height={150} />
          </div>
          <div className="max-w-[290px]">
            <h3 className="text-xl">Secure Payments</h3>
            <p>Payments are handled securely via Stripe.</p>
            <KeyFeatureSvgFour width={150} height={150} />
          </div>
        </AnimatedContainer>
      </div>
    </SectionContainer>
  );
};

export default KeyFeaturesSection;
