package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.response.LoginResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/authenticated")
public class AuthenticationController {

    @Autowired private MemberRepository memberRepo;
 //   @Autowired private OrdersService orderService;

    @RequestMapping(value = "me", method = RequestMethod.GET)
    public Member getMe(final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(claims.get("sub")+"", Utility.ACTIVE_STATUS);

        if (memOpt.isEmpty() ) {
            return new LoginResponse("",Utility.FAIL_ERRORCODE,"account not exist.");
        }

        return memDAO;
    }
}
