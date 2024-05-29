import { useDispatch } from 'react-redux';
import { useState } from 'react';
import { AiOutlineCheck } from 'react-icons/ai';
import { IPackage } from '../../../interfaces';
import {
  removePackageDesc,
  removePackageItem,
  updatePackageItem,
  updatePackageDesc,
  addPackageItem,
  updatePackagePrice,
} from '../../../state/store';

export interface IPackageProps {
  data: IPackage;
  name: string;
  title: string;
}

const Package = ({ data, name, title }: IPackageProps) => {
  const [item, setItem] = useState('');
  const dispatch = useDispatch();

  const handleOnDescChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value, name } = e.target;
    dispatch(updatePackageDesc({ name, value }));
  };

  const handleOnPriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    dispatch(updatePackagePrice({ name, value }));
  };

  const handleOnItemChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setItem(value);
  };

  const handleOnAddItem = () => {
    if (data.items.length >= 10 || item.trim().length === 0) {
      return;
    }
    dispatch(addPackageItem({ name, value: item }));
    setItem('');
  };

  const handleUpdatePackageItemEditing = (existingItem: { id: string; name: string; isEditing: number }) => {
    dispatch(updatePackageItem({ ...existingItem, pckg: name }));
  };

  const handleUpdatePackageItemValue = (
    e: React.ChangeEvent<HTMLInputElement>,
    existingItem: { id: string; name: string; isEditing: number }
  ) => {
    dispatch(updatePackageItem({ ...existingItem, name: e.target.value, pckg: name }));
  };

  const handleRemovePackageItem = (id: string) => {
    dispatch(removePackageItem({ id, pckg: name }));
  };

  const handleRemovePackageDesc = (pckg: string) => {
    dispatch(removePackageDesc({ pckg }));
  };

  return (
    <div className="my-8">
      <h3 className="text-xl my-2 text-gray-400">{title}</h3>
      <div className="my-4">
        <label className="pb-1 block" htmlFor={name}>
          Description
        </label>
        <input
          value={data.description}
          onChange={handleOnDescChange}
          id={name}
          name={name}
          className="h-9 rounded bg-transparent border border-gray-800 w-full placeholder:text-gray-500 placeholder:pl-2 pl-2 shadow"
        />
      </div>
      <div className="my-4">
        <label className="pb-1 block" htmlFor={`${name}price`}>
          Price
        </label>
        <input
          value={data.price}
          onChange={handleOnPriceChange}
          id={`${name}price`}
          name={`${name}price`}
          className="h-9 rounded bg-transparent border border-gray-800 md:w-[40%] w-full placeholder:text-gray-500 placeholder:pl-2 pl-2 shadow"
        />
      </div>
      <div className="my-4 flex items-center">
        <div className="flex flex-col md:w-[50%] w-full">
          <label>Included services</label>
          <div className="flex items-center">
            <input
              onChange={handleOnItemChange}
              value={item}
              className="h-9 rounded bg-transparent border border-gray-800 w-full placeholder:text-gray-500 placeholder:pl-2 pl-2 shadow"
            />
            <div className="ml-4">
              <button onClick={handleOnAddItem} type="button" className="btn">
                Add
              </button>
            </div>
          </div>
        </div>
      </div>
      {data.items.length > 0 && (
        <div className="my-20">
          <h3>Services:</h3>
          <div className="flex items-center justify-between">
            <p>{data.description}</p>
            <button
              onClick={() => handleRemovePackageDesc(name)}
              className="outline-btn border border-gray-800 !text-gray-400 mx-2"
            >
              Remove
            </button>
          </div>
          <div className="my-2">
            <p className="text-xl">{data.price}</p>
          </div>
          <ul>
            {data.items.map(({ id, name: value, isEditing }) => {
              return (
                <li key={id} className="my-1">
                  <div className="flex justify-between">
                    <div className="flex items-center">
                      <AiOutlineCheck className="mr-1 text-green-400" />
                      {isEditing ? (
                        <input
                          onChange={(e) => handleUpdatePackageItemValue(e, { id, name: value, isEditing })}
                          value={value}
                          className={`h-9 rounded bg-transparent border ${
                            isEditing ? 'border-green-400' : 'border-gray-800'
                          } w-full placeholder:text-gray-500 placeholder:pl-2 pl-2 shadow`}
                        />
                      ) : (
                        <p>{value}</p>
                      )}
                    </div>
                    <div>
                      <button
                        type="button"
                        onClick={() =>
                          handleUpdatePackageItemEditing({ id, name: value, isEditing: isEditing ? 0 : 1 })
                        }
                        className="outline-btn border border-gray-800 !text-gray-400 mx-2"
                      >
                        {isEditing ? 'Save' : 'Edit'}
                      </button>

                      <button
                        type="button"
                        onClick={() => handleRemovePackageItem(id)}
                        className="outline-btn border border-gray-800 !text-gray-400 mx-2"
                      >
                        Remove
                      </button>
                    </div>
                  </div>
                </li>
              );
            })}
          </ul>
        </div>
      )}
    </div>
  );
};

export default Package;
