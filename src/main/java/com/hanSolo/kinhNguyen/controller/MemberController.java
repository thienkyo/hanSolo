package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.models.MemberRole;
import com.hanSolo.kinhNguyen.models.SmsJob;
import com.hanSolo.kinhNguyen.models.SmsQueue;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.repository.MemberRoleRepository;
import com.hanSolo.kinhNguyen.repository.SmsJobRepository;
import com.hanSolo.kinhNguyen.repository.SmsQueueRepository;
import com.hanSolo.kinhNguyen.request.LoginRequest;
import com.hanSolo.kinhNguyen.request.SignupRequest;
import com.hanSolo.kinhNguyen.response.LoginResponse;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.response.SmsJobResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired private MemberRepository memberRepo;
    @Autowired private MemberRoleRepository memberRoleRepo;
    @Autowired private SmsJobRepository smsJobRepo;
    @Autowired private SmsQueueRepository smsQueueRepo;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final LoginRequest login) throws ServletException, UnsupportedEncodingException {
        String decoded = new String(Base64.getDecoder().decode(login.getLoginStr()));
        String[] parts = decoded.split(Utility.LOGIN_DILIMITER);

        // parts[3] : phone
        // parts[14] : pass
        Optional<Member> memOpt = memberRepo.findByPhoneAndPassAndStatus(parts[3], parts[14], Utility.ACTIVE_STATUS);
        if (parts[0].isEmpty() || memOpt.isEmpty() ) {
            return new LoginResponse("",Utility.FAIL_ERRORCODE,"account not exist.");
        }

        List<String> roleList = new ArrayList<>();
        for(MemberRole r : memOpt.get().getMemberRoles() ){
            roleList.add(r.getRole());
        }

        if(CommonCache.LOGIN_MEMBER_LIST.size() == 8){
            CommonCache.LOGIN_MEMBER_LIST.clear();
        }

        CommonCache.LOGIN_MEMBER_LIST.put(memOpt.get().getPhone(),memOpt.get());

        return new LoginResponse(Jwts.builder()
                .setSubject(parts[3])
                .claim("roles", roleList)
                .claim("name", memOpt.get().getFullName())
                .claim("status", memOpt.get().getStatus())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Utility.AUTHENTICATION_TIMEOUT))
                .signWith(SignatureAlgorithm.HS256, Utility.SECRET_KEY.getBytes("UTF-8"))
                .compact(),Utility.SUCCESS_ERRORCODE,"login success");
    }

    @RequestMapping(value = "add", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse add(@RequestBody final SignupRequest signup) throws ServletException {
        String decoded = new String(Base64.getDecoder().decode(signup.getSignupStr()));
        String[] parts = decoded.split(Utility.LOGIN_DILIMITER);

        if (memberRepo.findByPhone(parts[3]).isPresent()) {
            return new GenericResponse("",Utility.FAIL_ERRORCODE,"Phone already existed.");
        }

        Date now = new Date();
        List<MemberRole> roleList = new ArrayList<>();

        Member member = new Member();
        member.setEmail(signup.getEmail());
        member.setPass(parts[14]);
        member.setFullName(signup.getFullName());
        member.setPhone(parts[3]);
        member.setGmtCreate(now);
        member.setGmtModify(now);
        member.setStatus(Utility.ACTIVE_STATUS);
        roleList.add(new MemberRole(Utility.MEMBER_ROLE,"0", member.getFullName(), member.getPhone(),member,now,now));

        member.setMemberRoles(roleList);
        Member returnMem = memberRepo.save(member);
        return new GenericResponse(returnMem.getPhone(),Utility.SUCCESS_ERRORCODE,"Register user successfully.");
    }

    //////////////////////////// fastSMS /////////////////////////////
    @RequestMapping(value = "getFastSMSConfig", method = RequestMethod.GET)
    public SmsJobResponse getFastSMSConfig() {
        SmsJob job = smsJobRepo.findFirstByJobType(Utility.SMS_JOB_FASTSMS).isPresent() ?
                smsJobRepo.findFirstByJobType(Utility.SMS_JOB_FASTSMS).get() : null;
        return new SmsJobResponse(job,Utility.SUCCESS_ERRORCODE,"get sms config success");
    }

    @RequestMapping(value = "upsertSmsQueue", method = RequestMethod.POST)
    public GenericResponse upsertSmsQueue(@RequestBody final SmsQueue one, final HttpServletRequest request) {
        if(one.getPassCode().equalsIgnoreCase(Utility.SMS_JOB_FASTSMS_PASSCODE)){
            smsQueueRepo.save(one);
            return new GenericResponse("insert SmsQueue success",Utility.SUCCESS_ERRORCODE,"Success");
        }
        return new GenericResponse("insert SmsQueue fail",Utility.FAIL_ERRORCODE,"FAIL");
    }
}
