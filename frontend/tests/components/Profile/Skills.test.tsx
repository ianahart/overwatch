import { screen, render } from '@testing-library/react';

import Skills from '../../../src/components/Profile/Skills';
import { db } from '../../mocks/db';
import { AllProviders } from '../../AllProviders';
import { toPlainObject } from 'lodash';

export interface ISkill {
  id: number;
  name: string;
}

describe('Skills', () => {
  const getProps = () => {
    const profileEntity = db.fullProfile.create();
    const { languages, programmingLanguages, qualifications } = profileEntity.skills;

    if (!languages || !programmingLanguages || !qualifications) {
      throw new Error('Missing languages, programmingLanguages, or qualifications');
    }

    return {
      languages: languages.map((language) => toPlainObject(language)),
      programmingLanguages: programmingLanguages.map((programmingLanguage) => toPlainObject(programmingLanguage)),
      qualifications: qualifications.map((qualification) => toPlainObject(qualification)),
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<Skills {...props} />, { wrapper: AllProviders });

    return {
      props,
      getHeading: () => screen.getByRole('heading', { level: 3, name: /skills/i }),
    };
  };

  it('should render the section heading', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });

  it('should render all skill titles', () => {
    renderComponent();

    expect(screen.getByText('Languages')).toBeInTheDocument();
    expect(screen.getByText('Programming Languages')).toBeInTheDocument();
    expect(screen.getByText('Education')).toBeInTheDocument();
  });

  it('should render items from each skill group', () => {
    const { props } = renderComponent();

    const allSkills: ISkill[] = [...props.languages, ...props.programmingLanguages, ...props.qualifications];

    allSkills.forEach(({ name }) => expect(name).toBeInTheDocument());
  });
});
