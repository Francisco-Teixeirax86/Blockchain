package francisco.personal.blockchain.entities;

import francisco.personal.blockchain.Utils.StringUtil;

import java.util.List;
import java.util.Objects;

import java.util.List;

public class Block {
    private String hash;
    private String previousHash;
    private List<Transaction> transactions;
    private Long timestamp;
    private int nonce;
    private String merkleRoot;
    private int viewNumber;
    private int sequenceNumber;


    public Block(String previousHash, List<Transaction> transactions, int viewNumber, int sequenceNumber) {
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.timestamp = System.currentTimeMillis();
        this.viewNumber = viewNumber;
        this.sequenceNumber = sequenceNumber;
        this.merkleRoot = calculateMerkleRoot();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(previousHash + timestamp + merkleRoot + nonce + viewNumber + sequenceNumber);
    }

    private String calculateMerkleRoot() {
        return MerkleTree.getRoot(transactions.stream()
                .map(Transaction::getTransactionId).toList());
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
        this.hash = calculateHash();
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
