package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Integer> {
    Optional<Member> findByPhoneAndPass(@NonNull String phone, @NonNull String pass);
    Optional<Member> findByPhoneAndPassAndStatus(@NonNull String phone, @NonNull String pass, @NonNull Boolean status);

    boolean existsByPhoneAndPass(String phone, String pass);
}