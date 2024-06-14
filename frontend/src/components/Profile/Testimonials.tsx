import { useSelector } from 'react-redux';
import { TRootState, useFetchTopTestimonialsQuery } from '../../state/store';
import { useEffect, useState } from 'react';
import { ITestimonial } from '../../interfaces';
import Spinner from '../Shared/Spinner';

export interface ITestimonialsProps {
  userId: number;
}

const Testimonials = ({ userId }: ITestimonialsProps) => {
  const { token } = useSelector((store: TRootState) => store.user);
  const { data, isLoading } = useFetchTopTestimonialsQuery({ token, userId });
  const [items, setItems] = useState<ITestimonial[]>([]);

  useEffect(() => {
    if (data) {
      setItems(data.data);
    }
  }, [data]);

  return (
    <div className="p-4 border-b border-gray-800">
      <h3 className="text-gray-400 text-lg">Testimonials</h3>
      {isLoading && (
        <div className="flex justify-center my-4">
          <Spinner message="Fetching testimonials..." />
        </div>
      )}
      <div className="my-4">
        <ul className="p-0 m-0">
          {items.length > 0 &&
            items.map((item) => {
              return (
                <li key={item.id} className="my-4">
                  <p className="text-gray-400">{item.name}</p>
                  <p className="text-sm">&ldquo;{item.text}&rdquo;</p>
                </li>
              );
            })}
        </ul>
      </div>
    </div>
  );
};

export default Testimonials;
