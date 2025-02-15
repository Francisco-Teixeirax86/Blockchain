package francisco.project.blockchain.components;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Blockchain {

    @Getter
    List<Block> blockchain = new ArrayList<>();
    List<Transaction> pendingTransactions = new ArrayList<>();

    public Blockchain() {
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        Block genesisBlock = new Block(0, System.currentTimeMillis(), new ArrayList<Transaction>() {{new Transaction("system", "creator-adress", 100);}}, "0", 0, "genesis-hash");
        blockchain.add(genesisBlock);
    }

    public Block getLastBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    public double getBalance(String address) {
        double balance = 0.0;
        for (Block block : blockchain) {
            for (Transaction transaction : block.getTransactions()) {
                if(address.equals(transaction.getSender())) {
                    balance -= transaction.getAmount();
                }
                if(address.equals(transaction.getRecipient())) {
                    balance += transaction.getAmount();
                }
            }
        }
        return balance;
    }

    public boolean isValidTransaction(Transaction transaction) {
        if(transaction.getAmount() <= 0) return false;

        if(transaction.getSender().equals("system")) return true;

        double confirmedBalance = getBalance(transaction.getSender());

        double pendingBalance = pendingTransactions.stream()
                .filter(pendingTransaction -> pendingTransaction.getSender().equals(transaction.getSender()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        return (confirmedBalance - pendingBalance) >= transaction.getAmount();
    }

    public void addTransaction(Transaction transaction) {
        if(isValidTransaction(transaction)) pendingTransactions.add(transaction);
        else throw new IllegalArgumentException("Transaction is not valid");

    }
}
