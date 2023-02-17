import { registerPlugin } from '@capacitor/core';

import type { VNPaySDKPlugin } from './definitions';

const VNPaySDK = registerPlugin<VNPaySDKPlugin>('VNPaySDK', {
  web: () => import('./web').then(m => new m.VNPaySDKWeb()),
});

export * from './definitions';
export { VNPaySDK };
