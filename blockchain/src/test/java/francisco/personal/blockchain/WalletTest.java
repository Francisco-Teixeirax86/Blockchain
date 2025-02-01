package francisco.personal.blockchain;

import francisco.personal.blockchain.entities.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    @Test
    void testWallet() {
        Wallet wallet = new Wallet();
        assertNotNull(wallet.getAddress());
        assertNotNull(wallet.getPublicKeyAsHex());
        assertNotNull(wallet.getPrivateKeyAsHex());
    }

    @Test
    void testWalletAddressGeneration() {
        Wallet wallet = new Wallet();
        String address = wallet.getAddress();
        assertNotNull(address);
        assertFalse(address.isEmpty());
        System.out.println("Address: " + address);
    }

    @Test
    void testPubKeyEnconding() {
        Wallet wallet = new Wallet();
        String address = wallet.getPublicKeyAsHex();
        assertNotNull(address);
        assertFalse(address.isEmpty());
        System.out.println("Address: " + address);
    }
}
