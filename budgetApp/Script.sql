--<ScriptOptions statementTerminator=";"/>

CREATE TABLE TRXN_bank (
	Bank_id INT NOT NULL,
	Bank_name VARCHAR(45),
	PRIMARY KEY (Bank_id)
) ENGINE=InnoDB;

CREATE TABLE TRXN_transaction_types (
	transaction_type_id INT NOT NULL,
	transaction_type_desc VARCHAR(45),
	DR_CR VARCHAR(45),
	PRIMARY KEY (transaction_type_id)
) ENGINE=InnoDB;

CREATE TABLE TRXN_entity (
	entity_id INT NOT NULL,
	entity_classification_id INT,
	entity_description VARCHAR(450),
	entity_DR_CR VARCHAR(4),
	PRIMARY KEY (entity_id)
) ENGINE=InnoDB;

CREATE TABLE TRXN_transation_history (
	trxn_id INT NOT NULL,
	Bank_id INT,
	Account_id INT,
	transaction_type_id INT,
	transaction_date VARCHAR(45) NOT NULL,
	entity_id INT,
	Amount DECIMAL(10 , 0),
	balance DECIMAL(10 , 0),
	PRIMARY KEY (trxn_id,transaction_date)
) ENGINE=InnoDB;

CREATE TABLE TRXN_user_accounts (
	Account_id INT NOT NULL,
	Bank_id INT,
	message_acc_details VARCHAR(45),
	Account_desc VARCHAR(45),
	PRIMARY KEY (Account_id)
) ENGINE=InnoDB;

CREATE TABLE TRXN_entity_classification (
	entity_classification_id INT NOT NULL,
	classification_type VARCHAR(45),
	PRIMARY KEY (entity_classification_id)
) ENGINE=InnoDB;

