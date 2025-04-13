// See the Electron documentation for details on how to use preload scripts:
// https://www.electronjs.org/docs/latest/tutorial/process-model#preload-scripts

import { contextBridge, ipcRenderer } from 'electron';

// Expose protected methods that allow the renderer process to use
// the ipcRenderer without exposing the entire object
contextBridge.exposeInMainWorld('electronAPI', {
  // Authentication methods
  login: (role: string, credentials: any) => ipcRenderer.invoke('login', role, credentials),
  logout: () => ipcRenderer.invoke('logout'),
  getCurrentUser: () => ipcRenderer.invoke('getCurrentUser'),
  
  // Media store methods could be added here later
  // getMediaItems: () => ipcRenderer.invoke('getMediaItems'),
  // searchMedia: (query: string) => ipcRenderer.invoke('searchMedia', query),
});

console.log('Preload script loaded');