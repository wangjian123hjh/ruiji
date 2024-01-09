package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.CustomException;
import com.example.springboot.entity.Category;
import com.example.springboot.entity.Dish;
import com.example.springboot.entity.Setmeal;
import com.example.springboot.mapper.CategoryMapper;
import com.example.springboot.service.CategoryService;
import com.example.springboot.service.DishService;
import com.example.springboot.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        long count = dishService.count(dishLambdaQueryWrapper);
        if (count>0){
            throw new CustomException("当前分类下已关联菜品");
        }
        //查询当前分类是否关联了菜品，关联了则抛出业务异常

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        long num = setmealService.count(lambdaQueryWrapper);
        if (num>0){
            throw new CustomException("当前分类下已关联套餐");
        }
        //查询当前分类是否关联了套餐，关联了则抛出业务异常

        super.removeById(id);

        //正常删除

    }
}
