package com.example.springboot.dto;

import com.example.springboot.entity.Setmeal;
import com.example.springboot.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
