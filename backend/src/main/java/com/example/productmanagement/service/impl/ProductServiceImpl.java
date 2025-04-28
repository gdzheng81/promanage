package com.example.productmanagement.service.impl;

import com.example.productmanagement.mapper.ProductMapper;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    @Override
    public void addProduct(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void updateProduct(Long id, Product product) {
        product.setId(id);
        productMapper.update(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.delete(id);
    }
}
