-- USERS
CREATE TABLE users (
    userid BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phonenumber VARCHAR(10)
);

-- ROLE
CREATE TABLE role (
    roleid BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- USER_ROLE_MAP (join table)
CREATE TABLE user_role_map (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(userid) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(roleid) ON DELETE CASCADE
);

-- PRODUCT
CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    value INTEGER NOT NULL,
    currentprice INTEGER NOT NULL,
    category VARCHAR(50) NOT NULL,
    barcode VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    entrydate DATE NOT NULL,
    dimension VARCHAR(50) NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    rushordersupported BOOLEAN,
    imageurl VARCHAR(255)
);

-- PRODUCT CHILD TABLES
CREATE TABLE dvd (
    id BIGINT PRIMARY KEY,
    disctype VARCHAR(50) NOT NULL,
    director VARCHAR(100) NOT NULL,
    runtime INTEGER NOT NULL,
    studio VARCHAR(100) NOT NULL,
    language VARCHAR(50) NOT NULL,
    subtitles VARCHAR(255) NOT NULL,
    releasedate DATE,
    genre VARCHAR(100),
    FOREIGN KEY (id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE cd (
    id BIGINT PRIMARY KEY,
    artists VARCHAR(100) NOT NULL,
    recordlabel VARCHAR(100) NOT NULL,
    tracklist TEXT NOT NULL,
    genre VARCHAR(100),
    releasedate DATE,
    FOREIGN KEY (id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE lp (
    id BIGINT PRIMARY KEY,
    artists VARCHAR(100) NOT NULL,
    recordlabel VARCHAR(100) NOT NULL,
    tracklist TEXT NOT NULL,
    genre VARCHAR(100),
    releasedate DATE,
    FOREIGN KEY (id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE book (
    id BIGINT PRIMARY KEY,
    authors VARCHAR(100) NOT NULL,
    covertype VARCHAR(50) NOT NULL,
    publisher VARCHAR(100) NOT NULL,
    publicationdate DATE NOT NULL,
    numpages INTEGER,
    language VARCHAR(50),
    genre VARCHAR(100),
    FOREIGN KEY (id) REFERENCES product(id) ON DELETE CASCADE
);

-- PRODUCT CHECK LOG
CREATE TABLE productchecklog (
    checklogid BIGSERIAL PRIMARY KEY,
    productid BIGINT NOT NULL,
    userid BIGINT NOT NULL,
    checktime TIMESTAMP NOT NULL,
    actiontype VARCHAR(100) NOT NULL,
    FOREIGN KEY (productid) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE
);

-- DELIVERY INFO
CREATE TABLE deliveryinfo (
    deliveryinfoid BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    phonenumber VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    province VARCHAR(50) NOT NULL,
    shippinginstruction TEXT,
    expected_time TIMESTAMP

);

-- PAYMENT
CREATE TABLE paymenttransaction (
    transactionid BIGSERIAL PRIMARY KEY,
    banktransactionid VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    paymenttime TIMESTAMP NOT NULL,
    paymentamount INTEGER NOT NULL,
    cardtype VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL
);

-- REFUND
CREATE TABLE refundtransaction (
    refundid BIGSERIAL PRIMARY KEY,
    banktransactionid VARCHAR(100) NOT NULL,
    refundtime TIMESTAMP NOT NULL,
    orderamount INTEGER NOT NULL,
    currency VARCHAR(10) NOT NULL
);

-- INVOICE
CREATE TABLE invoice (
    invoiceid BIGSERIAL PRIMARY KEY,
    productpriceexvat INTEGER NOT NULL,
    productpriceincvat INTEGER NOT NULL,
    shippingfee INTEGER NOT NULL,
    totalamount INTEGER NOT NULL,
    transactionid BIGINT UNIQUE,
    refundid BIGINT UNIQUE,
    FOREIGN KEY (transactionid) REFERENCES paymenttransaction(transactionid) ON DELETE SET NULL,
    FOREIGN KEY (refundid) REFERENCES refundtransaction(refundid) ON DELETE SET NULL
);

-- ORDERS
CREATE TABLE orders (
    orderid BIGSERIAL PRIMARY KEY,
    orderdate TIMESTAMP NOT NULL,
    orderstatus VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    isrushorder BOOLEAN,
    rushdeliverytime TIMESTAMP,
    rushinstruction TEXT,
    currency VARCHAR(10) NOT NULL DEFAULT 'VND',
    deliveryinfoid BIGINT,
    invoiceid BIGINT,
    FOREIGN KEY (invoiceid) REFERENCES invoice(invoiceid) ON DELETE SET NULL,
    FOREIGN KEY (deliveryinfoid) REFERENCES deliveryinfo(deliveryinfoid) ON DELETE SET NULL
);

-- ORDER ITEM
CREATE TABLE orderitem (
    orderid BIGINT,
    productid BIGINT,
    quantity INTEGER NOT NULL,
    price INTEGER NOT NULL,
    type VARCHAR(50) NOT NULL,
    PRIMARY KEY (orderid, productid),
    FOREIGN KEY (orderid) REFERENCES orders(orderid) ON DELETE CASCADE,
    FOREIGN KEY (productid) REFERENCES product(id) ON DELETE CASCADE
);
CREATE TABLE reservation (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE reservation_item (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservation(id)
        ON DELETE CASCADE
);


-- INDEXES
CREATE INDEX idx_user_email ON users(email);

CREATE INDEX idx_product_title ON product(title);
CREATE INDEX idx_product_price ON product(currentprice);
CREATE INDEX idx_product_quantity ON product(quantity);
CREATE INDEX idx_product_category ON product(category);
CREATE INDEX idx_product_price_quantity ON product(currentprice, quantity);

CREATE INDEX idx_productchecklog_productid ON productchecklog(productid);
CREATE INDEX idx_productchecklog_checktime ON productchecklog(checktime);
CREATE INDEX idx_productchecklog_product_time ON productchecklog(productid, checktime);

CREATE INDEX idx_orders_date ON orders(orderdate);
CREATE INDEX idx_orders_status ON orders(orderstatus);
CREATE INDEX idx_orders_status_date ON orders(orderstatus, orderdate);

CREATE INDEX idx_deliveryinfo_province ON deliveryinfo(province);

CREATE INDEX idx_payment_bank_transaction ON paymenttransaction(banktransactionid);
CREATE INDEX idx_payment_time ON paymenttransaction(paymenttime);
