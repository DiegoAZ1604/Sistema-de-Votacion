package com.sysec.votechain.repository;

import com.sysec.votechain.model.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Diego
 */
public interface BlockJpaRepository extends JpaRepository<BlockEntity, Long> {
    List<BlockEntity> findAllByOrderByBlockIndexAsc();
}
