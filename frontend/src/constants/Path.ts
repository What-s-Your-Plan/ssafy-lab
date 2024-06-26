const BROWSER_PATH = {
  OAUTH: {
    GOOGLE: '/login/oauth2/code/google',
  },
  LANDING: '/',
  CALENDAR: '/calendar',
  NOT_FOUND: '*',
  GROUP: {
    BASE: '/group',
    DETAIL: '/group/:groupId?',
  },
  REVIEW: {
    BASE: '/review',
    WRITE: '/review/write/:scheduleId',
    DETAIL: '/review/:reviewId',
    MODIFY: '/review/modify/:scheduleId/:reviewId',
  },
};

const API_PATH = {
  AUTH: {
    ISSUE_TOKENS: '/auth/v1/sign-in',
    REISSUE: '/auth/v1/reissue',
    MOCK_ISSUE_TOKENS: '/auth/v1/sign-in/mock',
    LOGOUT: '/auth/v1/logout',
  },
  MEMBER: {
    BASE: '/member/v1/members',
    COLOR: '/member/v1/members/colors',
    NICKNAME: '/member/v1/members/nickname',
    PROFILE_IMAGE: '/member/v1/members/profile-image',
  },
  GROUP: {
    BASE: '/group/v1/groups',
    MEMBER: '/group/v1/groups/members',
    WITHDRAW: '/group/v1/groups/:groupId/members',
    FORCE_OUT: '/group/v1/groups/:groupId/members/force-out',
    PERSONAL_COLOR: '/group/v1/groups/:groupId/members/colors',
    INVITE: '/group/v1/groups/:groupId/members/invitation',
  },
};

export { API_PATH, BROWSER_PATH };
