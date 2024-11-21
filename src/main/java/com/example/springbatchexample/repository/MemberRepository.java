package com.example.springbatchexample.repository;

import com.example.springbatchexample.entity.Member;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
