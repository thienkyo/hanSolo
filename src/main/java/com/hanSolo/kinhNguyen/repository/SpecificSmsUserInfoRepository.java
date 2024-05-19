package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.SpecificSmsUserInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SpecificSmsUserInfoRepository extends PagingAndSortingRepository<SpecificSmsUserInfo, Integer> {
    Optional<SpecificSmsUserInfo> findByPhone(String phone);
    List<SpecificSmsUserInfo> findFirst100ByOrderByGmtCreateDesc();
    List<SpecificSmsUserInfo> findAllByOrderByGmtCreateDesc();

    List<SpecificSmsUserInfo> findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRun(Date orderCreateDate, String jobIdList, Date lastSendSmsDate, String jobIdToRun);

    List<SpecificSmsUserInfo> findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRunAndIsTestUser(Date orderCreateDate, String jobIdList, Date lastSendSmsDate, String jobIdToRun, Boolean isTestUser);

    @Transactional
    @Modifying
    @Query("update SpecificSmsUserInfo s set s.lastSendSmsDate = ?1 where s.phone = ?2")
    int updateLastSendSmsDateByPhone(Date lastSendSmsDate, String phone);

    List<SpecificSmsUserInfo> findFirst100ByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<SpecificSmsUserInfo> findFirst100ByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);
    List<SpecificSmsUserInfo> findByClientCodeOrderByGmtCreateDesc(String clientCode);
    List<SpecificSmsUserInfo> findByClientCodeAndShopCodeOrderByGmtCreateDesc(String clientCode, String shopCode);

}