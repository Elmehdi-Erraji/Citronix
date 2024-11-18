package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.Sale;
import com.spring.citronix.repository.HarvestRepository;
import com.spring.citronix.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaleServiceImpl {

    private final SaleRepository   saleRepository;
    private final HarvestRepository harvestRepository;

    public SaleServiceImpl(SaleRepository saleRepository, HarvestRepository harvestRepository) {
        this.saleRepository = saleRepository;
        this.harvestRepository = harvestRepository;
    }

    public Sale createSale(Sale sale) {

        System.out.println(sale.getClass());

        if (sale.getHarvest().getId() == null) {
            throw new IllegalArgumentException("Harvest cannot be null.");
        }

        // Check if the harvest exists in the repository
        Optional<Harvest> harvestOpt = harvestRepository.findById(sale.getHarvest().getId());

        if (harvestOpt.isEmpty()) {
            throw new IllegalArgumentException("Harvest not found.");
        }

        // Set the found harvest to the sale
        sale.setHarvest(harvestOpt.get());

        // Calculate and set the revenue
        sale.setRevenue(sale.calculateRevenue(sale.getQuantity()));

        // Save and return the sale
        return saleRepository.save(sale);
    }

}
