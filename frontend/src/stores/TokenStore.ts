import { create } from 'zustand';
import { persist } from 'zustand/middleware';

type JsonWebTokensState = {
  accessToken: string | null;
  refreshToken: string | null;
  setAccessToken: (newAccessToken: string) => void;
  setRefreshToken: (newRefreshToken: string) => void;
  resetTokens: () => void;
};

const useJsonWebTokensStore = create<JsonWebTokensState>()(
  persist(
    (set): JsonWebTokensState => ({
      accessToken: null,
      refreshToken: null,
      setAccessToken: (newAccessToken: string) => {
        set(() => ({ accessToken: newAccessToken }));
      },
      setRefreshToken: (newRefreshToken: string) => {
        set(() => ({ refreshToken: newRefreshToken }));
      },
      resetTokens: () => {
        set(() => ({ accessToken: null, refreshToken: null }));
      },
    }),
    {
      name: 'tokenStorage',
    },
  ),
);

export default useJsonWebTokensStore;
