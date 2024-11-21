import { MdOutlineKeyboardArrowLeft, MdOutlineKeyboardArrowRight } from 'react-icons/md';
import { animated, useSpring } from 'react-spring';
import SectionContainer from '../SectionContainer';
import { landingPageGalleryPhotos } from '../../../data';
import { useEffect, useRef, useState } from 'react';
import { IGalleryPhoto } from '../../../interfaces';

const GallerySection = () => {
  const [photos, _] = useState<IGalleryPhoto[]>(landingPageGalleryPhotos);
  const [currentPhotoIndex, setCurrentPhotoIndex] = useState(0);
  const intervalRef = useRef<NodeJS.Timeout | null>(null);
  const [direction, setDirection] = useState('next');

  const slideIn = useSpring({
    from: { transform: `translateX(${direction === 'next' ? '-100px' : '100px'})`, opacity: 0 },
    to: { transform: 'translateX(0)', opacity: 1 },
    config: { duration: 300 },
    reset: true,
  });

  useEffect(() => {
    intervalRef.current = setInterval(() => {
      if (direction !== 'next') {
        setDirection('next');
      }
      if (currentPhotoIndex < photos.length - 1) {
        setCurrentPhotoIndex((prevState) => prevState + 1);
      } else {
        setCurrentPhotoIndex(0);
      }
    }, 2500);
    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
    };
  }, [currentPhotoIndex, direction]);

  const clearExistingInterval = (): void => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
    }
  };

  const moveLeft = (): void => {
    clearExistingInterval();
    if (currentPhotoIndex > 0) {
      setCurrentPhotoIndex((prevState) => prevState - 1);
      setDirection('prev');
    } else {
      setCurrentPhotoIndex(photos.length - 1);
      setDirection('next');
    }
  };

  const moveRight = (): void => {
    clearExistingInterval();
    if (currentPhotoIndex < photos.length - 1) {
      setCurrentPhotoIndex((prevState) => prevState + 1);
      setDirection('next');
    } else {
      setCurrentPhotoIndex(0);
      setDirection('prev');
    }
  };

  return (
    <SectionContainer>
      <div className="flex justify-center mb-8">
        <h3 className="font-display text-green-400 tracking-wider text-2xl">Inside OverWatch</h3>
      </div>
      <div className="flex justify-center">
        <div className="p-2 border border-gray-800 rounded">
          <div className="flex justify-between overflow-hidden">
            <div onClick={moveLeft} className="flex flex-col self-center cursor-pointer">
              <MdOutlineKeyboardArrowLeft className="w-14 h-14 text-yellow-400 hover:text-yellow-200" />
            </div>
            <animated.div style={slideIn} className="max-w-[600px] w-full bg-gray-50">
              <img src={`${photos[currentPhotoIndex].src}`} alt={`${photos[currentPhotoIndex].title}`} />
            </animated.div>
            <div onClick={moveRight} className="flex flex-col self-center cursor-pointer">
              <MdOutlineKeyboardArrowRight className="w-14 h-14 text-yellow-400 hover:text-yellow-200" />
            </div>
          </div>
          <div className="my-2 text-center">
            <p className="text-gray-400">{photos[currentPhotoIndex].title}</p>
          </div>
          <div className="flex justify-center">
            {photos.map((_, index) => {
              return (
                <div className="mx-2">
                  <div
                    className={`border rounded-full ${
                      index === currentPhotoIndex ? 'border-yellow-400 h-5 w-5' : 'border-gray-800 h-4 w-4'
                    }`}
                  >
                    &nbsp;
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </SectionContainer>
  );
};

export default GallerySection;
