import AppTestimonialHeader from './AppTestimonialHeader';
import AppTestimonialList from './AppTestimonialList';

const AppTestimonial = () => {
  return (
    <>
      <div className="md:max-w-[1450px] w-full mx-auto mt-8">
        <div className="p2">
          <AppTestimonialHeader />
          <AppTestimonialList />
        </div>
      </div>
    </>
  );
};

export default AppTestimonial;
