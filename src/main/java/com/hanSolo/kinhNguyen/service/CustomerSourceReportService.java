package com.hanSolo.kinhNguyen.service;

import com.hanSolo.kinhNguyen.cacheCenter.CommonCache;
import com.hanSolo.kinhNguyen.models.Client;
import com.hanSolo.kinhNguyen.models.CustomerSource;
import com.hanSolo.kinhNguyen.models.CustomerSourceReport;
import com.hanSolo.kinhNguyen.models.DateTimeContainer;
import com.hanSolo.kinhNguyen.models.DatetimeWrapper;
import com.hanSolo.kinhNguyen.models.Order;
import com.hanSolo.kinhNguyen.repository.CustomerSourceReportRepository;
import com.hanSolo.kinhNguyen.repository.CustomerSourceRepository;
import com.hanSolo.kinhNguyen.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerSourceReportService {

    @Autowired private CustomerSourceRepository customerSourceRepo;
    @Autowired private CustomerSourceReportRepository customerSourceReportRepo;
    @Autowired private OrderRepository orderRepo;

    /**
     * for schedule task
     * @param dtWrapper
     * @return
     * @throws ParseException
     */
    public void calCustomerSourceReportForScheduleTask(DatetimeWrapper dtWrapper)  {
        List<CustomerSourceReport> csrList = customerSourceReportRepo.findByYearAndMonth(dtWrapper.getYearMonthStrList());
        List<Order> orderList = orderRepo.findByGmtCreateBetweenAndCusSourceNotNull(dtWrapper.getStartTime(), dtWrapper.getEndTime());
        Calendar calendar = Calendar.getInstance();

        for(CustomerSourceReport csr : csrList){
            List<Order> oneCSOrderList = orderList.stream().filter(o ->{
                        calendar.setTime(o.getGmtCreate());
                        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                        month = month.length() > 1 ? month : "0" + month;
                        return   o.getCusSource() == csr.getCustomerSourceId()
                                && o.getClientCode().equalsIgnoreCase(csr.getClientCode())
                                && o.getShopCode().equalsIgnoreCase(csr.getShopCode())
                                && month.equalsIgnoreCase(csr.getMonth())
                                && String.valueOf(calendar.get(Calendar.YEAR)).equalsIgnoreCase(csr.getYear());
                    }

                    ).collect(Collectors.toList());
            csr.setCount(oneCSOrderList.size());
        }
        customerSourceReportRepo.saveAll(csrList);
    }
}
