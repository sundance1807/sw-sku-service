package kz.sw_sku_service.repository;

import kz.sw_sku_service.model.entity.SkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkuRepository extends JpaRepository<SkuEntity, Long>,
        JpaSpecificationExecutor<SkuEntity> {

    Optional<SkuEntity> findByNameIgnoreCase(String skuName);
}