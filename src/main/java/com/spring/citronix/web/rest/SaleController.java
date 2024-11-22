package com.spring.citronix.web.rest;

import com.spring.citronix.domain.Sale;
import com.spring.citronix.service.SalesService;
import com.spring.citronix.web.mapper.request.SaleMapper;
import com.spring.citronix.web.vm.response.sale.SaleResponseVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SalesService salesService;
    private final SaleMapper saleMapper;

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        Sale createdSale = salesService.createSale(sale);
        return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
    }

    @PutMapping("/update/{saleId}")
    public ResponseEntity<Sale> updateSale(@PathVariable UUID saleId, @RequestBody Sale updatedSale) {
        Sale sale = salesService.updateSale(saleId, updatedSale);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{saleId}")
    public ResponseEntity<String> deleteSale(@PathVariable UUID saleId) {
        salesService.deleteSale(saleId);
        return ResponseEntity.ok("deleted successfully");
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponseVM> getSale(@PathVariable UUID saleId) {
        Sale sale = salesService.getSale(saleId);
        SaleResponseVM saleResponseVM = saleMapper.toSaleResponseVM(sale);
        return ResponseEntity.ok(saleResponseVM);
    }

    @GetMapping
    public ResponseEntity<Page<Sale>> listSales(Pageable pageable) {
        Page<Sale> sales = salesService.listSale(pageable);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }
}
