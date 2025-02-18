package francisco.project.blockchain.Controller;

import francisco.project.blockchain.components.Block;
import francisco.project.blockchain.components.Blockchain;
import francisco.project.blockchain.components.Transaction;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class BlockchainController {
    private final Blockchain blockchain;
    private final RestTemplate restTemplate;
    private final Environment environment;

    @Autowired
    public BlockchainController(Blockchain blockchain, RestTemplate restTemplate, Environment environment) {
        this.blockchain = blockchain;
        this.restTemplate = restTemplate;
        this.environment = environment;
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

    @PostMapping("/chain")
    public ResponseEntity<String> receiveChain(@RequestBody List<Block> newChain) {
        return blockchain.replaceChain(newChain) ? ResponseEntity.ok("Chain replaced!") : ResponseEntity.badRequest().body("Chain not replaced");
    }

    @PostMapping("/peers")
    public ResponseEntity<String> registerPeer(@RequestBody String peer) {
        if(blockchain.getPeers().contains(peer)){
            return ResponseEntity.badRequest().body("Peer already exists");
        }
        this.blockchain.addPeer(peer);

        try {
            String selfAddress = "http://localhost:" + environment.getProperty("local.server.port");
            restTemplate.postForEntity(peer + "/api/peers", selfAddress, String.class);
        } catch (Exception e) {
            System.out.println("Failed to broadcast to broadcast itself to " + peer + " with error: " + e.getMessage());
        }
        return ResponseEntity.ok("Peer added successfully");
    }

    @GetMapping("/peers")
    public ResponseEntity<Set<String>> getPeers() {
        return ResponseEntity.ok(this.blockchain.getPeers());
    }

    @PostConstruct
    public void syncChains() {
        for (String peer : this.blockchain.getPeers()) {
            try {
                List<Block> peerChain = restTemplate.getForObject(peer + "/api/chain", List.class);
                blockchain.replaceChain(peerChain);
            } catch (Exception e) {
                System.out.println("Failed to sync chains: " + peer);
            }
        }
    }
}
