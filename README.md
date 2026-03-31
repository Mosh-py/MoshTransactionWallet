# MoshTransactionWallet

This application is a Wallet Transaction Service designed for a platform. It provides a robust set of backend APIs to manage user wallets, process financial transactions securely, and maintain data consistency.

## Core Features
+ Wallet Management: Create a wallet for a user with a unique identifier.

+ Funding: Credit a wallet with an amount greater than zero, ensuring the transaction is recorded.

+ Withdrawals: Debit a wallet while properly handling insufficient balance scenarios.

+ Transfers: Transfer funds between wallets ensuring both debit and credit sides succeed consistently, strictly preventing partial completion.

+ History & Balances: View wallet balance and return paginated transaction history.

+ Clear Logging: Logging are done clearly and visibly as transactions are done, which supports audditing and monitoring

+ Reversals: Allow the reversal of a successful transaction exactly once, recorded as a separate transaction entry.
## Architecture & Design Principles

This application was designed with scalability, reliability, and maintainability as core priorities. This is demonstrated by the following architectural decisions:

+ Extensible Domain Model: An abstract Wallet base class establishes the core financial contract, allowing specialized wallet types (like the current NormalWallet) to inherit shared behaviors. This makes the system highly extensible; introducing new products in the future—such as a ChildrenWallet, SavingsWallet, or RetirementWallet—can be done seamlessly without modifying the core transaction engine.

+ Robust Transaction Safety: Financial systems demand strict reliability. The implementation of an Idempotency Interceptor ensures that network retries or duplicate API requests do not result in double-charging or duplicate transactions, keeping user balances safe.

+ Clear Separation of Concerns: The codebase strictly adheres to a layered Spring Boot architecture (Controllers, Services, Repositories, and DTOs). This isolates the business logic from the web and data access layers, making the application much easier to test, debug, and maintain.

+ Dedicated Reversal Flows: Rather than treating refunds as standard outbound transactions, the system implements a dedicated Reversal domain and service layer. This ensures that failed or contested transactions are rolled back cleanly while maintaining a precise, immutable audit trail.

  <img width="565" height="772" alt="Image Showing the abstract class" src="https://github.com/user-attachments/assets/85b6ede0-440f-4c52-b9db-1f2f785f5f60" />

## Technology Stack

+ Java 

+ Spring Boot 

+ Spring Data JPA 

+ Maven 

+ Docker / Docker Compose

## Set up and Deployment Options
Assumption is made that Docker is available on the indivdual System

``sh git clone https://github.com/Mosh-py/MoshTransactionWallet.git && 
cd MoshTransactionWallet ``

## API Documentation (Swagger)

After running the application, you can access the Swagger UI here:

http://localhost:8080/swagger-ui/index.html

This provides a full interactive documentation of all endpoints, request bodies, and responses.


``sh docker build -t mosh-wallet-api:latest &&
docker run -p 8080:8080 mosh-wallet-api:latest``




  
