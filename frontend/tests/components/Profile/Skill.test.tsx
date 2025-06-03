import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import Skill from '../../../src/components/Profile/Skill';
import { db } from '../../mocks/db';
import { AllProviders } from '../../AllProviders';

describe('Skill', () => {
  const getProps = () => {
    const profileEntity = db.fullProfile.create();

    const programmingLanguageSkills = profileEntity.skills.programmingLanguages;

    if (!programmingLanguageSkills) {
      throw new Error('Missing programmingLanguageSkills');
    }

    const data = programmingLanguageSkills.map((pls) => toPlainObject(pls));

    return {
      data,
      title: 'Programming Languages',
    };
  };
  const renderComponent = () => {
    const props = getProps();

    render(<Skill {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render the title correctly', () => {
    const { props } = renderComponent();

    const headingTitle = screen.getByRole('heading', { level: 3 });
    expect(headingTitle).toBeInTheDocument();
    expect(headingTitle).toHaveTextContent(props.title);
  });

  it('should render the programming language skills correctly', () => {
    const { props } = renderComponent();

    props.data.forEach(({ name }) => {
      expect(name).toBeInTheDocument();
    });
  });
});
