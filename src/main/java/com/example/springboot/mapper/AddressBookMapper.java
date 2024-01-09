package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.springboot.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
