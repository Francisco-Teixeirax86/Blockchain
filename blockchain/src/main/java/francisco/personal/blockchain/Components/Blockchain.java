package francisco.personal.blockchain.Components;

import francisco.personal.blockchain.entities.Block;
import francisco.personal.blockchain.entities.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Blockchain {
    private List<Block> blockchain;
    private List<Transaction> pendingTransactions;


    public Blockchain() {
        this.blockchain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();

        createGenesisBlock();
    }

    private void createGenesisBlock() {
        Block genesis = new Block("0", new ArrayList<>(), 0, 0);
        blockchain.add(genesis);
    }

    public void addBlock(Block block) {
        if (isValidBlock(block)) {
            blockchain.add(block);
            pendingTransactions.clear();
        }
    }

    public boolean isValidBlock(Block block) {
        Block latestBlock = getLatestBlock();
        return latestBlock.getHash().equals(block.getPreviousHash()) && block.getHash().equals(block.calculateHash());
    }

    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    public void addTransaction(Transaction transaction) {
        if(transaction.verifySignature()) pendingTransactions.add(transaction);
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(List<Block> blockchain) {
        this.blockchain = blockchain;
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(List<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }
}
