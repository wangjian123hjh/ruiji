package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.mapper.ShopCartMapper;
import com.example.springboot.service.ShopCartService;
import org.springframework.stereotype.Service;

@Service
public class ShopCartServiceImpl extends ServiceImpl<ShopCartMapper,ShoppingCart> implements ShopCartService {
}
