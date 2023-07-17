package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Integer> {
    Optional<Member> findByPhoneAndPass(@NonNull String phone, @NonNull String pass);
    Optional<Member> findByPhoneAndPassAndStatus(@NonNull String phone, @NonNull String pass, @NonNull Boolean status);
    Optional<Member> findByPhoneAndStatus(String phone, Boolean status);
    Optional<Member> findByPhone(String phone);

    List<Member> findFirst100ByOrderByGmtCreateDesc();
    List<Member> findByOrderByGmtCreateDesc();

    List<Member> findFirst100ByMemberRoles_Role(String role);
    List<Member> findByMemberRoles_Role(String role);

    @Transactional
    @Modifying
    @Query("update Member m set m.status = ?1, m.gmtModify = ?2 where m.id = ?3")
    void updateStatusAndGmtModifyById(Boolean status, Date gmtModify, Integer id);


    boolean existsByPhoneAndPass(String phone, String pass);
}