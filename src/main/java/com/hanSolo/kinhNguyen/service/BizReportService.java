package com.hanSolo.kinhNguyen.service;

import com.hanSolo.kinhNguyen.models.BizExpense;
import com.hanSolo.kinhNguyen.models.BizReport;
import com.hanSolo.kinhNguyen.models.Order;
import com.hanSolo.kinhNguyen.models.OrderDetail;
import com.hanSolo.kinhNguyen.models.Salary;
import com.hanSolo.kinhNguyen.repository.BizExpenseRepository;
import com.hanSolo.kinhNguyen.repository.BizReportRepository;
import com.hanSolo.kinhNguyen.repository.OrderRepository;
import com.hanSolo.kinhNguyen.repository.SalaryRepository;
import com.hanSolo.kinhNguyen.response.GeneralResponse;
import com.hanSolo.kinhNguyen.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component
public class BizReportService {

    @Autowired private SalaryRepository salaryRepo;
    @Autowired private BizExpenseRepository bizExpenseRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private BizReportRepository bizReportRepo;

    public GeneralResponse calculateReport(final BizReport bizReport) throws ParseException {
        bizReport.setGmtModify(Utility.getCurrentDate());
        Date startDate = Utility.getFirstDateOfMonth(bizReport.getYear(),bizReport.getMonth());
        Date endDate = Utility.getLastDateOfMonth(bizReport.getYear(),bizReport.getMonth());

        // get biz expense
        List<BizExpense> expenses = bizExpenseRepo.findByClientCodeAndShopCodeAndGmtCreateBetween(
                bizReport.getClientCode(), bizReport.getShopCode(), startDate,endDate);
        int expAmount = 0;
        for(BizExpense exp : expenses){
            expAmount += exp.getAmount() != null ? exp.getAmount() : 0;
        }
        // get salary
        List<Salary>  salaries = salaryRepo.findByYearAndMonthAndClientCodeAndShopCode(bizReport.getYear(),bizReport.getMonth(),
                bizReport.getClientCode(), bizReport.getShopCode());
        for(Salary sal : salaries){
            expAmount += (sal.getAmount() != null ? sal.getAmount() : 0 ) + (sal.getBonus() != null ? sal.getBonus() : 0);
        }
        bizReport.setOutcome(expAmount);

        // get order from client.shop in a month
        List<Order> orderList = orderRepo.findByClientCodeAndShopCodeAndGmtCreateBetween(bizReport.getClientCode(),
                bizReport.getShopCode(),startDate,endDate);
        int incomeAmount = 0;
        int discountAmount = 0;
        int lensQty = 0;
        int frameQty = 0;
        int orderQty = 0;
        for(Order or : orderList){
            int amount = 0;// one order amount
            int disAmount = 0;//
            int tempDisAmount ;//
            for(OrderDetail orderDetail : or.getOrderDetails()){
                int lensPrice = orderDetail.getLensPrice() != null ? orderDetail.getLensPrice() : 0;
                int otherPrice = orderDetail.getOtherPrice() != null ? orderDetail.getOtherPrice() : 0;

                //calculate each orderDetail discount value
                tempDisAmount = orderDetail.getFramePriceAtThatTime()*orderDetail.getFrameDiscountAmount()*orderDetail.getQuantity()/100
                        + lensPrice*orderDetail.getLensDiscountAmount()*orderDetail.getLensQuantity()/100;
                disAmount += tempDisAmount;
                //calculate each orderDetail amount
                amount += orderDetail.getFramePriceAtThatTime()*orderDetail.getQuantity() + lensPrice*orderDetail.getLensQuantity() +
                        otherPrice - tempDisAmount;

                if(orderDetail.getFramePriceAtThatTime() > 1000){
                    frameQty += orderDetail.getQuantity();
                }
                if(orderDetail.getLensPrice() != null && orderDetail.getLensPrice() > 1000){
                    lensQty += orderDetail.getLensQuantity();
                }
            }
            //total discount in one order
            discountAmount += disAmount + amount*or.getCouponDiscount()/100 + or.getCustomDiscountAmount();
            //total amount in one order
            incomeAmount += amount - amount*or.getCouponDiscount()/100 - or.getCustomDiscountAmount();

            if(amount > 10000){
                orderQty += 1;
            }
        }
        bizReport.setFrameQuantity(frameQty);
        bizReport.setLensQuantity(lensQty);
        bizReport.setIncome(incomeAmount);
        bizReport.setOrderQuantity(orderQty);
        bizReport.setDiscountAmount(discountAmount);

        return new GeneralResponse(bizReportRepo.save(bizReport),Utility.SUCCESS_ERRORCODE,Utility.SUCCESS_MSG);
    }
}
