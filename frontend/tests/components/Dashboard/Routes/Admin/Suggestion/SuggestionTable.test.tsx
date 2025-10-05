import { screen, render } from '@testing-library/react';
import SuggestionTable from '../../../../../../src/components/Dashboard/Routes/Admin/Suggestion/SuggestionTable';
import { AllProviders } from '../../../../../AllProviders';
import { ISuggestion } from '../../../../../../src/interfaces';
import { createSuggestions } from '../../../../../mocks/dbActions';

describe('SuggestionTable', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const getProps = () => {
    const suggestions: ISuggestion[] = createSuggestions(2);
    const formatColumnData = vi.fn();
    const headerMapping: Record<keyof ISuggestion, string> = {
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

    return { suggestions, formatColumnData, headerMapping };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<SuggestionTable {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render the table headers correctly', () => {
    const { props } = renderComponent();

    const { headerMapping } = props;

    Object.values(headerMapping).forEach((heading) => {
      expect(screen.getByText(heading)).toBeInTheDocument();
    });
  });

  it('should call formatColumnData for every cell with correct parameters', () => {
    const { props } = renderComponent();
    const { formatColumnData, headerMapping, suggestions } = props;

    expect(formatColumnData).toHaveBeenCalledTimes(Object.keys(headerMapping).length * 2);

    suggestions.forEach((suggestion) => {
      Object.keys(headerMapping).forEach((column) => {
        expect(formatColumnData).toHaveBeenCalledWith(column, suggestion[column], suggestion.id);
      });
    });
  });
});
