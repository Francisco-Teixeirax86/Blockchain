package francisco.project.blockchain.components;

import francisco.project.blockchain.utils.Constants;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class Blockchain {

    @Getter
    private List<Block> blockchain = new ArrayList<>();
    private List<Transaction> pendingTransactions = new ArrayList<>();
    @Getter
    private Set<String> peers = new HashSet<>();
    private final RestTemplate restTemplate;

    public void addPeer(String peer){
        peers.add(peer);
    }

    public void removePeer(String peer){
        peers.remove(peer);
    }

    public void broadcastChain(){
        for(String peer : peers){
            try {
                restTemplate.postForEntity(peer + "/api/chain", blockchain, String.class);
            } catch (Exception e) {
                System.out.println("Failed to broadcast to " + peer + " with error: " + e.getMessage());
            }
        }
    }

    @Autowired
    public Blockchain(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        List<Transaction> genesisTransactions = new ArrayList<>();
        Block genesisBlock = new Block(0, System.currentTimeMillis(), genesisTransactions, "0", 0, "genesis-hash");

        mineBlock(genesisBlock);
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

    public void minePendingTransactions(String minerAddress) {
        //Mining reward to transaction
        Transaction transaction = new Transaction("system", minerAddress, Constants.miningReward);
        addTransaction(transaction);

        Block newBlock = new Block(
                blockchain.size(),
                System.currentTimeMillis(),
                new ArrayList<>(pendingTransactions),
                getLastBlock().getHash(),
                0,
                ""
        );

        //mine the block proof-of-work
        mineBlock(newBlock);

        blockchain.add(newBlock);
        pendingTransactions.clear();

        broadcastChain();
    }


    //Simple proof-of-work: find a hash starting with "00"
    private void mineBlock(Block block) {
        while(!block.getHash().startsWith("00")) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(block.calculateHash());
        }
    }


    public boolean isChainValid(List<Block> chainToValidate) {
        //Check if the chain starts with the genesis block
        Block genesisBlock = chainToValidate.get(0);
        if(!isGenesisBlockValid(genesisBlock)) return false;

        for (int i = 1; i  < chainToValidate.size(); i++) {
            Block block = chainToValidate.get(i);
            Block previousBlock = chainToValidate.get(i - 1);

            //Check if the stored hash is the calculated hash, in other words, checking
            //if the block hasn't been manipulated
            if(!block.getHash().equals(block.calculateHash())) return false;

            //Check if the previous hash matches the hash of the previous block
            if(!previousBlock.getHash().equals(block.getPreviousHash())) return false;
            
            //Check proof-of-work
            if(!block.getHash().startsWith("00")) return false;
        }

        return true;
    }

    //Check if the genesis block is valid
    private boolean isGenesisBlockValid(Block genesisBlock) {
        if (genesisBlock.getIndex() != 0 ||
                !genesisBlock.getPreviousHash().equals("0") ||
                !genesisBlock.getTransactions().isEmpty()) {
            return false;
        }

        return genesisBlock.getHash().equals(genesisBlock.calculateHash());
    }

    public boolean replaceChain(List<Block> newChain) {
        if(newChain.size() > blockchain.size() && isChainValid(newChain)) {
            blockchain = new ArrayList<>(newChain);
            removeConfirmedTransactions();
            return true;
        }

        return false;
    }

    private void removeConfirmedTransactions() {
        Set<Transaction> confirmedTransactions = new HashSet<>();
        for(Block block : blockchain) {
            confirmedTransactions.addAll(block.getTransactions());
        }

        pendingTransactions.removeIf(confirmedTransactions::contains);
    }
}
