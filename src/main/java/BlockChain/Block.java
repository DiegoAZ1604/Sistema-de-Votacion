package BlockChain;

/**
 *
 * @author diegod
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.time.Instant;

//Declaración de Variables
    public class Block implements Serializable{
    private int id;
    private int nonce;
    private long timeStamp;
    private String hash;
    private String previousHash;
    private ArrayList<Vote> votes;
    //private String electionName;
    
    //Constructor con parámetros
    public Block(int pId, String pPrevHash){
        this.id = pId;
        this.timeStamp = Instant.now().toEpochMilli();
        this.previousHash = pPrevHash;
        this.votes = new ArrayList<>();
        this.nonce  = -1;
        this.hash = null;
    }
    
    //Constructor sin parámetros
    public Block(){
        this.timeStamp = Instant.now().toEpochMilli();
        this.votes = new ArrayList<>();
        this.nonce = -1;
        this.hash = null;
        this.id = -1;
    }
    
    //Función registrar que toma nonce y hash como parámetros, devuelve true si el ID es mayor que -1 y si el hash es nulo
    public boolean register(int pNonce, String pHash){
        if ((this.getId()>-1)&&(this.getNonce()<0)&&(this.getHash()==null)) {
            this.nonce = pNonce;
            this.hash = pHash;
            return true;
        }
        else 
            return false;
    }
    
    //Función que toma como parámetros el votante y el candidato y los agrega al arraylist
    public void setVote(String pVoter, String pCandidate) {
        this.votes.add(new Vote(this.votes.size(), pVoter, pCandidate));
    }

    //Función que toma como parámetros el objeto Vote y lo agrega al arrylist
    public void setVote(Vote pVote) {
        this.votes.add(new Vote(
                this.votes.size(), pVote.getVoter(), pVote.getCandidate()
        ));
    }
    
    //Función que retorna los IDs de los objetos Vote
    public Vote getVote(int pId){
        return this.votes.get(pId);
    }
    
    //Función que retorna la cantidad de votos en el array
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
    
//    @Override
//    public String toString(){
//        String sCad = Integer.toString(id) + Long.toString(timeStamp) + this.previousHash;
//        for (int i = 0; i < this.votes.size(); i++) {
//            sCad = sCad + this.votes.get(i).toString();
//        }
//        return sCad;
//    }
    
    @Override
    public String toString() {
        StringBuilder sCad = new StringBuilder(Integer.toString(id) + Long.toString(timeStamp) + this.previousHash);
        for (int i = 0; i < this.votes.size(); i++) {
            sCad.append(this.votes.get(i).toString());
        }
        return sCad.toString();
    }
}   
