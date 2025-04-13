CREATE TABLE "User" (
  "userID" int PRIMARY KEY,
  "name" varchar(100) NOT NULL,
  "email" varchar(100) NOT NULL,
  "password" varchar(32) NOT NULL,
  "phoneNumber" char(10) NOT NULL,
  "userRole" varchar(50) NOT NULL
);

CREATE TABLE "Product" (
  "id" int PRIMARY KEY,
  "title" varchar(255) NOT NULL,
  "genre" varchar(100),
  "dimension" varchar(50) NOT NULL,
  "weight" numeric(10,2) NOT NULL,
  "stock" int NOT NULL,
  "barcode" varchar(100) NOT NULL,
  "currentPrice" int NOT NULL,
  "rushOrderSupported" boolean NOT NULL,
  "value" int NOT NULL,
  "category" varchar(20) NOT NULL
);

CREATE TABLE "DVD" (
  "id" int PRIMARY KEY,
  "releaseDate" date,
  "subtitles" varchar(255) NOT NULL,
  "language" varchar(50) NOT NULL,
  "studio" varchar(100) NOT NULL,
  "runtime" int NOT NULL,
  "director" varchar(100) NOT NULL,
  "discType" varchar(50) NOT NULL
);

CREATE TABLE "CD" (
  "id" int PRIMARY KEY,
  "releaseDate" date,
  "musicCollection" varchar(255) NOT NULL,
  "artist" varchar(100) NOT NULL,
  "totalTrack" int,
  "recordLabel" varchar(100) NOT NULL
);

CREATE TABLE "CDTrack" (
  "trackID" int PRIMARY KEY NOT NULL,
  "cdID" int NOT NULL,
  "trackNo" int,
  "title" varchar(255) NOT NULL
);

CREATE TABLE "LPTrack" (
  "trackID" int PRIMARY KEY NOT NULL,
  "lpID" int NOT NULL,
  "trackNo" int,
  "title" varchar(255) NOT NULL
);

CREATE TABLE "LP" (
  "id" int PRIMARY KEY,
  "releaseDate" date,
  "musicCollection" varchar(255) NOT NULL,
  "artist" varchar(100) NOT NULL,
  "totalTrack" int,
  "recordLabel" varchar(100) NOT NULL
);

CREATE TABLE "Book" (
  "id" int PRIMARY KEY,
  "author" varchar(100) NOT NULL,
  "publisher" varchar(100) NOT NULL,
  "coverType" varchar(50) NOT NULL,
  "publicationDate" date NOT NULL,
  "numPages" int,
  "language" varchar(50)
);

CREATE TABLE "Orders" (
  "orderID" int PRIMARY KEY,
  "totalItem" int NOT NULL,
  "totalPrice" int NOT NULL,
  "currency" varchar(10) NOT NULL DEFAULT 'VND',
  "invoiceID" int,
  "deliveryInfoID" int,
  "orderDate" date NOT NULL,
  "orderStatus" varchar(50) NOT NULL DEFAULT 'new',
  "isRushOrder" boolean NOT NULL
);

CREATE TABLE "OrderItem" (
  "orderID" int,
  "productID" int,
  "quantity" int NOT NULL,
  "price" int NOT NULL,
  PRIMARY KEY ("orderID", "productID")
);

CREATE TABLE "DeliveryInformation" (
  "deliveryInfoID" int PRIMARY KEY,
  "customerName" varchar(100) NOT NULL,
  "email" varchar(100) NOT NULL,
  "phoneNumber" varchar(20) NOT NULL,
  "deliveryAddress" text NOT NULL,
  "deliveryProvince" varchar(50) NOT NULL,
  "shippingInstruction" text,
  "isRushOrder" boolean NOT NULL
);

CREATE TABLE "Invoice" (
  "invoiceID" int PRIMARY KEY,
  "productPriceExVAT" int NOT NULL,
  "productPriceIncVAT" int NOT NULL,
  "shippingFee" int NOT NULL,
  "totalAmountDue" int NOT NULL,
  "transactionID" int UNIQUE,
  "refundTransactionID" int UNIQUE
);

CREATE TABLE "PaymentTransaction" (
  "transactionID" int PRIMARY KEY,
  "customerName" varchar(100) NOT NULL,
  "cardType" varchar(50) NOT NULL,
  "transactionContent" text NOT NULL,
  "totalAmount" int NOT NULL,
  "currency" varchar(10) NOT NULL,
  "date" date NOT NULL
);

CREATE TABLE "RefundTransaction" (
  "refundTransactionID" int PRIMARY KEY,
  "refundDate" date NOT NULL,
  "orderAmount" int NOT NULL,
  "bankTransactionID" varchar(100) NOT NULL,
  "currency" varchar(10) NOT NULL
);

COMMENT ON COLUMN "User"."userRole" IS 'admin or productManager';

COMMENT ON COLUMN "Product"."category" IS 'book, lp, cd, dvd';

COMMENT ON COLUMN "Orders"."orderStatus" IS 'new, pending, approved, delivered, rejectedNotRefunded, refunded, canceled';

COMMENT ON TABLE "OrderItem" IS 'Composite primary key (orderID, productID)';

ALTER TABLE "DVD" ADD FOREIGN KEY ("id") REFERENCES "Product" ("id");

ALTER TABLE "CD" ADD FOREIGN KEY ("id") REFERENCES "Product" ("id");

ALTER TABLE "CDTrack" ADD FOREIGN KEY ("cdID") REFERENCES "CD" ("id");

ALTER TABLE "LPTrack" ADD FOREIGN KEY ("lpID") REFERENCES "LP" ("id");

ALTER TABLE "LP" ADD FOREIGN KEY ("id") REFERENCES "Product" ("id");

ALTER TABLE "Book" ADD FOREIGN KEY ("id") REFERENCES "Product" ("id");

ALTER TABLE "Orders" ADD FOREIGN KEY ("invoiceID") REFERENCES "Invoice" ("invoiceID");

ALTER TABLE "Orders" ADD FOREIGN KEY ("deliveryInfoID") REFERENCES "DeliveryInformation" ("deliveryInfoID");

ALTER TABLE "OrderItem" ADD FOREIGN KEY ("orderID") REFERENCES "Orders" ("orderID");

ALTER TABLE "OrderItem" ADD FOREIGN KEY ("productID") REFERENCES "Product" ("id");

ALTER TABLE "Invoice" ADD FOREIGN KEY ("transactionID") REFERENCES "PaymentTransaction" ("transactionID");

ALTER TABLE "Invoice" ADD FOREIGN KEY ("refundTransactionID") REFERENCES "RefundTransaction" ("refundTransactionID");
