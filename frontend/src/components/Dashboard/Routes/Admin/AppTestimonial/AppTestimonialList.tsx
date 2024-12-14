import { useSelector } from 'react-redux';
import {
  TRootState,
  useLazyFetchAdminAppTestimonialsQuery,
  useUpdateAdminAppTestimonialMutation,
} from '../../../../../state/store';
import { useEffect, useState } from 'react';
import { IAdminAppTestimonial, IPaginationState } from '../../../../../interfaces';
import AppTestimonialListItem from './AppTestimonialListItem';

const AppTestimonialList = () => {
  const paginationState = {
    page: -1,
    pageSize: 5,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };
  const { token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [adminAppTestimonials, setAdminAppTestimonials] = useState<IAdminAppTestimonial[]>([]);
  const [fetchAdminAppTestimonials] = useLazyFetchAdminAppTestimonialsQuery();
  const [updateAdminAppTestimonial] = useUpdateAdminAppTestimonialMutation();

  useEffect(() => {
    paginateAdminAppTestimonials('next');
  }, []);

  const paginateAdminAppTestimonials = (dir: string) => {
    fetchAdminAppTestimonials({ token, page: pag.page, direction: dir, pageSize: pag.pageSize })
      .unwrap()
      .then((res) => {
        const { items, page, pageSize, totalPages, direction, totalElements } = res.data;
        setAdminAppTestimonials(items);
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          totalPages,
          totalElements,
          direction,
        }));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const selectAdminAppTestimonial = (id: number, isSelected: boolean): void => {
    updateAdminAppTestimonial({ id, isSelected, token })
      .unwrap()
      .then((res) => {
        setAdminAppTestimonials(
          adminAppTestimonials.map((adminAppTestimonial) => {
            return id === adminAppTestimonial.id
              ? { ...adminAppTestimonial, isSelected: res.data }
              : { ...adminAppTestimonial };
          })
        );
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div>
      <div className="my-8">
        <ul>
          {adminAppTestimonials.map((adminAppTestimonial) => {
            return (
              <AppTestimonialListItem
                key={adminAppTestimonial.id}
                adminAppTestimonial={adminAppTestimonial}
                selectAdminAppTestimonial={selectAdminAppTestimonial}
              />
            );
          })}
        </ul>
      </div>
      <div className="flex items-center justify-center my-8">
        {pag.page > 0 && (
          <button onClick={() => paginateAdminAppTestimonials('prev')} className="mx-1">
            Prev
          </button>
        )}
        <p className="text-green-400">
          {pag.page + 1} of {pag.totalPages}
        </p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateAdminAppTestimonials('next')} className="mx-1">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default AppTestimonialList;
