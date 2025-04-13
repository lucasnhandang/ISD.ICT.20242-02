export {};

declare global {
  interface Window {
    electronAPI: {
      login: (role: string, credentials: any) => Promise<any>;
      logout: () => Promise<any>;
      getCurrentUser: () => Promise<any>;
    };
  }
}