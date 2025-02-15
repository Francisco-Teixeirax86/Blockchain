package francisco.project.blockchain.components;

import francisco.project.blockchain.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Block {

    private int index;
    private long timestamp;
    private List<Transaction> transactions;
    private String previousHash;
    private int nonce;
    private String hash;

    public String calculateHash() {
        String dataToHash = previousHash + timestamp + nonce + transactions.toString();
        return StringUtil.applySha256(dataToHash);
    }
}
