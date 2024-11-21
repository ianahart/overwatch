import BenefitsSection from './BenefitsSection';
import GallerySection from './GallerySection';
import HeroSection from './HeroSection';
import KeyFeaturesSection from './KeyFeaturesSection';
import StepsSection from './StepsSection';

const Home = () => {
  return (
    <div className="overflow-hidden">
      <HeroSection />
      <KeyFeaturesSection />
      <StepsSection />
      <BenefitsSection />
      <GallerySection />
    </div>
  );
};

export default Home;
