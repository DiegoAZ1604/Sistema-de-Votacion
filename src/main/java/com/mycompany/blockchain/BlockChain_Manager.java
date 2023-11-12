package com.mycompany.blockchain;

/**
 *
 * @author diegod
 */
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class BlockChain_Manager {
    private ArrayList<Block> blockchain;
    private int complexity;
    private String proofOfWork;
    
    public BlockChain_Manager(int iComplexity, String proofChar){
        this.blockchain = new ArrayList<>();
        this.complexity = iComplexity;
        this.proofOfWork = "";
        for (int i = 0; i<this.complexity; i++) this.proofOfWork += proofChar;
    }

    public List<Block> getBlockchain() {
        return this.blockchain;
    }
    
    public boolean blockExist(Block blk){
        for (int i = 0; i < this.blockchain.size(); i++) {
            if(this.blockchain.get(i).getId()==blk.getId()) return true;
        }
        return false;
    }
    
    public Block getBlock(int index){
        return this.blockchain.get(index);
    }
    
    public Block getLastBlock(){
        return this.blockchain.get(this.blockchain.size()-1);
    }
    
    public int size(){
        return this.blockchain.size();
    }
    
//    public boolean createGenesis(double pInitialBalance, String pClient){
//        if (this.size()<1) {
//            Block tmpBlock = new Block(0, "000000000000000000000000000000000000000000000000000000");
//            if (pInitialBalance > 0) tmpBlock.setTransaction("0000GeNeSiS", pInitialBalance, pClient);                
//            
//        }
//    }   
}
