package com.mycompany.blockchain;

/**
 *
 * @author diegod
 */
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

//Declaración de variables
    public class BlockChain_Manager {
    private ArrayList<Block> blockchain;
    private int complexity;
    private String proofOfWork;
    
    //Constructor
    public BlockChain_Manager(int iComplexity, String proofChar){
        this.blockchain = new ArrayList<>();
        this.complexity = iComplexity;
        this.proofOfWork = "";
        for (int i = 0; i<this.complexity; i++) this.proofOfWork += proofChar;
    }
    
    //Función que retorna la cadena de bloques
    public List<Block> getBlockchain() {
        return this.blockchain;
    }
    
    //Función que retorna true si el bloque existe
    public boolean blockExist(Block blk){
        for (int i = 0; i < this.blockchain.size(); i++) {
            if(this.blockchain.get(i).getId()==blk.getId()) return true;
        }
        return false;
    }
    
    //Función que retorna bloques
    public Block getBlock(int index){
        return this.blockchain.get(index);
    }
    
    //Función que retorna el último bloque
    public Block getLastBlock(){
        return this.blockchain.get(this.blockchain.size()-1);
    }
    
    //Función que retorna la cantidad de bloques
    public int size(){
        return this.blockchain.size();
    }
    
    public boolean createGenesis(String pVoter, String pCandidate){
        if (this.size()<1) {
            Block tmpBlock = new Block(0, "000000000000000000000000000000000000000000000000000000");
            if (pVoter != null) tmpBlock.setVote("0000GeNeSiS", pCandidate);                 this.blockchain.add(tmpBlock);
            this.mineBlock();
            return true;
        }
        return false;
    }
    
    public boolean createGenesis(){
        if(this.size()<1){
            Block tmpBlock = new Block(0, "000000000000000000000000000000000000000000000000000000");
            this.mineBlock();
            return true;
        }
        return false;
    }
    
    public void createBlock(){
        String prevHash = this.blockchain.get(this.blockchain.size()-1).getHash();
        this.blockchain.add(new Block(this.blockchain.size(), prevHash));
    }
    
//    public double getVotesPerCandidate(String pCandidate){
//        double positiveAmount = 0;
//        for (int i = 0; i < this.size(); i++) {
//            for (int j = 0; j < this.getBlock(i).countVotes(); j++) {
//                
//            }
//        }
//    }
    
    public boolean getProofOfWork_overBlock(Block blk){
        String cad = blk.toString();
        int nonce = blk.getNonce();
        String sHash = "";
        //sHash = this.generateHash(cad + Integer.toString(nonce));
        if(sHash.equals(blk.getHash())) return true;
        else return false;
    }
    
    public boolean addProvedBlock(Block blk){
        if(!this.blockExist(blk)){
            if (this.getProofOfWork_overBlock(blk)) {
                this.blockchain.add(blk);
                return true;
            }
        }
        return false;
    }
    
    public void mineBlock(){
        String cad = this.blockchain.get(this.blockchain.size()-1).toString();
        int nonce = 0;
        String sHash = "";
        while(true){
            sHash = this.generateHash(cad + Integer.toString(nonce));
            if (sHash.subSequence(0, complexity).equals(this.proofOfWork)) {
                this.blockchain.get(this.blockchain.size()-1).register(nonce, sHash);
                break;
            }
            nonce++;
        }
    }
    
    private String generateHash(String pCad){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pCad.getBytes("UTF-8"));
            StringBuffer hexadecimalString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hexadecimal = Integer.toHexString(0xff & hash[i]);
                if (hexadecimal.length()==1) hexadecimalString.append('0');
                hexadecimalString.append(hexadecimal);
            }
            return hexadecimalString.toString();
        } catch (Exception ee){
            System.out.println("Error generando hash");
            return null;
        }
    }
    
//    public String votesReport(int nBlock){
//        String sCad = "";
//        Block blk = this.blockchain.get(nBlock);
//        for (int i = 0; i < blk.countVotes(); i++) {
//            //sCad += "'\t Transacción #" + Integer.
//        }
//    }
    
    @Override
    public String toString(){
        String blockChain = "";
        for(Block block : this.blockchain) blockChain += block.toString() + "\n";
        return blockChain;
    }
}
