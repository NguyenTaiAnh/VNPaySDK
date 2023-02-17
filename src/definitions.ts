export interface VNPaySDKPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
