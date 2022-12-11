package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Integer> {
    Optional<Member> findByPhoneAndPass(@NonNull String phone, @NonNull String pass);
    Optional<Member> findByPhoneAndPassAndStatus(@NonNull String phone, @NonNull String pass, @NonNull Boolean status);
    Optional<Member> findByPhoneAndStatus(String phone, Boolean status);
    Optional<Member> findByPhone(String phone);

    List<Member> findFirst100ByOrderByGmtModifyDesc();

    List<Member> findByOrderByGmtModifyDesc();

    boolean existsByPhoneAndPass(String phone, String pass);
}