package com.butter.wypl.notification.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.butter.wypl.global.annotation.MongoRepositoryTest;
import com.butter.wypl.notification.data.NotificationTypeCode;
import com.butter.wypl.notification.domain.Notification;
import com.butter.wypl.notification.domain.NotificationButton;

@MongoRepositoryTest
class NotificationRepositoryTest {

	@Autowired
	private NotificationRepository notificationRepository;

	/*
	 * 1. 알림 생성 종류
	 *  [그룹]
	 *  - 그룹 초대
	 *   param: 그룹ID
	 *  - 그룹 일정 등록 => 관련 일정인사람들만
	 *   param: 그룹ID, 일정ID
	 *  [회고]
	 *  - 회고록 작성하세요
	 *   param: 일정ID
	 *
	 * 2. 알림 조회
	 *  - 회원 ID로 조회, order by 생성일시 desc
	 *  -
	 * 3. 알림 삭제
	 *  - 사용자 직접 삭제
	 *  - 읽지 않은 알림 => 생성일로부터 30일 지나면 삭제
	 *  - 읽은 알림 => 읽고 난 뒤 7일 뒤 삭제
	 */
	@Test
	@DisplayName("특정 이벤트 발생 후 알림을 생성한다.")
	void createNotificationTest() {
		// given
		String message = "";
		List<NotificationButton> buttons = new ArrayList<>();
		buttons.add(
				NotificationButton.builder()
						.text("수락")
						.actionUrl("ok")
						.color("#000000")
						.logo("수락이지롱")
						.build()
		);
		buttons.add(
				NotificationButton.builder()
						.text("취소")
						.actionUrl("cancel")
						.color("#000000")
						.logo("취소지롱")
						.build()
		);

		Notification notification = Notification.builder()
				.memberId(1)
				.message(message)
				.buttons(buttons)
				.isRead(false)
				.typeCode(NotificationTypeCode.GROUP)
				.build();

		// when
		Notification savedNotification = notificationRepository.save(notification);

		// then
		assertThat(savedNotification).isNotNull();
		assertThat(savedNotification.getMemberId()).isEqualTo(1);
	}

	@Test
	@DisplayName("알림 최초 조회")
	void findNotificationByMemberId() {
		//given
		int memberId = 99;
		PageRequest pageReq = PageRequest.of(0, 10);
		//when

		//then
		assertThatCode(() -> {
			Page<Notification> result = notificationRepository.findAllByMemberId(memberId, pageReq);
			assertThat(result).isNotNull();
		}).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("이후 알림 조회")
	void findNotificationByMemberIdAndId() {
		//given
		int memberId = 99;
		//when
		PageRequest pageReq = PageRequest.of(0, 10);
		String lastId = "6630481609e3d42813504c86";
		//then
		assertThatCode(() -> {
			Page<Notification> result = notificationRepository.findAllByLastId(memberId, lastId, pageReq);
			assertThat(result).isNotNull();
		}).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("회원ID에 맞는 알림 전체를 삭제한다.")
	void deleteNotificationByMemberId () {
	    //given
		// 저장
		int memberId = 1;
		makeNotificationList(memberId);

	    //when
		Page<Notification> pageOfNotifications = notificationRepository.findAllByMemberId(memberId, PageRequest.of(0, 50));
		List<Notification> content = pageOfNotifications.getContent();
		assertThat(content.size()).isEqualTo(5);

		notificationRepository.deleteByMemberId(memberId);
		Page<Notification> deletedResult = notificationRepository.findAllByMemberId(memberId, PageRequest.of(0, 50));
		List<Notification> content1 = deletedResult.getContent();
		//then
		assertThat(content1.size()).isEqualTo(0);
	}

	// List<Notification> makeNotificationList() {
	void makeNotificationList(final int memberId) {
		List<NotificationButton> buttons = new ArrayList<>();
		buttons.add(
			NotificationButton.builder()
				.text("삭제버튼")
				.actionUrl("delete")
				.color("#000000")
				.build()
		);
		buttons.add(
			NotificationButton.builder()
				.text("취소버튼")
				.actionUrl("cancel")
				.color("#000000")
				.build()
		);

		Stream.iterate(0, i -> i < 5, i -> i + 1)
			.forEach(i -> notificationRepository.save(
				Notification.builder()
					.memberId(memberId)
					.message("삭제 테스트용" + i)
					.buttons(buttons)
					.isRead(false)
					.typeCode(NotificationTypeCode.GROUP)
					.build()
			));
	}
}