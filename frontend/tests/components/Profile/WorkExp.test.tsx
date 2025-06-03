import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import WorkExp from '../../../src/components/Profile/WorkExp';
import { db } from '../../mocks/db';
import { IWorkExperience } from '../../../src/interfaces';
import { AllProviders } from '../../AllProviders';

describe('WorkExp', () => {
  const getProps = () => {
    const workExps: IWorkExperience[] = [toPlainObject(db.workExperience.create())];

    return {
      workExps,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<WorkExp {...props} />, { wrapper: AllProviders });

    return {
      props,
      getHeading: () => screen.getByRole('heading', { level: 3, name: /work experience/i }),
    };
  };
  it('should render the heading correctly', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });

  it('should render the work experience correctly', () => {
    const { props } = renderComponent();

    const [workExps] = props.workExps;

    expect(screen.getByRole('heading', { name: workExps.title })).toBeInTheDocument();
    expect(screen.getByText(workExps.desc)).toBeInTheDocument();
  });
});
