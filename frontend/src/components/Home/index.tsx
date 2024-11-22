import BenefitsSection from './BenefitsSection';
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
    </div>
  );
};

export default Home;
