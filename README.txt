pake sql ini ya ges buat tabel user dan items

CREATE TABLE `user` (
  `User_id` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `phoneNumber` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `role` VARCHAR(255) NOT NULL
);

CREATE TABLE `items` (
  `ItemID` VARCHAR(255) NOT NULL,
  `ItemName` VARCHAR(255) NOT NULL,
  `ItemSize` VARCHAR(255) NOT NULL,
  `ItemPrice` VARCHAR(255) NOT NULL,
  `ItemCategory` VARCHAR(255) NOT NULL,
  `ItemStatus` VARCHAR(255) DEFAULT NULL,
  `ItemWishlist` VARCHAR(255) DEFAULT NULL,
  `ItemOfferStatus` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ItemID`)
);