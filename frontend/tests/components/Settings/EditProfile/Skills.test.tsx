import { screen, render } from '@testing-library/react';

import Skills from '../../../../src/components/Settings/EditProfile/Skills';
import { getLoggedInUser } from '../../../utils';

describe('Skills', () => {
  const renderComponent = () => {
    const skills = {
      qualifications: [{ id: '1', name: 'Computer Science Degree' }],
      languages: [{ id: '1', name: 'English' }],
      programmingLanguages: [{ id: '1', name: 'JavaScript' }],
    };

    const { wrapper } = getLoggedInUser(
      {},
      {
        skills,
      }
    );

    render(<Skills />, { wrapper });

    return {
      skills,
    };
  };

  it('should render the heading', () => {
    renderComponent();
    expect(screen.getByText(/skills & expertise/i)).toBeInTheDocument();
  });

  it('should render all three skill sections', () => {
    const { skills } = renderComponent();

    expect(screen.getByLabelText(/languages spoken/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/fluent programming languages/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/your qualifications/i)).toBeInTheDocument();

    expect(screen.getByText(skills.languages[0].name)).toBeInTheDocument();
    expect(screen.getByText(skills.qualifications[0].name)).toBeInTheDocument();
    expect(screen.getByText(skills.programmingLanguages[0].name)).toBeInTheDocument();
  });
});
