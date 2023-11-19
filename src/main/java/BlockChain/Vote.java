package BlockChain;

import java.io.Serializable;
import java.time.Instant;

/**
 *
 * @author diegod
 */

//Declaración de Variables
    public class Vote implements Serializable {

    private final int id;
    private final long timeStamp;
    private final String voter;
    private final String candidate;

    public Vote(int pId, String pVoter, String pCandidate) {
        this.id = pId;
        this.timeStamp = Instant.now().toEpochMilli();
        this.voter = pVoter;
        this.candidate = pCandidate;
    }

    @Override
    public String toString() {
        return Integer.toString(getId()) + Long.toString(getTimeStamp()) + voter + candidate;
    }

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getVoter() {
        return voter;
    }

    public String getCandidate() {
        return candidate;
    }
}
