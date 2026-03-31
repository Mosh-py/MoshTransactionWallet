CREATE TABLE if not exists user (
    user_id VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL unique,
    username VARCHAR(255) NOT NULL unique,
    PRIMARY KEY (user_id)
);


CREATE TABLE IF NOT EXISTS wallet (
    id VARCHAR(255) PRIMARY KEY,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    user_id VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE IF NOT EXISTS normal_wallet (
    id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(255) PRIMARY KEY,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(19,2),
    transaction_status VARCHAR(50),
    transaction_type VARCHAR(50),
    wallet_id VARCHAR(255),
    idempotency_key VARCHAR(255) NOT NULL,
    transaction_reference VARCHAR(255),
    sender_id VARCHAR(40),
    receiver_id VARCHAR(40),
    CONSTRAINT uk_idempotency_key_type UNIQUE (idempotency_key),
    CONSTRAINT fk_transaction_wallet FOREIGN KEY (wallet_id) REFERENCES wallet(id)
);

CREATE TABLE IF NOT EXISTS reversal (
    id BIGINT NOT NULL AUTO_INCREMENT,
    date_reversed DATETIME(6),
    date_last_modified DATETIME(6),
    transaction_reference VARCHAR(40),
    transaction_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_transaction_id (transaction_id),
    CONSTRAINT FK_reversal_transaction FOREIGN KEY (transaction_id) 
        REFERENCES transactions(id)
);

