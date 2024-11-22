package com.spring.citronix.service;

import com.spring.citronix.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SalesService {
    Sale createSale(Sale sale);
    Sale updateSale(UUID saleId, Sale updatedSale);
    void deleteSale(UUID saleId);
    Sale getSale(UUID saleId);
    Page<Sale> listSale(Pageable pageable);
}
