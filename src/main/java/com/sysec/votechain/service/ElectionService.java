package com.sysec.votechain.service;

import com.sysec.votechain.blockchain.Block;
import com.sysec.votechain.blockchain.BlockChain_Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Manages the lifecycle of a single election backed by a blockchain.
 * @author Diego
 */
@Service
public class ElectionService {

    private static final Logger log = LoggerFactory.getLogger(ElectionService.class);

    private final BlockChain_Manager chain = new BlockChain_Manager(4, "0");

    public ElectionService() {
        log.info("Initializing blockchain...");
        chain.createGenesis();
        log.info("Genesis block mined: {}", chain.getLastBlock().getHash());
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
        chain.createBlock();
        log.info("Block sealed. New block #{} ready.", chain.size() - 1);
    }
}
