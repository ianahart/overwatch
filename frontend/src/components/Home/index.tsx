import BenefitsSection from './BenefitsSection';
import FooterSection from './FooterSection';
import GallerySection from './GallerySection';
import HeroSection from './HeroSection';
import KeyFeaturesSection from './KeyFeaturesSection';
import SignUpSection from './SignUpSection';
import StepsSection from './StepsSection';
import TestimonialSection from './TestimonialSections';

const Home = () => {
  return (
    <div className="overflow-hidden">
      <HeroSection />
      <KeyFeaturesSection />
      <StepsSection />
      <BenefitsSection />
      <GallerySection />
      <TestimonialSection />
      <SignUpSection />
      <FooterSection />
    </div>
  );
};

export default Home;
