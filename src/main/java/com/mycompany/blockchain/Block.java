package com.mycompany.blockchain;

/**
 *
 * @author diegod
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.time.Instant;

public class Block implements Serializable{
    private int id;
    private int nonce;
    private long timeStamp;
    private String hash;
    private String previousHash;
    private ArrayList<Vote> votes;
    private String electionName;
    
    public Block(int pId, String pPrevHash){
        this.id = pId;
        this.timeStamp = Instant.now().toEpochMilli();
        this.previousHash = pPrevHash;
        this.votes = new ArrayList<>();
        this.nonce  = -1;
        this.hash = null;
    }
    
    public Block(){
        this.timeStamp = Instant.now().toEpochMilli();
        this.votes = new ArrayList<>();
        this.nonce = -1;
        this.hash = null;
        this.id = -1;
    }
    
    public boolean register(int pNonce, String pHash){
        if ((this.getId()>-1)&&(this.getNonce()<0)&&(this.getHash()==null)) {
            this.nonce = pNonce;
            this.hash = pHash;
            return true;
        }
        else return false;
    }
    
    public void setVote(String pVoter, String pCandidate) {
        this.votes.add(new Vote(this.votes.size(), pVoter, pCandidate));
    }

    public void setVote(Vote pVote) {
        this.votes.add(new Vote(
                this.votes.size(), pVote.getVoter(), pVote.getCandidate()
        ));
    }

    public Vote getVote(int pId){
        return this.votes.get(pId);
    }
    
    public int countVotes(){
        return this.votes.size();
    } 

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the nonce
     */
    public int getNonce() {
        return nonce;
    }

    /**
     * @return the timeStamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return the previousHash
     */
    public String getPreviousHash() {
        return previousHash;
    }
    
    
}   
