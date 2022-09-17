package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.models.Order;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.repository.OrderRepository;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.response.LoginResponse;
import com.hanSolo.kinhNguyen.response.MemberResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authenticated")
public class AuthenticationController {

    @Autowired private MemberRepository memberRepo;
    @Autowired private OrderRepository orderRepo;

    @RequestMapping(value = "me", method = RequestMethod.GET)
    public MemberResponse getMe(final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(claims.get("sub")+"", Utility.ACTIVE_STATUS);
        if (memOpt.isEmpty() ) {
            return new MemberResponse(null, Utility.FAIL_ERRORCODE,"member not exist or disable");
        }
        memOpt.get().setPass("");
        return new MemberResponse(memOpt.get(), Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "saveOrder", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse saveOrder(@RequestBody final Order order, final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(claims.get("sub")+"", Utility.ACTIVE_STATUS);
        if (memOpt.isEmpty() ) {
            return new GenericResponse(null, Utility.FAIL_ERRORCODE,"member not exist or disable");
        }
        order.setMember(memOpt.get());
        Order or = orderRepo.save(order);
        GenericResponse response = or == null ? new GenericResponse("",Utility.FAIL_ERRORCODE,"save order fail") : new GenericResponse(or.getId()+"",Utility.SUCCESS_ERRORCODE,"save order success");

        return response;
    }

    @RequestMapping(value = "updateMe", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse updateMe(@RequestBody final Member member) throws ServletException {
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(member.getPhone(), Utility.ACTIVE_STATUS);
        if (memOpt.isEmpty()) {
            return new GenericResponse("",Utility.FAIL_ERRORCODE,"member not exist or disable");
        }

        Member m = memOpt.get();
        m.setAddress(member.getAddress());
        m.setFullName(member.getFullName());
        m.setEmail(m.getEmail());
        m.setGmtModify(new Date());

        memberRepo.save(m);
        return new GenericResponse(m.getId()+"",Utility.SUCCESS_ERRORCODE,"save member success");
    }

}
