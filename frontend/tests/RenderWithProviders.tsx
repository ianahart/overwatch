import React from 'react';
import { AllProviders } from './AllProviders';
import { TRootState } from '../src/state/store';
import { PreloadedState } from '@reduxjs/toolkit';

export function getWrapper(preloadedState?: PreloadedState<TRootState>) {
  return function Wrapper({ children }: { children: React.ReactNode }) {
    return <AllProviders preloadedState={preloadedState}>{children}</AllProviders>;
  };
}
