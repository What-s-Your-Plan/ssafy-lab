package com.butter.wypl.group.domain;

import static com.butter.wypl.group.exception.GroupErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.group.exception.GroupException;
import com.butter.wypl.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "group_tbl")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	private int id;

	@Column(name = "name", length = 20, nullable = false)
	private String name;

	@Column(name = "description", length = 50)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	private Member owner;

	@OneToMany(mappedBy = "group")
	private List<MemberGroup> memberGroups = new ArrayList<>();

	@Builder
	private Group(String name, String description, Member owner) {
		this.name = name;
		this.description = description;
		this.owner = owner;
	}

	public static Group of(String name, String description, Member owner) {
		validateName(name);
		validateDescription(description);
		return Group.builder()
				.name(name)
				.description(description)
				.owner(owner)
				.build();
	}

	public static void validateName(String name) {
		if (name == null || name.isBlank() || name.length() > 20) {
			throw new GroupException(NOT_APPROPRIATE_TYPE_OF_GROUP_NAME);
		}
	}

	public static void validateDescription(String description) {
		if (description.length() > 50) {
			throw new GroupException(EXCEED_MAX_LENGTH_OF_GROUP_DESCRIPTION);
		}
	}

	public void updateGroupInfo(String name, String description) {
		validateName(name);
		validateDescription(description);
		this.name = name;
		this.description = description;
	}

}
