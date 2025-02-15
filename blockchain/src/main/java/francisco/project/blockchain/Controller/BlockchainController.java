package francisco.project.blockchain.Controller;

import francisco.project.blockchain.components.Block;
import francisco.project.blockchain.components.Blockchain;
import francisco.project.blockchain.components.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class BlockchainController {
    private final Blockchain blockchain;

    @Autowired
    public BlockchainController(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
        try {
            blockchain.addTransaction(transaction);
            return ResponseEntity.ok("Transaction added successfully to the pending pool");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mine")
    public  ResponseEntity<Block> mineBlock(@RequestParam String miner) {
        blockchain.minePendingTransactions(miner);
        Block minedBlock = blockchain.getLastBlock();
        return ResponseEntity.ok(minedBlock);
    }

    @GetMapping("/chain")
    public ResponseEntity<List<Block>> getChain() {
        return ResponseEntity.ok(blockchain.getBlockchain());
    }

    @GetMapping("/balance/{address}")
    public ResponseEntity<Double> getBalance(@PathVariable String address) {
        double balance = blockchain.getBalance(address);
        return ResponseEntity.ok(balance);
    }
}
