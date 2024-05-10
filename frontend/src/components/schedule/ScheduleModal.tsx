import Modal from '@/components/common/Modal';
import useForm from '@/hooks/useForm';
import SchedulePanel from '@/components/schedule/SchedulePanel';
import postSchedule from '@/services/schedule/postSchedule';

type ScheduleModalProps = {
  isOpen: boolean;
  init: Schedule & Repeat;
  handleClose: (() => void) | (() => Promise<void>);
  handleConfirm: (() => void) | (() => Promise<void>);
};

function ScheduleModal({
  isOpen,
  init,
  handleClose,
  handleConfirm,
}: ScheduleModalProps) {
  const { form, setForm, handleChange } = useForm<Schedule & Repeat>(
    init,
    postSchedule,
  );
  console.log(init)
  return (
    <Modal
      isOpen={isOpen}
      cancel="취소"
      confirm={{content:"저장", handleConfirm}}
      title={<></>}
      contents={
        <SchedulePanel
          states={form}
          handleChange={handleChange}
          setStates={setForm}
        />
      }
      handleClose={handleClose}
    />
  );
}

export default ScheduleModal;
