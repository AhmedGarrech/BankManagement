CREATE TABLE IF NOT EXISTS account (
    accountId BIGINT AUTO_INCREMENT PRIMARY KEY,
    accountAmount DOUBLE);

CREATE TABLE IF NOT EXISTS client (
    clientId BIGINT AUTO_INCREMENT PRIMARY KEY,
    clientName VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    clientType VARCHAR(255) NOT NULL,
    accountId VARCHAR(255),
    FOREIGN KEY (accountId) REFERENCES account(accountId));

CREATE TABLE IF NOT EXISTS operation (
                operationId BIGINT AUTO_INCREMENT PRIMARY KEY,
                accountId BIGINT,
                operationAmount DOUBLE,
                operationType VARCHAR(255),
                date DATETIME,
    FOREIGN KEY (accountId) REFERENCES account(accountId));