import { screen, render } from '@testing-library/react';

import YourLanguages from '../../../../src/components/Settings/EditProfile/YourLangauges';
import { getLoggedInUser } from '../../../utils';
import { IProgrammingLanguage } from '../../../../src/interfaces';

describe('YourLangauges', () => {
  const renderComponent = () => {
    const programmingLanguages: IProgrammingLanguage[] = [
      { id: '1', name: 'JavaScript' },
      { id: '2', name: 'Java' },
      { id: '3', name: 'Python' },
    ];

    const { wrapper } = getLoggedInUser(
      {},
      {
        skills: {
          programmingLanguages,
        },
      }
    );

    render(<YourLanguages />, { wrapper });

    return {
      programmingLanguages,
    };
  };

  it('should render the heading', () => {
    renderComponent();
    expect(screen.getByRole('heading', { name: /your programming languages/i })).toBeInTheDocument();
  });

  it('should render the user programming languages', () => {
    const { programmingLanguages } = renderComponent();

    programmingLanguages.forEach((pl) => {
      expect(screen.getByText(pl.name)).toBeInTheDocument();
    });
  });
});
