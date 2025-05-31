import { screen, render } from '@testing-library/react';
import AdditionalInfo from '../../../src/components/Profile/AdditionalInfo';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';

describe('AdditionalInfo', () => {
  const getProps = () => {
    const userProfileEntity = db.fullProfile.create();

    const { profileSetup, additionalInfo } = userProfileEntity;

    if (!profileSetup || !additionalInfo) {
      throw new Error('Missing profileSetup or additionalInfo');
    }

    const { tagLine, bio } = profileSetup;
    const { moreInfo } = additionalInfo;

    if (!tagLine || !bio || !moreInfo) {
      throw new Error('One or more fields are missing');
    }

    return {
      tagLine,
      bio,
      moreInfo,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<AdditionalInfo {...props} />, { wrapper: AllProviders });

    return {
      props,
      secondaryHeadings: ['Bio', 'Tagline', 'More Info'],
      getHeading: () => screen.getByRole('heading', { level: 3 }),
      getSecondaryHeadings: () => screen.getAllByRole('heading', { level: 4 }),
    };
  };

  it('should render the heading and sub headings correctly', () => {
    const { getHeading, secondaryHeadings, getSecondaryHeadings } = renderComponent();

    expect(getHeading()).toBeInTheDocument();

    const headingElements = getSecondaryHeadings();

    headingElements.forEach((heading, index) => {
      expect(heading).toHaveTextContent(secondaryHeadings[index]);
    });
  });
});
