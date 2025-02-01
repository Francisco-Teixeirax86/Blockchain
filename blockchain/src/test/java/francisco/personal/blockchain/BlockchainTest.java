package francisco.personal.blockchain;

import francisco.personal.blockchain.Components.Blockchain;
import francisco.personal.blockchain.entities.Block;
import francisco.personal.blockchain.entities.Transaction;
import francisco.personal.blockchain.entities.Wallet;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BlockchainTest {

    @Test
    void testGenesisBlockCreation() {
        Blockchain blockchain = new Blockchain();
        assertEquals(1, blockchain.getBlockchain().size());
        assertEquals("0", blockchain.getBlockchain().get(0).getPreviousHash());
    }

    @Test
    void testAddBlock() {
        Blockchain blockchain = new Blockchain();
        Wallet wallet = new Wallet();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(wallet.getPublicKey(), wallet.getPublicKey(), 10.0));

        Block block = new Block(blockchain.getLatestBlock().getHash(), transactions, 0, 1);
        blockchain.addBlock(block);
        assertEquals(2, blockchain.getBlockchain().size());
        assertEquals(block.getHash(), blockchain.getLatestBlock().getHash());
    }

    @Test
    void testInvalidChain() {
        Blockchain blockchain = new Blockchain();
        Wallet wallet = new Wallet();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(wallet.getPublicKey(), wallet.getPublicKey(), 10.0));

        Block block = new Block(blockchain.getLatestBlock().getHash(), transactions, 0, 1);
        blockchain.addBlock(block);

        block.setNonce(20);

        assertFalse(blockchain.isValidBlock(block));
    }
}
