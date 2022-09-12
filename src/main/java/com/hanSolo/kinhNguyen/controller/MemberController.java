package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.models.MemberRole;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.repository.MemberRoleRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberRepository memberRepo;

    @Autowired private MemberRoleRepository memberRoleRepo;


    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final LoginRequest login) throws ServletException, UnsupportedEncodingException {
        String decoded = new String(Base64.getDecoder().decode(login.loginStr));
        String[] parts = decoded.split(Utility.LOGIN_DILIMITER);

        Optional<Member> memOpt = memberRepo.findByPhoneAndPassAndStatus(parts[3], parts[14], Utility.ACTIVE_STATUS);
        if (parts[0].isEmpty() || memOpt.isEmpty() ) {
            throw new ServletException("Invalid login");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(parts[14]);
        String decodedString = new String(decodedBytes);

     // List<MemberRole> mr = memberRoleService.findByEmail(parts[3]);

        List<String> roleList = new ArrayList<>();
        for(MemberRole r : memOpt.get().getMemberRoles() ){
            roleList.add(r.getRole());
        }

        return new LoginResponse(Jwts.builder()
                .setSubject(parts[3])
                .claim("roles", roleList)
                .claim("name", memOpt.get().getFullname())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Utility.AUTHENTICATION_TIMEOUT*60*1000))
                .signWith(SignatureAlgorithm.HS256, Utility.SECRET_KEY.getBytes("UTF-8"))
                .compact());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AddResponse add(@RequestBody final SignupRequest signup) throws ServletException {
        String decoded = new String(Base64.getDecoder().decode(signup.signupStr));
        String[] parts = decoded.split(Utility.LOGIN_DILIMITER);

        if (memberRepo.findByPhone(parts[3]).isPresent()) {
            throw new ServletException("phoneExists");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(parts[14]);
        String decodedString = new String(decodedBytes);

        Date now = new Date();
        List<MemberRole> roleList = new ArrayList<>();

        Member member = new Member();
        member.setEmail(signup.email);
        member.setPass(parts[14]);
        member.setFullname(signup.fullName);
        member.setPhone(parts[3]);
        member.setGmtCreate(now);
        member.setGmtModify(now);
        member.setStatus(Utility.ACTIVE_STATUS);
        roleList.add(new MemberRole(Utility.MEMBER_ROLE,"0", member.getFullname(), member.getPhone(),member,now,now));

        member.setMemberRoles(roleList);
        Member returnMem = memberRepo.save(member);
   //     memberRoleService.save();
        return new AddResponse(returnMem.getPhone());
    }


    private static class LoginResponse {
        public String token;
        public LoginResponse(final String token) {
            this.token = token;
        }
    }

    private static class LoginRequest {
        public String loginStr;
    }

    private static class SignupRequest {
        public String signupStr;
        public String fullName;
        public String email;
    }

    @SuppressWarnings("unused")
    private static class AddResponse {
        public String replyStr;
        public AddResponse(final String replyStr) {
            this.replyStr = replyStr;
        }
    }
}
