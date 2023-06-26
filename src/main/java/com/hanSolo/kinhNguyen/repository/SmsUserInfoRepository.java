package com.hanSolo.kinhNguyen.repository;

import com.hanSolo.kinhNguyen.models.SmsUserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SmsUserInfoRepository extends PagingAndSortingRepository<SmsUserInfo, Integer> {
    Optional<SmsUserInfo> findByPhone(String phone);
    List<SmsUserInfo> findAllByOrderByGmtCreateDesc();
    List<SmsUserInfo> findFirst100ByOrderByGmtCreateDesc();
    List<SmsUserInfo> findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBefore(Date orderCreateDate, String jobIdList, Date lastSendSmsDate);
    List<SmsUserInfo> findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndIsTestUser(Date orderCreateDate, String jobIdList, Date lastSendSmsDate, Boolean isTestUser);
    List<SmsUserInfo> findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndOrderCreateDateAfterOrderByOrderCreateDateAsc(Date orderCreateDate, String jobIdList, Date lastSendSmsDate, Date orderCreateDate1);
    List<SmsUserInfo> findFirst100ByOrderCreateDateBeforeAndJobIdListNotLikeAndLastSendSmsDateBeforeAndIsTestUserAndOrderCreateDateAfterOrderByOrderCreateDateAsc(Date orderCreateDate, String jobIdList, Date lastSendSmsDate, Boolean isTestUser, Date orderCreateDate1);

    List<SmsUserInfo> findByGenderOrderByOrderCreateDateDesc(Boolean gender);
    List<SmsUserInfo> findByGenderAndAreaCodeOrderByOrderCreateDateDesc(Boolean gender, String areaCode);

    List<SmsUserInfo> findByOrderCreateDateBeforeAndGenderOrderByOrderCreateDateDesc(Date orderCreateDate, Boolean gender);
    List<SmsUserInfo> findByOrderCreateDateBeforeAndGenderAndAreaCodeOrderByOrderCreateDateDesc(Date orderCreateDate, Boolean gender, String areaCode);
}