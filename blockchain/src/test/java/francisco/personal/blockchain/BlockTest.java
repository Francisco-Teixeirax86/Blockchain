package francisco.personal.blockchain;

import francisco.personal.blockchain.entities.Block;
import francisco.personal.blockchain.entities.Transaction;
import francisco.personal.blockchain.entities.Wallet;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class BlockTest {
    @Test
    void testBlockCreation() {
        Wallet wallet = new Wallet();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(wallet.getPublicKey(), wallet.getPublicKey(), 10.0));

        Block block = new Block("0", transactions, 0, 0);
        assertNotNull(block.getHash());
        assertNotNull(block.getPreviousHash());
        assertNotNull(block.getMerkleRoot());
        assertEquals(0, block.getViewNumber());
        assertEquals(0, block.getSequenceNumber());
    }

    @Test
    void testBlockHashIntegrity() {
        Wallet wallet = new Wallet();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(wallet.getPublicKey(), wallet.getPublicKey(), 10.0));

        Block block = new Block("0", transactions, 0, 0);
        String originalHash = block.getHash();
        block.setNonce(block.getNonce() + 1);
        assertNotEquals(originalHash, block.getHash());
    }

    @Test
    void testMerkleRootCalculation() {
        Wallet wallet = new Wallet();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(wallet.getPublicKey(), wallet.getPublicKey(), 10.0));
        transactions.add(new Transaction(wallet.getPublicKey(), wallet.getPublicKey(), 20.0));

        Block block = new Block("0", transactions, 0, 0);
        assertNotNull(block.getMerkleRoot());
        System.out.println("Merkle Root: " + block.getMerkleRoot());
    }
}
