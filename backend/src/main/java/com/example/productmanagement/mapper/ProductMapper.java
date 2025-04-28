package com.example.productmanagement.mapper;

import com.example.productmanagement.model.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product_master")
    List<Product> findAll();

    @Insert("INSERT INTO product_master (code, name, category_id, spec, unit, supplier_id, status, version, create_time) VALUES (#{code}, #{name}, #{categoryId}, #{spec}, #{unit}, #{supplierId}, #{status}, #{version}, #{createTime})")
    void insert(Product product);

    @Update("UPDATE product_master SET code=#{code}, name=#{name}, category_id=#{categoryId}, spec=#{spec}, unit=#{unit}, supplier_id=#{supplierId}, status=#{status}, version=#{version}, create_time=#{createTime} WHERE id=#{id}")
    void update(Product product);

    @Delete("DELETE FROM product_master WHERE id=#{id}")
    void delete(Long id);
}
```
