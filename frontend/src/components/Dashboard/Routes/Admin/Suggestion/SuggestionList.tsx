import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import dayjs from 'dayjs';

import { IPaginationState, ISuggestion } from '../../../../../interfaces';
import { TRootState, useLazyFetchSuggestionsQuery } from '../../../../../state/store';
import Avatar from '../../../../Shared/Avatar';
import Screenshot from './Screenshot';
import Description from './Description';
import SuggestionTable from './SuggestionTable';
import Actions from './Actions';

export interface IPriorityMapping {
  [key: string]: string;
  LOW: string;
  MEDIUM: string;
  HIGH: string;
}

const SuggestionList = () => {
  const paginationState = {
    page: 0,
    pageSize: 10,
    totalPages: 0,
    direction: 'next',
    totalElements: 0,
  };

  const { token } = useSelector((store: TRootState) => store.user);
  const [pag, setPag] = useState<IPaginationState>(paginationState);
  const [suggestions, setSuggestions] = useState<ISuggestion[]>([]);
  const [fetchSuggestions] = useLazyFetchSuggestionsQuery();
  const [query, setQuery] = useState('PENDING');

  useEffect(() => {
    paginateSuggestions('next', true);
  }, [token, query]);

  const paginateSuggestions = (dir: string, initial = false): void => {
    const payload = {
      token,
      page: initial ? -1 : pag.page,
      pageSize: pag.pageSize,
      direction: dir,
      feedbackStatus: query,
    };

    fetchSuggestions(payload)
      .unwrap()
      .then((res) => {
        const { direction, items, page, pageSize, totalElements, totalPages } = res.data;
        setPag((prevState) => ({
          ...prevState,
          page,
          pageSize,
          direction,
          totalPages,
          totalElements,
        }));
        setSuggestions(items);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const headerMapping = {
    id: 'ID',
    createdAt: 'Submitted On',
    title: 'Title',
    description: 'Description',
    contact: 'Contact',
    fileUrl: 'File',
    feedbackType: 'Feedback Type',
    priorityLevel: 'Priority Level',
    feedbackStatus: 'Feedback Status',
    fullName: 'Full Name',
    avatarUrl: 'Profile Picture',
  };

  const priorityMapping: IPriorityMapping = {
    LOW: 'bg-gray-400',
    MEDIUM: 'bg-yellow-400',
    HIGH: 'bg-red-300',
  };

  const feedbackMapping = {
    FEATURE_REQUEST: { value: 'Feature Request', background: 'bg-blue-400' },
    BUG_REPORT: { value: 'Bug Report', background: 'bg-red-200' },
    GENERAL_FEEDBACK: { value: 'General Feedback', background: 'bg-green-200' },
    SUGGESTION: { value: 'Suggestion', background: 'bg-yellow-200' },
  } as const;

  type FeedbackType = keyof typeof feedbackMapping;

  const isFeedbackType = (value: unknown): value is FeedbackType => {
    return typeof value === 'string' && value in feedbackMapping;
  };

  const handleUpdateSuggestion = (feedbackStatus: string, id: string): void => {
    setSuggestions(
      suggestions.map((suggestion) => {
        if (suggestion.id === id) {
          return { ...suggestion, feedbackStatus };
        } else {
          return { ...suggestion };
        }
      })
    );
  };

  const handleDeleteSuggestion = (id: string): void => {
    setSuggestions(suggestions.filter((suggestion) => suggestion.id !== id));
  };

  const formatColumnData = (column: string, value: string, id: string): string | JSX.Element => {
    switch (column) {
      case 'feedbackStatus':
        return (
          <Actions
            id={id}
            value={value}
            handleUpdateSuggestion={handleUpdateSuggestion}
            handleDeleteSuggestion={handleDeleteSuggestion}
          />
        );
      case 'priorityLevel':
        return (
          <p className={`${priorityMapping[value]} text-center py-1 px-2 text-black text-xs font-bold rounded`}>
            {value}
          </p>
        );
      case 'createdAt':
        return dayjs(value).format('MM/D/YYYY hh:mm:ss a');
      case 'description':
        return <Description value={value} />;
      case 'fileUrl':
        if (value === null) {
          return '';
        }
        return <Screenshot value={value} />;

      case 'feedbackType':
        if (isFeedbackType(value)) {
          const { background, value: text } = feedbackMapping[value];
          return <p className={`py-1 text-center text-xs font-bold px-2 text-black rounded ${background}`}>{text}</p>;
        }
        return value;
      case 'avatarUrl':
        return <Avatar width="w-12" height="h-12" initials="?.?" avatarUrl={value} />;
      default:
        return value;
    }
  };

  return (
    <div data-testid="SuggestionList" className="my-8 p-2">
      <form>
        <select
          className="bg-transparent border border-gray-800 h-9"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        >
          <option value="UNDER_REVIEW">Under Review</option>
          <option value="IMPLEMENTED">Implemented</option>
          <option value="REJECTED">Rejected</option>
          <option value="PENDING">Pending</option>
        </select>
      </form>
      <div className="my-6 overflow-x-auto">
        <SuggestionTable headerMapping={headerMapping} suggestions={suggestions} formatColumnData={formatColumnData} />
      </div>
      <div className="flex items-center text-gray-400 justify-center">
        {pag.page > 0 && (
          <button onClick={() => paginateSuggestions('prev')} className="mx-2">
            Prev
          </button>
        )}
        <p className="mx-2">{pag.page + 1}</p>
        {pag.page < pag.totalPages - 1 && (
          <button onClick={() => paginateSuggestions('next')} className="mx-2">
            Next
          </button>
        )}
      </div>
    </div>
  );
};

export default SuggestionList;
