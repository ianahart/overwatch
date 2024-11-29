import { Light as SyntaxHighlighter } from 'react-syntax-highlighter';
import dayjs from 'dayjs';
import { tomorrowNightBright } from 'react-syntax-highlighter/dist/esm/styles/hljs';
import Avatar from '../../Shared/Avatar';
import { ITeamPost } from '../../../interfaces';

export interface ITeamPostItemProps {
  teamPost: ITeamPost;
}

// Function to decode HTML entities
const decodeHtmlEntities = (input: string): string => {
  const doc = new DOMParser().parseFromString(input, 'text/html');
  return doc.documentElement.textContent || '';
};

const TeamPostItem = ({ teamPost }: ITeamPostItemProps) => {
  const decodedCode = decodeHtmlEntities(teamPost.code);
  return (
    <div className="my-6">
      <div className="flex items-center">
        <Avatar initials="?.?" avatarUrl={teamPost.avatarUrl} height="h-9" width="w-9" />
        <div className="mb-2 ml-1">
          <h3>{teamPost.fullName}</h3>
          <p className="text-xs">{dayjs(teamPost.createdAt).format('MM/DD/YYYY')}</p>
        </div>
      </div>
      <SyntaxHighlighter
        language={teamPost.language}
        style={tomorrowNightBright}
        customStyle={{
          backgroundColor: '#2e2e2e',
          borderRadius: '8px',
          padding: '16px',
          overflowX: 'auto',
          fontFamily: 'monospace',
          fontSize: '14px',
        }}
      >
        {decodedCode}
      </SyntaxHighlighter>
    </div>
  );
};

export default TeamPostItem;
