package francisco.project.blockchain;

import francisco.project.blockchain.components.Blockchain;
import francisco.project.blockchain.components.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlockchainApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockchainApplication.class, args);
    }


}
