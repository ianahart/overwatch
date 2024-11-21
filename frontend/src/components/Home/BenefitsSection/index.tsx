import BenefitsOne from '../../../assets/benefits-01.svg?react';
import BenefitsTwo from '../../../assets/benefits-02.svg?react';
import AnimatedContainer from '../AnimatedContainer';
import SectionContainer from '../SectionContainer';

const BenefitsSection = () => {
  return (
    <SectionContainer>
      <div className="my-4 flex justify-evenly md:flex-row flex-col">
        <AnimatedContainer side="left">
          <div>
            <BenefitsOne width={200} height={200} />
            <h3 className="text-xl text-green-400 font-bold">Users</h3>
            <ul className="list-disc">
              <li>Improve code quality and avoid costly bugs</li>
              <li>Gain valuable insights from experts in the field</li>
            </ul>
          </div>
        </AnimatedContainer>
        <AnimatedContainer side="right">
          <div>
            <BenefitsTwo width={200} height={200} />
            <h3 className="text-xl text-green-400 font-bold">Reviewers</h3>
            <ul className="list-disc">
              <li>Monetize your expertise by helping others</li>
              <li>Grow your personal brand as a trusted code reviewer</li>
            </ul>
          </div>
        </AnimatedContainer>
      </div>
    </SectionContainer>
  );
};

export default BenefitsSection;
