INSERT INTO account (accountId,accountAmount) values (1,0.0);
INSERT INTO client (clientName,password,clientType,accountId) values ('admin','$2a$10$n0Pwqd4ye/YoRHIURErTee28iML9vdUWIMRSAdDmSeE..ioWi9eWy','ADMIN',1);
INSERT INTO operation (accountId, operationAmount, operationType,date) VALUES (1, 100.0, 'DEPOSIT',CURRENT_TIMESTAMP);