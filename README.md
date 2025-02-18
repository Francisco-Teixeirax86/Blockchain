# Blockchain Implementation in Java with Spring Boot  
A simple yet functional blockchain demonstrating core concepts like proof-of-work, peer-to-peer networking, and transaction validation. Built for learning and portfolio purposes.

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## Features  
- **Blockchain Core**:  
  - Mine blocks with **proof-of-work** (hash starting with `00`).  
  - Validate transactions and wallet balances.  
  - Consensus mechanism (adopt the longest valid chain).
  - Dynamic proof-of-work difficulty adjustment (increases as the blockchain grows based on it's current size).  


- **Peer-to-Peer Networking**:  
  - Register and sync with other nodes.  
  - Broadcast transactions and blocks automatically.  

- **APIs**:  
  - Create transactions, mine blocks, check balances, and manage peers via REST.    

---

## Proof-of-Work 
Miners solve a computational puzzle (finding a hash with a specific pattern, e.g., starting with `00`) to add a new block. This process:  
- **Prevents spam**: Miners must invest computational resources.  
- **Enables decentralized consensus**: Miners compete to solve the puzzle, ensuring no single entity controls the blockchain.  

In this project, the difficulty of the puzzle increases as the blockchain grows:  
- **2 leading zeros** for the first 2 blocks.  
- **1 more leading zero** for every block afterwards

---

## Tech Stack  
- **Backend**: Java 17, Spring Boot  
- **Tools**: Maven, Lombok, RestTemplate  
- **Testing**: Postman, JUnit  

---

## Setup  
1. **Clone the repository**:  
   ```bash  
   git clone https://github.com/Francisco-Teixeirax86/blockchain.git  
   cd blockchain
##Run
- mvn clean install  
- mvn spring-boot:run

##Running multiple nodes
# Node 1 (Port 8080)  
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080  

# Node 2 (Port 8081)  
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081  

Test using curl or postman
