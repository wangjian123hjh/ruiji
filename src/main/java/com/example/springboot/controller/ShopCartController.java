package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springboot.common.BaseContext;
import com.example.springboot.common.R;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.service.ShopCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShopCartController {
    @Autowired
    private ShopCartService shopCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置用户id，指定当前哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        shoppingCart.setCreateTime(LocalDateTime.now());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        //查询当前菜品或者套餐是否在购物车中，如果在，数量+1（存在bug，可能口味不同）
        Long dishId = shoppingCart.getDishId();
        if (dishId!=null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shopCartService.getOne(queryWrapper);
        if (one!=null){
            //菜品或套餐已存在，+1
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shopCartService.updateById(one);
        }else{
            //不存在，新增
            shoppingCart.setNumber(1);
            shopCartService.save(shoppingCart);
            one=shoppingCart;
        }
        return R.success(one);
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shopCartService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        //设置用户id，指定当前哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        //查询当前菜品或者套餐是否在购物车中，如果在，数量+1（存在bug，可能口味不同）
        Long dishId = shoppingCart.getDishId();
        if (dishId!=null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shopCartService.getOne(queryWrapper);
        if (one.getNumber()==1){
            shopCartService.removeById(one);
        }else{
            //不存在，新增
            one.setNumber(one.getNumber()-1);
            shopCartService.updateById(one);
        }
        return R.success(one);
    }
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shopCartService.remove(queryWrapper);
        return R.success("清空购物车成功！");
    }
}
