package com.sysec.votechain.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity for persisting blockchain blocks.
 * @author Diego
 */
@Entity
@Table(name = "blocks")
public class BlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int blockIndex;

    @Column(nullable = false)
    private int nonce;

    @Column(nullable = false)
    private long timeStamp;

    @Column(nullable = false, length = 64)
    private String hash;

    @Column(nullable = false, length = 64)
    private String previousHash;

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VoteEntity> votes = new ArrayList<>();

    public BlockEntity() {}

    public BlockEntity(int blockIndex, int nonce, long timeStamp, String hash, String previousHash) {
        this.blockIndex = blockIndex;
        this.nonce = nonce;
        this.timeStamp = timeStamp;
        this.hash = hash;
        this.previousHash = previousHash;
    }

    public int getBlockIndex() { return blockIndex; }
    public int getNonce() { return nonce; }
    public long getTimeStamp() { return timeStamp; }
    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
    public List<VoteEntity> getVotes() { return votes; }
}
