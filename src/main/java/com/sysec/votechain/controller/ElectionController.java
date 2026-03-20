package com.sysec.votechain.controller;

import com.sysec.votechain.blockchain.Block;
import com.sysec.votechain.service.ElectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST endpoints for the VoteChain election system.
 * @author Diego
 */
@RestController
@RequestMapping("/api/election")
public class ElectionController {

    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    // Cast a vote — POST /api/election/vote
    // Body: { "voterId": "voter-001", "candidateId": "Alice" }
    @PostMapping("/vote")
    public ResponseEntity<Map<String, Object>> castVote(@RequestBody Map<String, String> body) {
        String voterId = body.get("voterId");
        String candidateId = body.get("candidateId");

        if (voterId == null || candidateId == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "voterId and candidateId are required"
            ));
        }

        boolean accepted = electionService.castVote(voterId, candidateId);
        return ResponseEntity.ok(Map.of(
            "success", accepted,
            "message", accepted ? "Vote recorded" : "Voter has already voted"
        ));
    }

    // Get vote count for a candidate — GET /api/election/results/{candidateId}
    @GetMapping("/results/{candidateId}")
    public ResponseEntity<Map<String, Object>> getResults(@PathVariable String candidateId) {
        int votes = electionService.getVotesForCandidate(candidateId);
        return ResponseEntity.ok(Map.of(
            "candidate", candidateId,
            "votes", votes
        ));
    }

    // Get full blockchain — GET /api/election/blockchain
    @GetMapping("/blockchain")
    public ResponseEntity<List<Block>> getBlockchain() {
        return ResponseEntity.ok(electionService.getBlockchain());
    }

    // Validate chain integrity — GET /api/election/validate
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate() {
        boolean valid = electionService.validateChain();
        return ResponseEntity.ok(Map.of(
            "valid", valid,
            "message", valid ? "Chain is intact" : "Chain integrity compromised"
        ));
    }

    // Seal current block and open next — POST /api/election/seal
    @PostMapping("/seal")
    public ResponseEntity<Map<String, Object>> sealBlock() {
        electionService.sealCurrentBlock();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Block sealed and new block opened"
        ));
    }
}
