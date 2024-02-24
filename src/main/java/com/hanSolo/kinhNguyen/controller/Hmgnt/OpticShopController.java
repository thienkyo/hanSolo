package com.hanSolo.kinhNguyen.controller.Hmgnt;

import com.hanSolo.kinhNguyen.DTO.ClientShopList;
import com.hanSolo.kinhNguyen.models.Client;
import com.hanSolo.kinhNguyen.models.Contract;
import com.hanSolo.kinhNguyen.models.Member;
import com.hanSolo.kinhNguyen.models.MemberRole;
import com.hanSolo.kinhNguyen.models.OpticShopOnlineConfig;
import com.hanSolo.kinhNguyen.models.Salary;
import com.hanSolo.kinhNguyen.models.Shop;
import com.hanSolo.kinhNguyen.models.ShopConfig;
import com.hanSolo.kinhNguyen.repository.ClientRepository;
import com.hanSolo.kinhNguyen.repository.MemberRepository;
import com.hanSolo.kinhNguyen.repository.OpticShopOnlineConfigRepository;
import com.hanSolo.kinhNguyen.repository.ShopRepository;
import com.hanSolo.kinhNguyen.response.GeneralResponse;
import com.hanSolo.kinhNguyen.service.ClientService;
import com.hanSolo.kinhNguyen.utility.Utility;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/Hmgnt")
public class OpticShopController {

    @Autowired private ClientRepository clientRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private MemberRepository memberRepo;
    @Autowired private OpticShopOnlineConfigRepository opticShopOnlineRepo;

    @Autowired private ClientService clientService;

    @RequestMapping(value = "upsertClient", method = RequestMethod.POST)
    public GeneralResponse<Client> upsertClient(@RequestBody final Client one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());

        }
        one.setGmtModify(Utility.getCurrentDate());

        return new GeneralResponse(clientRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save client success");
    }

    @RequestMapping(value = "updateClientStatus", method = RequestMethod.POST)
    public GeneralResponse<String> updateClientStatus(@RequestBody final Client one) throws ParseException {
        clientService.updateClientStatus(one);
        return new GeneralResponse("upsert_order_success",Utility.SUCCESS_ERRORCODE,"Success");
    }

    @RequestMapping(value = "insertMemberByClient", method = RequestMethod.POST)
    public GeneralResponse<Member> createSuperAdmin(@RequestBody final Client one) {
         Optional<Member> memOpt = memberRepo.findByPhone(one.getPhone());
            if (memOpt.isEmpty() ) {
                Date now = new Date();
                List<MemberRole> roleList = new ArrayList<>();
                Member member = new Member();
                member.setFullName(one.getName());
                member.setPhone(one.getPhone());
                member.setAddress(one.getAddress());
                member.setPass(Utility.DEFAULT_PW);
                member.setGmtCreate(now);
                member.setGmtModify(now);
                member.setStatus(Utility.INACTIVE_STATUS);
                member.setClientCode(one.getClientCode());
                member.setShopCode("ALL");
                member.setIsShow(true);
                roleList.add(new MemberRole(Utility.MEMBER_ROLE, "0", member.getFullName(), member.getPhone(), member, now, now));
                roleList.add(new MemberRole(Utility.MOD_ROLE, "0", member.getFullName(), member.getPhone(), member, now, now));
                roleList.add(new MemberRole(Utility.ADMIN_ROLE, "0", member.getFullName(), member.getPhone(), member, now, now));
                roleList.add(new MemberRole(Utility.SUPERADMIN_ROLE, "0", member.getFullName(), member.getPhone(), member, now, now));
                member.setMemberRoles(roleList);
                memberRepo.save(member);
                return new GeneralResponse(member,Utility.SUCCESS_ERRORCODE,"Save member success");
            }
            return new GeneralResponse("member existed",Utility.FAIL_ERRORCODE,"member existed");

    }

    @RequestMapping(value = "getAllClient", method = RequestMethod.GET)
    public List<Client> getAllClient() {
        return clientRepo.findAllByOrderByGmtCreateDesc();
    }

    @RequestMapping(value = "deleteClient", method = RequestMethod.POST)
    public GeneralResponse<String> deleteClient(@RequestBody final Client one, final HttpServletRequest request)  {
        if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE) ){
            List<Shop> shopList = shopRepo.findByClientCodeOrderByGmtCreateDesc(one.getClientCode());
            if(!shopList.isEmpty()){
                shopRepo.deleteAll(shopList);
            }

            clientRepo.delete(one);
            return new GeneralResponse(Utility.SUCCESS_ERRORCODE,Utility.SUCCESS_ERRORCODE,"Success");
        }
        return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
    }

    @RequestMapping(value = "getClientShopList", method = RequestMethod.GET)
    public GeneralResponse<ClientShopList> getClientShopList() {
        List<Client> clientList = clientRepo.findAllByOrderByGmtCreateDesc();
        List<Shop> shopList = (List<Shop>) shopRepo.findAll();
        ClientShopList csl = new ClientShopList();
        csl.setClientList(clientList);
        csl.setShopList(shopList);

        return new GeneralResponse(csl,Utility.SUCCESS_ERRORCODE,"success");
    }

    //////////////////////////// shop Management section /////////////////////////////
    @RequestMapping(value = "upsertShop", method = RequestMethod.POST)
    public GeneralResponse<Shop> upsertShop(@RequestBody final Shop one) throws ParseException {
        if(one.getId() == 0){
            one.setGmtCreate(Utility.getCurrentDate());
            String code = RandomStringUtils.randomAlphanumeric(5);
            one.setShopCode("S"+code.toUpperCase(Locale.ROOT));
        }
        one.setGmtModify(Utility.getCurrentDate());
        return new GeneralResponse(shopRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save shop success");
    }

    @RequestMapping(value = "getAllShopOneClient/{clientId}", method = RequestMethod.GET)
    public List<Shop> getAllSalaryOnePerson(@PathVariable final int clientId, final HttpServletRequest request) {
        List<Shop> shopList = new ArrayList<>();
        if(onlyAllowThisRole(request,Utility.GODLIKE_ROLE) ){
            return shopRepo.findByClientIdOrderByGmtCreateDesc(clientId);
        }
        return shopList;
    }

    @RequestMapping(value = "deleteShop", method = RequestMethod.POST)
    public GeneralResponse deleteShop(@RequestBody final Shop one, final HttpServletRequest request)  {
        if(!onlyAllowThisRole(request,Utility.GODLIKE_ROLE) ){
            return new GeneralResponse("no authorization",Utility.FAIL_ERRORCODE,Utility.FAIL_MSG);
        }
        shopRepo.delete(one);
        return new GeneralResponse("",Utility.SUCCESS_ERRORCODE,"Success");
    }

    //////////////////////////// optic shop online config section /////////////////////////////
    @RequestMapping(value = "getOSOConfig", method = RequestMethod.GET)
    @Cacheable("OSOConfig")
    public GeneralResponse<OpticShopOnlineConfig> getOSOConfig() {
        return new GeneralResponse(opticShopOnlineRepo.findFirstByOrderByNameDesc(),Utility.SUCCESS_ERRORCODE,"query success");
    }

    @RequestMapping(value = "refreshOSOConfig", method = RequestMethod.GET)
    @CacheEvict(value = "OSOConfig",allEntries = true)
    public GeneralResponse<OpticShopOnlineConfig> refreshOSOConfig() {
        return new GeneralResponse("refresh success",Utility.SUCCESS_ERRORCODE,"success");
    }

    @RequestMapping(value = "upsertOSOConfig", method = RequestMethod.POST)
    public GeneralResponse<OpticShopOnlineConfig> upsertOSOConfig(@RequestBody final OpticShopOnlineConfig one) {
        return new GeneralResponse(opticShopOnlineRepo.save(one),Utility.SUCCESS_ERRORCODE,"Save key success");
    }

    /**
     * ex: onlyAllowThisRole(request,Utility.SUPERADMIN_ROLE)
     * if true: allow continuing.
     * @param request
     * @param role
     * @return
     */
    private boolean onlyAllowThisRole(final HttpServletRequest request, String role){
        final Claims claims = (Claims) request.getAttribute("claims");
        if(((List<String>) claims.get("roles")).contains(role)){
            return true;
        }
        return false;
    }
}
