import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';
import userEvent from '@testing-library/user-event';

import BasicInfo from '../../../src/components/Profile/BasicInfo';
import { db } from '../../mocks/db';
import { getLoggedInUser } from '../../utils';
import { IUser, IVerifyConnectionResponse } from '../../../src/interfaces';
import { HttpResponse, http } from 'msw';
import { baseURL } from '../../../src/util';
import { RequestStatus } from '../../../src/enums';

describe('BasicInfo', () => {
  beforeEach(() => {
    [1, 2].forEach((id) => {
      db.user.delete({ where: { id: { equals: id } } });
    });
  });
  const getProps = (overrides = {}, user?: ReturnType<typeof db.user.create>) => {
    const profileUser = user ?? db.user.create();
    const profileEntity = db.fullProfile.create({
      profile: {
        userId: profileUser,
      },
    });

    const { profile, basicInfo, profileSetup } = profileEntity;

    if (!profile || !basicInfo || !profileSetup) {
      throw new Error('Missing profile, basicInfo, or profileSetup');
    }

    const { userId, abbreviation, city, country } = profile;
    const { avatar: avatarUrl } = profileSetup;
    const { fullName } = basicInfo;

    if (!fullName || !userId?.id || !abbreviation || !city || !country || !avatarUrl) {
      throw new Error('One or more fields are missing');
    }

    return {
      fullName,
      userId: userId.id,
      abbreviation,
      city,
      country,
      avatarUrl,
      ...overrides,
    };
  };

  const renderComponent = (role: string, curUserId?: number, sharedUser?: ReturnType<typeof db.user.create>) => {
    const { curUser: curUserEntity, wrapper } = getLoggedInUser({ id: curUserId, role });
    const curUser: IUser = { ...toPlainObject(curUserEntity) };

    const props = getProps({}, sharedUser);

    render(<BasicInfo {...props} />, { wrapper });

    return {
      user: userEvent.setup,
      profileUser: props,
      curUser,
    };
  };

  it('should render avatar and basic user info', async () => {
    const { profileUser } = renderComponent('REVIEWER', 1);

    expect(screen.getByText(profileUser.fullName)).toBeInTheDocument();
    expect(screen.getByText(`${profileUser.city}, ${profileUser.country}`)).toBeInTheDocument();
    expect(screen.getByRole('img')).toHaveAttribute('src', profileUser.avatarUrl);
  });

  it('should render options menu when user is viewing their own profile', async () => {
    const sharedUser = db.user.create({ id: 2 });

    renderComponent('REVIEWER', 2, sharedUser);

    expect(await screen.findByTestId('cur-user-profile-options')).toBeInTheDocument();
  });

  it('should render connected when the viewer is a REVIEWER and the users are not the same user', async () => {
    renderComponent('REVIEWER', 1);
    expect(await screen.findByText(/connected/i)).toBeInTheDocument();
  });

  it('should render connect button when viewer is not reviewer and not reviewing their own profile', async () => {
    http.get(`${baseURL}/connections/verify`, () => {
      return HttpResponse.json<IVerifyConnectionResponse>(
        {
          message: 'success',
          data: {
            id: 2,
            status: RequestStatus.UNINITIATED,
          },
        },
        { status: 200 }
      );
    });

    renderComponent('USER', 1);

    expect(await screen.findByRole('button', { name: /connect/i })).toBeInTheDocument();
  });
});
