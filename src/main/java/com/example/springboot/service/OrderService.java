package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Orders;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
