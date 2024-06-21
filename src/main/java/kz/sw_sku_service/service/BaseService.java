package kz.sw_sku_service.service;

import kz.sw_sku_service.exception.CustomException;
import kz.sw_sku_service.model.dto.SearchDTO;
import kz.sw_sku_service.model.dto.response.DeleteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface BaseService<D, E> {

    @Transactional(rollbackFor = CustomException.class)
    D saveOne(D d) throws CustomException;

    D getOne(Long id) throws CustomException;

    @Transactional(rollbackFor = CustomException.class)
    D updateOne(Long id, D d) throws CustomException;

    @Transactional(rollbackFor = CustomException.class)
    DeleteResponseDTO deleteOne(Long id) throws CustomException;

    Page<D> search(SearchDTO searchDTO, Pageable pageable);

    E findById(Long id) throws CustomException;
}
