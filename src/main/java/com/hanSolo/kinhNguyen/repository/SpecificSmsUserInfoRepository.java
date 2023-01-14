package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.SpecificSmsUserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SpecificSmsUserInfoRepository extends PagingAndSortingRepository<SpecificSmsUserInfo, Integer> {
    Optional<SpecificSmsUserInfo> findByPhone(String phone);
    List<SpecificSmsUserInfo> findFirst100ByOrderByGmtCreateDesc();
    List<SpecificSmsUserInfo> findAllByOrderByGmtCreateDesc();

    List<SpecificSmsUserInfo> findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRun(Date orderCreateDate, String jobIdList, Date lastSendSmsDate, String jobIdToRun);

    List<SpecificSmsUserInfo> findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndJobIdToRunAndIsTestUser(Date orderCreateDate, String jobIdList, Date lastSendSmsDate, String jobIdToRun, Boolean isTestUser);

}