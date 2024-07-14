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
}

export const Session = new SessionService();
