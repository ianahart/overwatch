import { useEffect, useState } from 'react';
import { useFetchPexelPhotosQuery, useLazyFetchPexelPhotosQuery } from '../../../../../../state/store';
import { retrieveTokens } from '../../../../../../util';
import Spinner from '../../../../../Shared/Spinner';

export interface ICardPhotosLibraryProps {
  updateCardPhoto: (photo: string) => void;
}

const CardPhotosLibrary = ({ updateCardPhoto }: ICardPhotosLibraryProps) => {
  const token = retrieveTokens()?.token;
  const [query, setQuery] = useState('');
  const [fetchPhotosByQuery] = useLazyFetchPexelPhotosQuery();
  const [photos, setPhotos] = useState<string[]>([]);
  const [feedbackMsg, setFeedbackMsg] = useState('');
  const { data, isLoading } = useFetchPexelPhotosQuery({ token }, { skip: !token });
  useEffect(() => {
    if (data !== undefined) {
      setPhotos(data.data);
    }
  }, [data]);

  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    setFeedbackMsg('');
    e.preventDefault();
    if (query.trim().length === 0) return;
    fetchPhotosByQuery({ token, query })
      .unwrap()
      .then((res) => {
        setQuery('');
        if (res.data.length === 0) {
          setFeedbackMsg(`No results for '${query}'`);
        } else {
          setPhotos(res.data);
        }
      })
      .catch(() => {
        console.log('Error fetching pexel photos by query');
      });
  };

  const handleOnClick = (photo: string) => {
    updateCardPhoto(photo);
  };

  return (
    <div className="p-2 rounded bg-gray-700 overflow-x-hidden absolute z-20 right-0 w-[200px] h-40 overflow-y-auto top-0">
      <p className="text-xs">Search for a photo</p>
      {feedbackMsg.length > 0 && <p className="text-xs">{feedbackMsg}</p>}
      <form onSubmit={handleOnSubmit}>
        <div className="my-1">
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Enter term..."
            className="border w-full text-sm h-8 rounded border-gray-500 bg-transparent"
          />
          <div className="flex justify-end my-2">
            <button type="submit" className="btn">
              Search
            </button>
          </div>
        </div>
      </form>
      <div className="my-2 flex justify-center">{isLoading && <Spinner message="Loading photos..." />}</div>
      <div className="my-2 flex flex-wrap w-[180px]">
        {photos.map((photo) => {
          return (
            <div
              data-testid="PexelPhoto"
              onClick={() => handleOnClick(photo)}
              className="h-9 w-9 rounded m-1 cursor-pointer"
              key={photo}
            >
              <img className="rounded h-full w-full" src={photo} alt={`pexel photo of ${query}`} />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default CardPhotosLibrary;
