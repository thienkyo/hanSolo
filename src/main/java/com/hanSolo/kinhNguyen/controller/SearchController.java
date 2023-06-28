package com.hanSolo.kinhNguyen.controller;

import com.hanSolo.kinhNguyen.facade.ProductInterface;
import com.hanSolo.kinhNguyen.models.Article;
import com.hanSolo.kinhNguyen.models.Order;
import com.hanSolo.kinhNguyen.models.OrderDetail;
import com.hanSolo.kinhNguyen.models.Product;
import com.hanSolo.kinhNguyen.repository.ArticleRepository;
import com.hanSolo.kinhNguyen.repository.OrderDetailRepository;
import com.hanSolo.kinhNguyen.repository.OrderRepository;
import com.hanSolo.kinhNguyen.repository.ProductRepository;
import com.hanSolo.kinhNguyen.response.SearchResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired private ProductRepository prodRepo;
    @Autowired private ArticleRepository articleRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private OrderDetailRepository orderDetailRepo;

    @RequestMapping("{keySearch}")
    public List<SearchResponse> search(@PathVariable final String keySearch) {
        List<ProductInterface> prodList = prodRepo.findByNameContainsIgnoreCaseAndStatus(keySearch, Utility.ACTIVE_STATUS);
        List<SearchResponse> resultList = new ArrayList<>();
        prodList.forEach(item->{
            SearchResponse temp = new SearchResponse("Frame",item.getName(), item.getId()+"", item.getThumbnail(), Utility.SUCCESS_ERRORCODE, "Query success");
            resultList.add(temp);
        });

        List<Article> articleList = articleRepo.findByNameContainsIgnoreCaseAndStatus(keySearch, Utility.ACTIVE_STATUS);
        articleList.forEach(item->{
            SearchResponse temp = new SearchResponse("Blog",item.getName(), item.getId()+"", item.getThumbnail(), Utility.SUCCESS_ERRORCODE, "Query success");
            resultList.add(temp);
        });

        return resultList;
    }

    @RequestMapping("product/{keySearch}")
    public List<ProductInterface> searchProduct(@PathVariable final String keySearch) {
        return prodRepo.findByNameContainsIgnoreCaseAndStatus(keySearch, Utility.ACTIVE_STATUS);
    }

    @RequestMapping("productMngt/{keySearch}")
    public List<ProductInterface> productMngt(@PathVariable final String keySearch) {
        return prodRepo.findByNameContainsIgnoreCase(keySearch);
    }

    @RequestMapping("orderMngt/{keySearch}")
    public List<Order> orderMngt(@PathVariable final String keySearch) {
        List<Order> orderList = orderRepo.findFirst40ByShippingNameContainsIgnoreCaseOrderByGmtCreateDesc(keySearch);
        List<OrderDetail> orderDetailList = orderDetailRepo.findFirst30ByNameContainsIgnoreCaseOrderByGmtCreateDesc(keySearch);

        return orderListBuilder(orderList, orderDetailList);
    }

    @RequestMapping("orderByPhoneMngt/{keySearch}")
    public List<Order> orderByPhoneMngt(@PathVariable final String keySearch) {
        List<Order> orderList = orderRepo.findFirst40ByShippingPhoneContainsOrderByGmtCreateDesc(keySearch);
        List<OrderDetail> orderDetailList = orderDetailRepo.findFirst30ByPhoneContainsOrderByGmtCreateDesc(keySearch);

        return orderListBuilder(orderList, orderDetailList);
    }


    @RequestMapping("orderByNamePhoneMngt/{keySearch}")
    public List<Order> orderByNamePhoneMngt(@PathVariable String keySearch) {
        String regex = "[0-9 ]+";
        if(keySearch.matches(regex)){
            keySearch  = keySearch.replace(" ","");
            return orderByPhoneMngt(keySearch);
        }else{
            return orderMngt(keySearch);
        }
    }

    private List<Order> orderListBuilder(List<Order> orderList, List<OrderDetail> orderDetailList){
        List<Order> resultList = new ArrayList<>();
        for(Order or : orderList){
            if(resultList.contains(or)){
                continue;
            }
            resultList.add(or);
        }
        for (OrderDetail orderDetail : orderDetailList){
            Order or = orderBuilder(orderDetail.getOrderId(),orderDetail.getName(),orderDetail.getPhone(),orderDetail.getAddress(),orderDetail.getGmtCreate());
            if(resultList.contains(or)){
                continue;
            }
            or = orderRepo.findById(orderDetail.getOrderId()).get();
            resultList.add(or);
        }
        return resultList;
    }

    private Order orderBuilder(Integer orderId, String name, String phone, String address, Date gmtCreate){
        Order or = new Order(orderId,name,phone,address,gmtCreate,"STORE");
        or.getOrderDetails().add(new OrderDetail(name,phone,address,gmtCreate));
        return or;
    }
}
