import { screen, render } from '@testing-library/react';
import { toPlainObject } from 'lodash';

import TimeSlots from '../../../src/components/Profile/TimeSlots';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';
import { IDayAvailability, ITimeSlot } from '../../../src/interfaces';

describe('TimeSlots', () => {
  const getProps = () => {
    const availabilityEntity: IDayAvailability = toPlainObject(db.availability.create());
    const slotEntity: ITimeSlot = toPlainObject(db.slot.create());
    const availability: IDayAvailability = { ...availabilityEntity, slots: [slotEntity] };

    return {
      availability: [availability],
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<TimeSlots {...props} />, { wrapper: AllProviders });

    return {
      getHeading: () => screen.getByRole('heading', { level: 3 }),
      props,
    };
  };

  it('should render the heading title correctly', () => {
    const { getHeading } = renderComponent();

    expect(getHeading()).toBeInTheDocument();
  });

  it('should render the availability time slots correctly', () => {
    const { props } = renderComponent();

    const [availability] = props.availability;

    const { startTime, endTime } = availability.slots[0];

    expect(screen.getByText(availability.day)).toBeInTheDocument();
    expect(screen.getByText(`${startTime}-${endTime}`)).toBeInTheDocument();
  });
});
