package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.BaseContext;
import com.example.springboot.common.R;
import com.example.springboot.entity.OrderDetail;
import com.example.springboot.entity.Orders;
import com.example.springboot.service.OrderDetailService;
import com.example.springboot.service.OrderService;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("提交成功！");
    }
    @GetMapping("/userPage")
    public R<Page> page(@RequestParam int page,int pageSize){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId,currentId);
        List<Orders> list = orderService.list(lambdaQueryWrapper);
        List<Long> longs = new ArrayList<>();
        list.forEach((item)->{
            longs.add(item.getId());
        });
        Page<OrderDetail> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OrderDetail::getOrderId,longs);
        orderDetailService.page(page1,queryWrapper);
        return R.success(page1);
    }
}
