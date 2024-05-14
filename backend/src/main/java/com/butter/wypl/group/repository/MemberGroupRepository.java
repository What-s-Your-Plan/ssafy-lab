package com.butter.wypl.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.domain.MemberGroupId;
import com.butter.wypl.group.repository.query.MemberGroupRepositoryCustom;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, MemberGroupId>,
		MemberGroupRepositoryCustom {

	Optional<MemberGroup> findMemberGroupByMemberIdAndGroupId(int memberId, int groupId);

	void deleteByMemberIdAndGroupId(int memberId, int groupId);

	//member에 해당하는 member_group
	List<MemberGroup> findMemberGroupsByMemberId(int memberId);
}
