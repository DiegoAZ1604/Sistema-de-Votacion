package BlockChain;

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
    
    public boolean createGenesis(String pVoter, String pCandidate) {
        if (this.size() < 1) {
            Block tmpBlock = new Block(0, "000000000000000000000000000000000000000000000000000000");
            if (pVoter != null) {
                tmpBlock.setVote("0000GeNeSiS", pCandidate);
            }
            this.blockchain.add(tmpBlock);
            this.mineBlock();
            return true;
        }
        return false;
    }
    
    public boolean createGenesis() {
        if (this.size() < 1) {
            Block tmpBlock = new Block(0, "000000000000000000000000000000000000000000000000000000");
            this.mineBlock();
            return true;
        }
        return false;
    }
    
   public void createBlock() {
        String prevHash = this.blockchain.get(this.blockchain.size() - 1).getHash();
        this.blockchain.add(new Block(this.blockchain.size(), prevHash));
    }

    
    public double getVotesPerCandidate(String pCandidate) {
        double positiveAmount = 0;

        for (int i = 0; i < this.size(); i++) {
            Block block = this.getBlock(i);

            for (int j = 0; j < block.countVotes(); j++) {
                Vote vote = block.getVote(j);

                if (vote.getCandidate().equals(pCandidate)) {
                    positiveAmount++;
                }
            }
        }

        return positiveAmount;
    }

    public boolean getProofOfWork_overBlock(Block blk) {
        String cad = blk.toString();
        int nonce = blk.getNonce();
        String sHash = this.generateHash(cad + Integer.toString(nonce));

        return sHash.equals(blk.getHash());
    }
    
    public boolean addProvedBlock(Block blk) {
        if (!this.blockExist(blk)) {
            if (this.getProofOfWork_overBlock(blk)) {
                this.blockchain.add(blk);
                return true;
            }
        }
        return false;
    }
    
    public void mineBlock() {
        String cad = this.blockchain.get(this.blockchain.size() - 1).toString();
        int nonce = 0;
        String sHash;

        while (true) {
            sHash = this.generateHash(cad + Integer.toString(nonce));
            if (sHash.substring(0, complexity).equals(this.proofOfWork)) {
                this.blockchain.get(this.blockchain.size() - 1).register(nonce, sHash);
                break;
            }
            nonce++;
        }
    }
    
    private String generateHash(String pCad) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pCad.getBytes("UTF-8"));
            StringBuilder hexadecimalString = new StringBuilder();
            for (byte b : hash) {
                String hexadecimal = Integer.toHexString(0xff & b);
                if (hexadecimal.length() == 1) hexadecimalString.append('0');
                hexadecimalString.append(hexadecimal);
            }
            return hexadecimalString.toString();
        } catch (Exception ee) {
            System.out.println("Error generando hash");
            return null;
        }
    }
    
   public String votesReport(int nBlock) {
    StringBuilder sCad = new StringBuilder();
    Block blk = this.blockchain.get(nBlock);

    sCad.append("Votes Report for Block ").append(nBlock).append(":\n");

    for (int i = 0; i < blk.countVotes(); i++) {
        Vote vote = blk.getVote(i);
        sCad.append("\tTransaction #").append(vote.getId());
        sCad.append("\tVoter: ").append(vote.getVoter());
        sCad.append("\tCandidate: ").append(vote.getCandidate()).append("\n");
    }

    return sCad.toString();
}

    
    @Override
    public String toString() {
        StringBuilder blockChain = new StringBuilder();
        for (Block block : this.blockchain) blockChain.append(block.toString()).append("\n");
        return blockChain.toString();
    }
}
