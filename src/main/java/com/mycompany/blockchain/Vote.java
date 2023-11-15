package com.mycompany.blockchain;

import java.io.Serializable;
import java.time.Instant;

/**
 *
 * @author diegod
 */

//Declaración de Variables
    public class Vote implements Serializable{
    private final int id;
    private final long timeStamp;
    private final String voter;
    private final String candidate;
    
    //Constructor
    public Vote(int pId, String pVoter, String pCandidate) {
        this.id = pId;
        this.timeStamp = Instant.now().toEpochMilli();
        this.voter = pVoter;
        this.candidate = pCandidate;
    }
    
    
    //Método To String para dar formato
    @Override
    public String toString(){
        return Integer.toString(getId())+Long.toString(getTimeStamp())+voter+candidate;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the timeStamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * @return the voter
     */
    public String getVoter() {
        return voter;
    }

    /**
     * @return the candidate
     */
    public String getCandidate() {
        return candidate;
    }
}
