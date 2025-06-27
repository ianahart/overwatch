import { useEffect, useState } from 'react';
import dayjs from 'dayjs';
import { useSelector } from 'react-redux';

import { paginationState } from '../../../data';
import { IPaginationState, ITestimonial } from '../../../interfaces';
import {
  TRootState,
  useDeleteTestimonialMutation,
  useFetchTestimonialsQuery,
  useLazyFetchTestimonialsQuery,
} from '../../../state/store';
import Spinner from '../../Shared/Spinner';

const Testimonials = () => {
  const { user, token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [testimonials, setTestimonials] = useState<ITestimonial[]>([]);
  const [deleteTestimonial] = useDeleteTestimonialMutation();
  const [fetchTestimonials] = useLazyFetchTestimonialsQuery();
  const { data, isLoading } = useFetchTestimonialsQuery(
    {
      userId: user.id,
      token,
      page: -1,
      pageSize: 2,
      direction: 'next',
    },
    { skip: !user.id || !token }
  );

  useEffect(() => {
    if (data !== undefined) {
      const { items, page, pageSize, totalPages, direction, totalElements } = data.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setTestimonials(items);
    }
  }, [data]);

  const paginateTestimonials = async (dir: string) => {
    try {
      const response = await fetchTestimonials({
        userId: user.id,
        token,
        page: pag.page,
        pageSize: pag.pageSize,
        direction: dir,
      }).unwrap();

      const { items, page, pageSize, totalPages, direction, totalElements } = response.data;
      setPag((prevState) => ({
        ...prevState,
        page,
        pageSize,
        totalElements,
        totalPages,
        direction,
      }));
      setTestimonials(items);
    } catch (err) {
      console.log(err);
    }
  };

  const handleOnDelete = async (id: number) => {
    try {
      await deleteTestimonial({ token, id });
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="my-4">
      <div className="flex justify-center">{isLoading && <Spinner message="Loading testimonials..." />}</div>
      {testimonials.map((testimonial) => {
        return (
          <div
            data-testid="setting-testimonial-item"
            key={testimonial.id}
            className="my-4 border border-gray-800 p-4 rounded"
          >
            <div className="flex justify-between items-center">
              <h3 className="text-lg">{testimonial.name}</h3>
              <p className="text-sm italic">{dayjs(testimonial.createdAt).format('MM/DD/YYYY')}</p>
            </div>
            <p>{testimonial.text}</p>
            <div className="my-4 flex justify-end">
              <button
                onClick={() => handleOnDelete(testimonial.id)}
                className="outline-btn border !text-gray-400 border-gray-800 rounded"
              >
                Delete
              </button>
            </div>
          </div>
        );
      })}
      <div className="flex items-center justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateTestimonials('prev')} className="mx-1">
            Prev
          </button>
        )}
        <p className="text-green-400">
          {pag.page + 1} of {pag.totalPages}
        </p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateTestimonials('next')} className="mx-1">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default Testimonials;
