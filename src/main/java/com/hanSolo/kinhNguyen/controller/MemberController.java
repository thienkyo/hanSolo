package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
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


    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final LoginRequest login) throws ServletException, UnsupportedEncodingException {
        String decoded = new String(Base64.getDecoder().decode(login.loginStr));
        String[] parts = decoded.split("d3m");

        Optional<Member> memOpt =memberRepo.findByPhoneAndPassAndStatus(parts[3], parts[14], Utility.ACTIVE_STATUS);
        if (parts[0].isEmpty() || memOpt.isEmpty() ) {
            throw new ServletException("Invalid login");
        }

     //   List<MemberRole> mr = memberRoleService.findByEmail(parts[3]);

        /*List<String> rolelist = new ArrayList<String>();
        for(MemberRole r : mr ){
            rolelist.add(r.getRole());
        }*/

        return new LoginResponse(Jwts.builder()
                .setSubject(parts[3])
                .claim("roles", memOpt.get().getMemberRoles())
                .claim("name", memOpt.get().getFullname())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Utility.AUTHENTICATION_TIMEOUT*60*1000))
                .signWith(SignatureAlgorithm.HS256, Utility.SECRET_KEY.getBytes("UTF-8"))
                .compact());
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
}
