package com.sysec.votechain.repository;

import com.sysec.votechain.model.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Diego
 */
public interface VoteJpaRepository extends JpaRepository<VoteEntity, Long> {
}
