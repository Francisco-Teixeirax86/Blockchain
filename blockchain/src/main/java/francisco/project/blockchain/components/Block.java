package francisco.project.blockchain.components;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Block {

    private int index;
    private String previousHash;
    private long timestamp;
    private String hash;
    private int nonce;
    private List<Transaction> transactions;

    private String calculateHash() {
        String dataToHash = previousHash + timestamp + nonce + transactions.toString();
        return StringUtil.applySha256(dataToHash);
    }
}
