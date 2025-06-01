import { screen, render } from '@testing-library/react';

import Pckgs from '../../../src/components/Profile/Pckgs';
import { AllProviders } from '../../AllProviders';
import { db } from '../../mocks/db';
import { toPlainObject } from 'lodash';
import { IPackage } from '../../../src/interfaces';

describe('Pckgs', () => {
  const getProps = () => {
    const profileEntity = db.fullProfile.create();

    const { pckg } = profileEntity;
    let { basic, standard, pro } = pckg;

    if (!basic || !standard || !pro) {
      throw new Error('Missing package: basic, standard, or pro in profile');
    }

    const basicPackageItem = db.packageItem.create();
    const standardPackageItem = db.packageItem.create();
    const proPackageItem = db.packageItem.create();

    let basicPOJO: IPackage = toPlainObject(basic);
    basicPOJO = { ...basicPOJO, price: null as unknown as string };

    const basicPckg = { ...basicPOJO, items: [toPlainObject(basicPackageItem)] } as IPackage;
    const standardPckg = { ...toPlainObject(standard), items: [toPlainObject(standardPackageItem)] } as IPackage;
    const proPckg = { ...toPlainObject(pro), items: [toPlainObject(proPackageItem)] } as IPackage;

    return {
      basic: basicPckg,
      standard: standardPckg,
      pro: proPckg,
    };
  };

  const renderComponent = () => {
    const props = getProps();

    render(<Pckgs {...props} />, { wrapper: AllProviders });

    return {
      props,
    };
  };

  it('should render title and prices for all non-null packages', () => {
    const { props } = renderComponent();

    // basic has a null price

    expect(screen.queryByText('Basic')).not.toBeInTheDocument();
    expect(screen.getByText('Standard')).toBeInTheDocument();
    expect(screen.getByText('Pro')).toBeInTheDocument();

    expect(screen.queryByText(`$${props.basic.price}`)).not.toBeInTheDocument();
    expect(screen.getByText(`$${props.standard.price}`)).toBeInTheDocument();
    expect(screen.getByText(`$${props.pro.price}`)).toBeInTheDocument();
  });

  it('should render basic, standard, and pro descriptions correctly', () => {
    const { props } = renderComponent();

    expect(screen.getByText(props.basic.description)).toBeInTheDocument();
    expect(screen.getByText(props.standard.description)).toBeInTheDocument();
    expect(screen.getByText(props.pro.description)).toBeInTheDocument();
  });

  it('should render package items correctly', () => {
    const { props } = renderComponent();

    props.basic.items.forEach(({ name }) => {
      expect(screen.getByText(name)).toBeInTheDocument();
    });
  });

  it('should render a checkmark next to each item name correctly', () => {
    renderComponent();

    const checkmarks = screen.getAllByTestId('pckg-item-check');

    expect(checkmarks.length).toBeGreaterThan(1);
  });
});
