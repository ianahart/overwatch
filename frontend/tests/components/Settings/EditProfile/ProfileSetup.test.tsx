import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { useDispatch } from 'react-redux';
import { Mock } from 'vitest';
import { toPlainObject } from 'lodash';

import ProfileSetup from '../../../../src/components/Settings/EditProfile/ProfileSetup';
import { updateProfileSetupFormField } from '../../../../src/state/store';
import { getLoggedInUser } from '../../../utils';
import { IFullProfile, IProfileSetupResponse } from '../../../../src/interfaces';
import { db } from '../../../mocks/db';

vi.mock('react-redux', async () => {
  const actual = await vi.importActual('react-redux');
  return {
    ...actual,
    useDispatch: vi.fn(),
  };
});

describe('ProfileSetup', () => {
  const mockDispatch = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    (useDispatch as Mock).mockReturnValue(mockDispatch);
  });

  const getProps = () => {
    const fullProfile: IFullProfile = toPlainObject(db.fullProfile.create());
    const profileSetup: IProfileSetupResponse = toPlainObject(fullProfile.profileSetup);

    return {
      profileSetup,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    const { avatar, tagLine, bio } = props.profileSetup;
    const { wrapper, curUser } = getLoggedInUser(
      {},
      {
        profileSetup: {
          avatar: { name: 'avatar', error: '', type: 'file', value: avatar },
          tagLine: { name: 'tagLine', error: '', type: 'text', value: tagLine },
          bio: { name: 'bio', error: '', type: 'text', value: bio },
        },
      }
    );

    render(<ProfileSetup />, { wrapper });

    return {
      curUser,
      user: userEvent.setup(),
      props,
    };
  };

  it('should render header, avatar upload, tagLine input, and bio textarea', () => {
    renderComponent();

    expect(screen.getByText(/profile setup/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/tag line/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/bio/i)).toBeInTheDocument();
  });

  it('should dispatch "updateProfileSetupFormField" when typing in tagLine', async () => {
    const { props, user } = renderComponent();

    const tagLineInput = screen.getByLabelText(/tag line or title/i);
    await user.clear(tagLineInput);
    await user.type(screen.getByLabelText(/tag line/i), 'v');

    expect(mockDispatch).toHaveBeenCalledWith(
      updateProfileSetupFormField({
        name: 'tagLine',
        value: `${props.profileSetup.tagLine}v`,
        attribute: 'value',
      })
    );
  });

  it('should dispatche "updateProfileSetupFormField" when typing in bio', async () => {
    const { props, user } = renderComponent();

    const bioTextarea = screen.getByLabelText(/bio/i);
    await user.type(bioTextarea, 'v');

    expect(mockDispatch).toHaveBeenLastCalledWith(
      updateProfileSetupFormField({
        name: 'bio',
        value: `${props.profileSetup.bio}v`,
        attribute: 'value',
      })
    );
  });
});
