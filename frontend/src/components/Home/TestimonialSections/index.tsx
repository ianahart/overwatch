import { useFetchAppTestimonialsQuery } from '../../../state/store';
import SectionContainer from '../SectionContainer';
import Avatar from '../../Shared/Avatar';

const TestimonialSection = () => {
  const pageSize = 2;
  const { data } = useFetchAppTestimonialsQuery({ pageSize });

  return (
    <SectionContainer>
      <div className="my-4 flex justify-center">
        <h3 className="text-2xl font-display text-green-400 tracking-wider">Testimonials</h3>
      </div>
      <div className="my-4 flex justify-evenly flex-wrap">
        {data &&
          data.data.map((testimonial) => {
            return (
              <div key={testimonial.id}>
                <Avatar initials="?.?" avatarUrl={testimonial.avatarUrl} width="w-14" height="h-14" />
                <div className="my-1 max-w-[200px] w-full">
                  <p className="italic leading-7 text-gray-400">&quot;{testimonial.content}&quot;</p>
                  <div className="flex justify-end">
                    <div className="text-green-400">
                      <p className="font-bold">-{testimonial.firstName}</p>
                      <p>{testimonial.developerType}</p>
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
      </div>
    </SectionContainer>
  );
};

export default TestimonialSection;
