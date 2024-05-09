import { WhiteContainer } from '@/components/common/Container';
import Calendar from '@/assets/icons/calendar.svg';
import Tag from '@/assets/icons/tag.svg';
import Users from '@/assets/icons/users.svg';
import LabelButton from '@/components/common/LabelButton';
import { LabelColorsType } from '@/assets/styles/colorThemes';

type VScheduleProps = {
  schedule: ScheduleSimpleResponse;
};

function VSchedule({ schedule }: VScheduleProps) {
  const renderMembers = () => {
    return schedule.members.map((member) => (
      <img
        className="inline-block h-8 w-8 rounded-full"
        src={member.profile_image}
        alt={member.nickname}
      />
    ));
  };

  return (
    <WhiteContainer $width="900" className="flex flex-wrap gap-4">
      <div className="flex gap-4 text-sm">
        <img src={Calendar} alt="일정명" className="w-5" />
        <div>
          <div className="font-semibold">{schedule.title}</div>
          <div>
            {schedule.start_date} ~ {schedule.end_date}
          </div>
        </div>
      </div>
      <div className="flex gap-4 text-sm">
        <img src={Tag} alt="라벨" className="w-5" />
        <LabelButton $bgColor={schedule.label?.color as LabelColorsType}>
          {schedule.label?.title}
        </LabelButton>
      </div>
      <div className="flex gap-4 text-sm">
        <img src={Users} alt="참가자" className="w-5" />
        {renderMembers()}
      </div>
    </WhiteContainer>
  );
}

export default VSchedule;
