package com.sysec.votechain.service;

import com.sysec.votechain.blockchain.Block;
import com.sysec.votechain.blockchain.BlockChain_Manager;
import com.sysec.votechain.blockchain.Vote;
import com.sysec.votechain.model.BlockEntity;
import com.sysec.votechain.model.VoteEntity;
import com.sysec.votechain.repository.BlockJpaRepository;
import com.sysec.votechain.repository.VoteJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Manages the lifecycle of a single election backed by a blockchain.
 * Persists mined blocks to PostgreSQL and reconstructs the chain on startup.
 * @author Diego
 */
@Service
public class ElectionService {

    private static final Logger log = LoggerFactory.getLogger(ElectionService.class);

    private final BlockChain_Manager chain;
    private final BlockJpaRepository blockRepo;
    private final VoteJpaRepository voteRepo;

    public ElectionService(BlockJpaRepository blockRepo,
        VoteJpaRepository voteRepo,
        @Value("${blockchain.difficulty}") int difficulty,
        @Value("${blockchain.proof-char}") String proofChar) {
        this.blockRepo = blockRepo;
        this.voteRepo = voteRepo;
        this.chain = new BlockChain_Manager(difficulty, proofChar);
        initChain();
    }

    // On startup: load persisted blocks or create genesis
    private void initChain() {
        List<BlockEntity> persisted = blockRepo.findAllByOrderByBlockIndexAsc();

        if (persisted.isEmpty()) {
            log.info("No persisted chain found. Mining genesis block...");
            chain.createGenesis();
            persistBlock(chain.getLastBlock());
            chain.createBlock();
            log.info("Genesis block mined: {}", chain.getBlock(0).getHash());
        } else {
            log.info("Loading {} block(s) from database...", persisted.size());
            reconstructChain(persisted);
            log.info("Chain restored. Last block: {}", chain.getLastBlock().getHash());
            // Open a fresh block for incoming votes
            chain.createBlock();
        }
    }

    // Rebuild BlockChain_Manager from DB records
    private void reconstructChain(List<BlockEntity> persisted) {
        for (BlockEntity be : persisted) {
            Block block = new Block(
                    be.getBlockIndex(),
                    be.getPreviousHash(),
                    be.getTimeStamp(),
                    be.getNonce(),
                    be.getHash()
            );
            for (VoteEntity ve : be.getVotes()) {
                block.addVote(new Vote(
                        ve.getVoteIndex(),
                        ve.getVoter(),
                        ve.getCandidate(),
                        ve.getTimeStamp()
                ));
            }
            chain.loadBlock(block);
        }
    }

    // Persist a mined block and its votes to PostgreSQL
    private void persistBlock(Block block) {
        BlockEntity be = new BlockEntity(
                block.getId(),
                block.getNonce(),
                block.getTimeStamp(),
                block.getHash(),
                block.getPreviousHash()
        );
        blockRepo.save(be);

        for (int i = 0; i < block.countVotes(); i++) {
            var vote = block.getVote(i);
            voteRepo.save(new VoteEntity(
                    vote.getId(),
                    vote.getTimeStamp(),
                    vote.getVoter(),
                    vote.getCandidate(),
                    be
            ));
        }
    }

    public boolean castVote(String voterId, String candidateId) {
        return chain.castVote(voterId, candidateId);
    }

    public int getVotesForCandidate(String candidateId) {
        return chain.getVotesPerCandidate(candidateId);
    }

    public List<Block> getBlockchain() {
        return chain.getBlockchain();
    }

    public boolean validateChain() {
        for (int i = 0; i < chain.size(); i++) {
            if (!chain.getProofOfWork_overBlock(chain.getBlock(i))) return false;
        }
        return true;
    }

    public void sealCurrentBlock() {
        chain.mineBlock();
        Block mined = chain.getLastBlock();
        persistBlock(mined);
        log.info("Block #{} sealed and persisted: {}", mined.getId(), mined.getHash());
        chain.createBlock();
    }
}
