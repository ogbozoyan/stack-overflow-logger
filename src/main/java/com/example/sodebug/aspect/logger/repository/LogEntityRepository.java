package com.example.sodebug.aspect.logger.repository;

import com.example.sodebug.aspect.logger.model.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * The {@code LogEntityRepository} interface provides CRUD operations for {@link LogEntity} objects in the database.
 *
 * <p>It is annotated with {@code @Repository} to indicate that it is a repository component.</p>
 *
 * @author ogbozoyan
 * @date 12.04.2023
 */
@Repository
public interface LogEntityRepository extends JpaRepository<LogEntity, Long>, JpaSpecificationExecutor<LogEntity> {
    /**
     * Saves a {@link LogEntity} object in the database.
     *
     * <p>It is annotated with {@code @Lock} to specify the lock mode for concurrent access.</p>
     *
     * @param entity the log entity to be saved
     * @return the saved log entity
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    LogEntity save(LogEntity entity);

    /**
     * Returns a {@link Page} of entities matching the given {@link Specification}.
     *
     * @param spec     can be {@literal null}.
     * @param pageable must not be {@literal null}.
     * @return never {@literal null}.
     */
    @Override
    Page<LogEntity> findAll(Specification<LogEntity> spec, Pageable pageable);
}
