package com.spring.citronix.service.imp;

import com.spring.citronix.domain.Harvest;
import com.spring.citronix.domain.Sale;
import com.spring.citronix.repository.SaleRepository;
import com.spring.citronix.service.HarvestService;
import com.spring.citronix.service.SalesService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class SaleServiceImpl implements SalesService {
    private final SaleRepository saleRepository;
    private final HarvestService harvestService;

    public SaleServiceImpl(SaleRepository saleRepository, @Lazy HarvestService harvestService) {
        this.saleRepository = saleRepository;
        this.harvestService = harvestService;
    }
    @Override
    public Sale createSale(Sale sale) {
        Harvest harvest = harvestService.getHarvestById(sale.getHarvest().getId());

        validateHarvestQuantity(harvest, sale.getQuantity());
        sale.setRevenue(sale.calculateRevenue(sale.getQuantity()));
        return saleRepository.save(sale);
    }

    @Override
    public Sale updateSale(UUID saleId, Sale updatedSale) {
        Sale existingSale = getSale(saleId);
        Harvest harvest = harvestService.getHarvestById(updatedSale.getHarvest().getId());
        validateHarvestQuantity(harvest, updatedSale.getQuantity());
        updatedSale.setRevenue(updatedSale.calculateRevenue(updatedSale.getQuantity()));

        existingSale.setQuantity(updatedSale.getQuantity());
        existingSale.setUnitPrice(updatedSale.getUnitPrice());
        existingSale.setClient(updatedSale.getClient());
        existingSale.setDate(updatedSale.getDate());
        existingSale.setHarvest(harvest);
        existingSale.setRevenue(updatedSale.getRevenue());

        return saleRepository.save(existingSale);
    }

    @Override
    public void deleteSale(UUID saleId) {
        Sale sale = getSale(saleId);
        saleRepository.delete(sale);
    }

    @Override
    public Sale getSale(UUID saleId) {
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found"));
    }

    @Override
    public Page<Sale> listSale(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    private void validateHarvestQuantity(Harvest harvest, double saleQuantity) {
        double totalSoldQuantity = harvest.getSales().stream()
                .mapToDouble(Sale::getQuantity)
                .sum();

        if (totalSoldQuantity + saleQuantity > harvest.getTotalQuantity()) {
            throw new IllegalArgumentException("Insufficient harvest quantity");
        }
    }
}
