import { WebPlugin } from '@capacitor/core';

import type { VNPaySDKPlugin } from './definitions';

export class VNPaySDKWeb extends WebPlugin implements VNPaySDKPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
