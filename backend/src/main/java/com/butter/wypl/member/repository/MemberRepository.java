package com.butter.wypl.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butter.wypl.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	Optional<Member> findByEmail(String email);
}
