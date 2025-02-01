package francisco.personal.blockchain.entities;

import francisco.personal.blockchain.Utils.StringUtil;
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Transaction {
    private String transactionId;
    private PublicKey sender;
    private PublicKey recipient;
    private double amount;
    private String signature;


    public Transaction(PublicKey sender, PublicKey recipient, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.transactionId = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        amount
        );
    }

    public void generateSignature(PrivateKey privateKey) {
        signature = StringUtil.applyECDSASignature(privateKey, transactionId);
    }

    public boolean verifySignature() {
        return StringUtil.verifyECDSASignature(sender, transactionId, signature);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public void setRecipient(PublicKey recipient) {
        this.recipient = recipient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
