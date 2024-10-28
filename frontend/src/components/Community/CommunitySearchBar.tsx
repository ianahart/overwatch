import { useState } from 'react';
import { useLazySearchTopicsQuery } from '../../state/store';
import { ITopic } from '../../interfaces';
import ClickAway from '../Shared/ClickAway';
import CommunitySearchTopicList from './CommunitySearchTopicList';

export interface ICommunitySearchBarProps {
  btnText: string;
}

const CommunitySearchBar = ({ btnText }: ICommunitySearchBarProps) => {
  const [query, setQuery] = useState('');
  const [topics, setTopics] = useState<ITopic[]>([]);
  const [isOpen, setisOpen] = useState(false);
  const [fetchTopics] = useLazySearchTopicsQuery();
  const handleOnSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setisOpen(false);
    if (query.trim().length === 0) return;

    fetchTopics({ query })
      .unwrap()
      .then((res) => {
        if (res.data.length) {
          setisOpen(true);
        }
        setTopics(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleOnClickAway = () => {
    setisOpen(false);
  };

  return (
    <form onSubmit={handleOnSubmit}>
      <div className="flex items-center justify-center w-full">
        <div className="relative md:w-[60%] w-[100%]">
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search a topic..."
            className="border border-gray-800 bg-transparent h-9 rounded p-2 w-full"
          />
          {isOpen && (
            <div className="absolute z-10 bg-black border border-gray-800 rounded p-2 w-full">
              <ClickAway onClickAway={handleOnClickAway}>
                <CommunitySearchTopicList topics={topics} />
              </ClickAway>
            </div>
          )}
        </div>
        <button type="submit" className="btn md:ml-2">
          {btnText}
        </button>
      </div>
    </form>
  );
};

export default CommunitySearchBar;
