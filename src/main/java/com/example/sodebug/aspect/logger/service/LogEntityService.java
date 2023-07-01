package com.example.sodebug.aspect.logger.service;


import com.example.sodebug.web.dto.AbstractResponseDTO;
import com.example.sodebug.aspect.logger.model.LogEntity;

/**
 * The {@code LogEntityService} interface defines operations for managing {@link LogEntity} objects.
 *
 * @author ogbozoyan
 * @date 11.04.2023
 */
public interface LogEntityService {
    LogEntity save(LogEntity entity);

    AbstractResponseDTO findAll();
}
