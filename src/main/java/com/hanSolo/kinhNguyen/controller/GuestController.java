package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.models.MemberRole;
import com.hanSolo.kinhNguyen.models.Order;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.repository.OrderRepository;
import com.hanSolo.kinhNguyen.response.GenericResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private OrderRepository orderRepo;

    @RequestMapping(value = "saveOrderDeprecated", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponse saveOrder(@RequestBody final Order order, final HttpServletRequest request) throws ServletException {

        Member mem = null;
        Optional<Member> memOpt = memberRepo.findByPhoneAndStatus(order.getShippingPhone(), Utility.ACTIVE_STATUS);
        if (memOpt.isEmpty() ) {
            Date now = new Date();
            List<MemberRole> roleList = new ArrayList<>();
            Member member = new Member();
            member.setFullName(order.getShippingName());
            member.setPhone(order.getShippingPhone());
            member.setAddress(order.getShippingAddress());
            member.setPass(Utility.DEFAULT_PW);
            member.setGmtCreate(now);
            member.setGmtModify(now);
            member.setStatus(Utility.ACTIVE_STATUS);
            roleList.add(new MemberRole(Utility.MEMBER_ROLE,"0", member.getFullName(), member.getPhone(),member,now,now));
            member.setMemberRoles(roleList);
          //   mem = memberRepo.save(member);
        }else {
            mem = memOpt.get();
        }
        order.setMember(mem);
        //Order or = orderRepo.save(order);
        Order or = null;
        GenericResponse response = or == null ? new GenericResponse("",Utility.FAIL_ERRORCODE,"save order fail") : new GenericResponse(or.getId()+"",Utility.SUCCESS_ERRORCODE,"save order success");

        return response;
    }

    @RequestMapping("getGuestOrder/{phone}")
    public List<Order> getOrdersByPhone(@PathVariable String phone) {
        return orderRepo.findByShippingPhoneOrderByIdDesc(phone);
    }
}
