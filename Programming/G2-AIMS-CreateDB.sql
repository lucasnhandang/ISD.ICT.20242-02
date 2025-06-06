CREATE TABLE "User" (
    userID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(32) NOT NULL,
    email VARCHAR(100),
    phoneNumber CHAR(10)
);

CREATE TABLE Role (
    roleID SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE UserRole (
    userID INTEGER,
    roleID INTEGER,
    PRIMARY KEY (userID, roleID)
);

CREATE TABLE Product (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    value INTEGER NOT NULL,
    currentPrice INTEGER NOT NULL,
    barcode VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    entryDate DATE NOT NULL,
    dimension VARCHAR(50) NOT NULL,
    weight NUMERIC(10,2) NOT NULL,
    rushOrderSupported BOOLEAN,
    imageUrl VARCHAR(255)
);

CREATE TABLE ProductChecklog (
    checklogID SERIAL PRIMARY KEY,
    productID INTEGER NOT NULL,
    userID INTEGER NOT NULL,
    checkTime TIMESTAMP NOT NULL,
    actionType VARCHAR(100) NOT NULL
);

CREATE TABLE DVD (
    id INTEGER PRIMARY KEY,
    discType VARCHAR(50) NOT NULL,
    director VARCHAR(100) NOT NULL,
    runtime INTEGER NOT NULL,
    studio VARCHAR(100) NOT NULL,
    language VARCHAR(50) NOT NULL,
    subtitles VARCHAR(255) NOT NULL,
    releaseDate DATE,
    genre VARCHAR(100)
);

CREATE TABLE CD (
    id INTEGER PRIMARY KEY,
    artists VARCHAR(100) NOT NULL,
    recordLabel VARCHAR(100) NOT NULL,
    tracklist TEXT NOT NULL,
    genre VARCHAR(100),
    releaseDate DATE
);

CREATE TABLE LP (
    id INTEGER PRIMARY KEY,
    artists VARCHAR(100) NOT NULL,
    recordLabel VARCHAR(100) NOT NULL,
    tracklist TEXT NOT NULL,
    genre VARCHAR(100),
    releaseDate DATE
);

CREATE TABLE Book (
    id INTEGER PRIMARY KEY,
    authors VARCHAR(100) NOT NULL,
    coverType VARCHAR(50) NOT NULL,
    publisher VARCHAR(100) NOT NULL,
    publicationDate DATE NOT NULL,
    numPages INTEGER,
    language VARCHAR(50),
    genre VARCHAR(100)
);

CREATE TABLE DeliveryInfo (
    deliveryInfoID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    province VARCHAR(50) NOT NULL,
    shippingInstruction TEXT
);

CREATE TABLE PaymentTransaction (
    transactionID SERIAL PRIMARY KEY,
    bankTransactionID VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    paymentTime TIMESTAMP NOT NULL,
    paymentAmount INTEGER NOT NULL,
    cardType VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL
);

CREATE TABLE RefundTransaction (
    refundID SERIAL PRIMARY KEY,
    bankTransactionID VARCHAR(100) NOT NULL,
    refundTime TIMESTAMP NOT NULL,
    orderAmount INTEGER NOT NULL,
    currency VARCHAR(10) NOT NULL
);

CREATE TABLE Invoice (
    invoiceID SERIAL PRIMARY KEY,
    productPriceExVAT INTEGER NOT NULL,
    productPriceIncVAT INTEGER NOT NULL,
    shippingFee INTEGER NOT NULL,
    totalAmount INTEGER NOT NULL,
    transactionID INTEGER UNIQUE,
    refundID INTEGER UNIQUE
);

CREATE TABLE Orders (
    orderID SERIAL PRIMARY KEY,
    orderDate TIMESTAMP NOT NULL,
    orderStatus VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    isRushOrder BOOLEAN,
    rushDeliveryTime TIMESTAMP,
    rushInstruction TEXT,
    currency VARCHAR(10) NOT NULL DEFAULT 'VND',
    deliveryInfoID INTEGER,
    invoiceID INTEGER
);

CREATE TABLE OrderItem (
    orderID INTEGER,
    productID INTEGER,
    quantity INTEGER NOT NULL,
    type VARCHAR(50) NOT NULL,
    PRIMARY KEY (orderID, productID)
);

-- Add Foreign Key Constraints
ALTER TABLE UserRole 
    ADD CONSTRAINT fk_userrole_userid FOREIGN KEY (userID) REFERENCES "User"(userID) ON DELETE CASCADE,
    ADD CONSTRAINT fk_userrole_roleid FOREIGN KEY (roleID) REFERENCES Role(roleID) ON DELETE CASCADE;

ALTER TABLE ProductChecklog 
    ADD CONSTRAINT fk_productchecklog_userid FOREIGN KEY (userID) REFERENCES "User"(userID) ON DELETE CASCADE,
    ADD CONSTRAINT fk_productchecklog_productid FOREIGN KEY (productID) REFERENCES Product(id) ON DELETE CASCADE;

ALTER TABLE DVD 
    ADD CONSTRAINT fk_dvd_productid FOREIGN KEY (id) REFERENCES Product(id) ON DELETE CASCADE;

ALTER TABLE CD 
    ADD CONSTRAINT fk_cd_productid FOREIGN KEY (id) REFERENCES Product(id) ON DELETE CASCADE;

ALTER TABLE LP 
    ADD CONSTRAINT fk_lp_productid FOREIGN KEY (id) REFERENCES Product(id) ON DELETE CASCADE;

ALTER TABLE Book 
    ADD CONSTRAINT fk_book_productid FOREIGN KEY (id) REFERENCES Product(id) ON DELETE CASCADE;

ALTER TABLE Orders 
    ADD CONSTRAINT fk_orders_invoiceid FOREIGN KEY (invoiceID) REFERENCES Invoice(invoiceID) ON DELETE SET NULL,
    ADD CONSTRAINT fk_orders_deliveryinfoid FOREIGN KEY (deliveryInfoID) REFERENCES DeliveryInfo(deliveryInfoID) ON DELETE SET NULL;

ALTER TABLE OrderItem 
    ADD CONSTRAINT fk_orderitem_orderid FOREIGN KEY (orderID) REFERENCES Orders(orderID) ON DELETE CASCADE,
    ADD CONSTRAINT fk_orderitem_productid FOREIGN KEY (productID) REFERENCES Product(id) ON DELETE CASCADE;

ALTER TABLE Invoice 
    ADD CONSTRAINT fk_invoice_transactionid FOREIGN KEY (transactionID) REFERENCES PaymentTransaction(transactionID) ON DELETE SET NULL,
    ADD CONSTRAINT fk_invoice_refundid FOREIGN KEY (refundID) REFERENCES RefundTransaction(refundID) ON DELETE SET NULL;

-- Create Essential Performance Indexes

-- User table - for login and search
CREATE INDEX idx_user_email ON "User"(email);

-- Product table - for catalog search and inventory
CREATE INDEX idx_product_title ON Product(title);
CREATE INDEX idx_product_price ON Product(currentPrice);
CREATE INDEX idx_product_quantity ON Product(quantity);

-- ProductChecklog - for audit queries
CREATE INDEX idx_productchecklog_productid ON ProductChecklog(productID);
CREATE INDEX idx_productchecklog_checktime ON ProductChecklog(checkTime);

-- Orders - for order management
CREATE INDEX idx_orders_date ON Orders(orderDate);
CREATE INDEX idx_orders_status ON Orders(orderStatus);

-- DeliveryInfo - for regional queries
CREATE INDEX idx_deliveryinfo_province ON DeliveryInfo(province);

-- Payment transactions - for tracking and lookup
CREATE INDEX idx_payment_bank_transaction ON PaymentTransaction(bankTransactionID);
CREATE INDEX idx_payment_time ON PaymentTransaction(paymentTime);

-- Composite indexes for common query patterns
CREATE INDEX idx_product_price_quantity ON Product(currentPrice, quantity);
CREATE INDEX idx_orders_status_date ON Orders(orderStatus, orderDate);
CREATE INDEX idx_productchecklog_product_time ON ProductChecklog(productID, checkTime);
