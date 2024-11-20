import { useEffect, useRef, useState } from 'react';
import { useSpring, animated } from 'react-spring';

export interface IKeyFeatureContainerProps {
  children: React.ReactNode;
  side: string;
}

const KeyFeatureContainer = ({ children, side }: IKeyFeatureContainerProps) => {
  const [isVisible, setIsVisible] = useState(false);
  const divRef = useRef<HTMLDivElement | null>(null);
  const isVisibleProperties = side === 'right' ? 'translate3d(-25px, 100px, 0)' : 'translate3d(25px,-100px, 0)';

  const springs = useSpring({
    opacity: isVisible ? 1 : 0,
    transform: isVisible ? 'translate3d(0,0,0)' : isVisibleProperties,
    config: { duration: 500 },
  });

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        console.log(entry);
        if (entry.isIntersecting) {
          console.log('run');
          setIsVisible(true);
          observer.disconnect();
        }
      },
      { threshold: 0.9 }
    );

    if (divRef.current) {
      observer.observe(divRef.current);
    }

    return () => observer.disconnect();
  }, []);

  return (
    <animated.div
      style={springs}
      ref={divRef}
      className="flex md:flex-row flex-col md:justify-evenly items-center mb-12"
    >
      {children}
    </animated.div>
  );
};

export default KeyFeatureContainer;
