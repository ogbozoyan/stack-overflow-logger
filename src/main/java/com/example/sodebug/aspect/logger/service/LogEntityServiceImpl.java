package com.example.sodebug.aspect.logger.service;

import com.example.sodebug.web.dto.AbstractResponseDTO;
import com.example.sodebug.aspect.logger.model.LogEntity;
import com.example.sodebug.aspect.logger.repository.LogEntityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ogbozoyan
 * @date 07.04.2023
 */
@Service
public class LogEntityServiceImpl implements LogEntityService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper patchingMapper;
    @Autowired
    private LogEntityRepository repository;


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW, timeout = 5)
    @Retryable(retryFor = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    public LogEntity save(LogEntity entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    @Transactional(timeout = 200, readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public AbstractResponseDTO findAll() {
        try {
            return new AbstractResponseDTO(null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
