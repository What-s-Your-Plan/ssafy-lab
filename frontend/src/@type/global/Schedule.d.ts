interface Repeat {
  repetitionCycle: string; // 필수
  week: number; // week일 경우 몇 주에 한번 반복할지, 선택
  dayOfWeek: number; // bit로 0100010와 같은 형태로, 선택
  day: number; //월 년도 반복일 경우, 선택
  month: number; // 년도 반복일 경우, 선택
  period: string; // 앤드 데이트 여부 필수
  endRDate: string; // 선택
}

type Label = {
  label_id: number;
  title: string;
  color: string;
  member_id: number;
};

type Member = {
  member_id: number;
  nickname: string;
  oauth_id: string;
  profile_image: string;
};

interface Schedule {
  title: string; // 필수
  description: string; // 선택
  startDate: string; // 날짜 + 시간, 필수
  endDate: string; // 날짜 + 시간, 필수
  isAllday: boolean;
  startAMPM: string;
  endAMPM: string;
  startHour: number;
  endHour: number;
  startMinute: number;
  endMinute: number;
  category: 'Member' | 'Group'; // 개인: Member, 그룹: Group, 필수
  groupId: number;
  label: Label | null;
  members: Array<Member>; // 개인 일정은 한명만
  isRepetition: boolean; // 선택
}
