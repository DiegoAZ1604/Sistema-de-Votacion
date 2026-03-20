package com.sysec.votechain.model;

import jakarta.persistence.*;

/**
 * JPA entity for persisting individual votes.
 * @author Diego
 */
@Entity
@Table(name = "votes")
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int voteIndex;

    @Column(nullable = false)
    private long timeStamp;

    @Column(nullable = false)
    private String voter;

    @Column(nullable = false)
    private String candidate;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private BlockEntity block;

    public VoteEntity() {}

    public VoteEntity(int voteIndex, long timeStamp, String voter, String candidate, BlockEntity block) {
        this.voteIndex = voteIndex;
        this.timeStamp = timeStamp;
        this.voter = voter;
        this.candidate = candidate;
        this.block = block;
    }

    public int getVoteIndex() { return voteIndex; }
    public long getTimeStamp() { return timeStamp; }
    public String getVoter() { return voter; }
    public String getCandidate() { return candidate; }
}
