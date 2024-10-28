import { useSearchParams } from 'react-router-dom';
import TagsList from './TagsList';

const Tags = () => {
  const [searchParams] = useSearchParams();
  const tagParam = searchParams.get('tag');

  return (
    <div className="max-w-[1280px] mx-auto p-2 min-h-[100vh]">
      <div className="flex flex-col items-center justify-center">
        <h3 className="text-2xl text-gray-400">Tags under {tagParam}</h3>
      </div>
      <div className="my-8 flex flex-col items-center">{tagParam !== null && <TagsList query={tagParam} />}</div>
    </div>
  );
};

export default Tags;
