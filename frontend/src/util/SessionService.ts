class SessionService {
  setItem(accessToken: string): void {
    sessionStorage.setItem('github_access_token', accessToken);
  }

  removeItem(item: string): void {
    sessionStorage.removeItem(item);
  }

  getItem(item: string): string | null {
    return sessionStorage.getItem(item);
  }

  setDynamicItem(key: string, value: string): void {
    sessionStorage.setItem(key, value);
  }
}

export const Session = new SessionService();
