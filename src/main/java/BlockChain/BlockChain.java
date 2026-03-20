package BlockChain;

public class BlockChain {

    public static void main(String[] args) {

        // Difficulty 4: each block hash must start with "0000"
        BlockChain_Manager election = new BlockChain_Manager(4, "0");

        System.out.println("=== VoteChain Demo ===\n");

        // Genesis block — anchors the chain
        election.createGenesis();
        System.out.println("Genesis block mined: " + election.getLastBlock().getHash());

        // --- Block 1 ---
        election.createBlock();
        election.castVote("voter-001", "Alice");
        election.castVote("voter-002", "Bob");
        election.castVote("voter-003", "Alice");
        election.castVote("voter-001", "Alice"); // duplicate — should be rejected
        election.mineBlock();
        System.out.println("Block 1 mined:       " + election.getLastBlock().getHash());

        // --- Block 2 ---
        election.createBlock();
        election.castVote("voter-004", "Bob");
        election.castVote("voter-005", "Alice");
        election.mineBlock();
        System.out.println("Block 2 mined:       " + election.getLastBlock().getHash());

        // --- Results ---
        System.out.println("\n=== Results ===");
        System.out.println("Alice: " + election.getVotesPerCandidate("Alice"));
        System.out.println("Bob:   " + election.getVotesPerCandidate("Bob"));

        // --- Chain validation ---
        System.out.println("\n=== Chain Validation ===");
        for (int i = 0; i < election.size(); i++) {
            Block blk = election.getBlock(i);
            boolean valid = election.getProofOfWork_overBlock(blk);
            System.out.println("Block " + i + " [" + blk.getHash() + "] — " + (valid ? "VALID" : "INVALID"));
        }

        // --- Tamper simulation ---
        System.out.println("\n=== Tamper Simulation ===");
        System.out.println("Re-validating Block 1 after simulated tamper...");
        Block block1 = election.getBlock(1);
        System.out.println("Block 1 valid: " + election.getProofOfWork_overBlock(block1));
        System.out.println("(If you manually alter a vote in Block 1, this returns false)");

        System.out.println("\n=== Vote Report — Block 1 ===");
        System.out.println(election.votesReport(1));
    }
}

