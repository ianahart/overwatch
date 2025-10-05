import { nanoid } from 'nanoid';
import TableHeader from './TableHeader';
import { ISuggestion } from '../../../../../interfaces';

export interface ISuggestionTableProps {
  suggestions: ISuggestion[];
  headerMapping: Record<keyof ISuggestion, string>;
  formatColumnData: (column: string, value: string, id: string) => string | JSX.Element;
}

const SuggestionTable = ({ suggestions, headerMapping, formatColumnData }: ISuggestionTableProps) => {
  return (
    <table className="border-collapse w-full rounded text-gray-400">
      <thead>
        <tr>
          {Object.entries(headerMapping).map(([key, val]) => {
            return <TableHeader key={key} heading={val} />;
          })}
        </tr>
      </thead>
      <tbody>
        {suggestions.map((row) => {
          return (
            <tr key={row.id} data-testid={`suggestion-row-${row.id}`}>
              {Object.keys(headerMapping).map((column) => {
                return (
                  <td data-testid={`suggestion-cell-${row.id}-${column}`} className="text-sm" key={nanoid()}>
                    {formatColumnData(column, row[column], row.id)}
                  </td>
                );
              })}
            </tr>
          );
        })}
      </tbody>
    </table>
  );
};

export default SuggestionTable;
