DROP DATABASE IF EXISTS  AIRLINE;

CREATE DATABASE AIRLINE;
USE AIRLINE;


-- NAMES
DROP TABLE IF EXISTS NAMES;
CREATE TABLE NAMES (
	nameID		    int not null AUTO_INCREMENT,
    firstName		varchar(25),
	lastName		varchar(25),
	primary key (nameID)
);

INSERT INTO NAMES (nameID, firstName, lastName) VALUES
(1, 'Ella', 'Boulanger'),
(2, 'Hamza', 'Itani'),
(3, 'Fabiha', 'Tuheen'),
(4, 'Afrah', 'Mohommand'),
(5, 'Lisa', 'Vanderpump'),
(6, 'Tom', 'Sandoval'),
(7, 'Jax', 'Smith'),
(8, 'Stassi', 'Schroeder'),
(9, 'Avery', 'Jones'),
(10, 'Malibu', 'Barbie'),
(11, 'Just', 'Ken'),
(12, 'Alan', 'Friend');



-- ADDRESSES
DROP TABLE IF EXISTS ADDRESSES;
CREATE TABLE ADDRESSES (
	addressID		int not null AUTO_INCREMENT,
    houseNumber		    int,
	streetName		varchar(25),
    city            varchar(25),
    province        varchar(25),
    postalCode     varchar(6),
	primary key (addressID)
);

INSERT INTO ADDRESSES (addressID, houseNumber, streetName, city, province, postalCode) VALUES
(1, 100, 'Java St', 'Calgary', 'Alberta', 'T3V2X5'),
(2, 100, 'Python St', 'Edmonton', 'Alberta', 'T4G2X7'),
(3, 100, 'React St', 'Toronto', 'Onatrio', 'T5J4X5'),
(4, 100, 'Cpp St', 'Calgary', 'Alberta', 'T1AF3'),
(5, 100, 'Main St', 'Winnpeg', 'Manitoba', 'T4D5B6'),
(6, 100, 'Road St', 'Quebec City', 'Quebec', 'T0B7G5'),
(7, 100, 'Highway St', 'Montreal', 'Quebec', 'T9Y2V7'),
(8, 100, 'Freeway St', 'Calgary', 'Alberta', 'T2F2C8'),
(9, 100, 'Uni St', 'Lethbridge', 'Alberta', 'T3V3X3'),
(10, 100, 'Class St', 'Medecine Hat', 'Alberta', 'T5T5T5'),
(11, 100, 'Object St', 'Calgary', 'Alberta', 'T6T6T6'),
(12, 100, 'Instance St', 'Edmonton', 'Alberta', 'T7T7T7');



-- ACCOUNTS
DROP TABLE IF EXISTS ACCOUNTS;
CREATE TABLE ACCOUNTS (
	accountID		int not null AUTO_INCREMENT,
    nameID		    int,
	addressID		int,
    email           varchar(25),
    role            ENUM('registeredUser', 'systemAdmin', 'airlineAgent', 'tourismAgent', 'flightAttendent'),
    password		varchar(30),
	PRIMARY KEY (accountID),
    FOREIGN KEY (nameID) REFERENCES names(nameID),
    FOREIGN KEY (addressID) REFERENCES addresses(addressID)
);

INSERT INTO ACCOUNTS (accountID, nameID, addressID, email, role, password) VALUES
(1, 1, 1, 'ellab@gmail.com', 'registeredUser', 'ella1'),
(2, 2, 2, 'hamzai@gmail.com', 'registeredUser', 'hamza2'),
(3, 3, 3, 'fabihat@gmail.com', 'registeredUser', 'fabiha3'),
(4, 4, 4, 'afrahm@gmail.com', 'registeredUser', 'afrah4'),
(5, 5, 5, 'lisav@gmail.com', 'airlineAgent', 'lisa5'),
(6, 6, 6, 'tomsi@gmail.com', 'airlineAgent', 'tom6'),
(7, 7, 7, 'jaxs@gmail.com', 'flightAttendent', 'jax7'),
(8, 8, 8, 'stassis@gmail.com', 'flightAttendent', 'stassi8'),
(9, 9, 9, 'averyj@gmail.com', 'flightAttendent', 'avery9'),
(10, 10, 10, 'malibub@gmail.com', 'flightAttendent', 'malibu10'),
(11, 11, 11, 'justk@gmail.com', 'systemAdmin', 'just11'),
(12, 12, 12, 'alanf@gmail.com', 'tourismAgent', 'alan12');



-- AIRLINE AGENTS
DROP TABLE IF EXISTS AIRLINEAGENTS;
CREATE TABLE AIRLINEAGENTS (
	airlineAgentID		int not null AUTO_INCREMENT,
    accountID		        int,
	PRIMARY KEY (airlineAgentID),
    FOREIGN KEY (accountID) REFERENCES accounts(accountID)    
);

INSERT INTO AIRLINEAGENTS (airlineAgentID, accountID) VALUES
(1, 5),
(2, 6);



-- SYSTEM ADMINS
DROP TABLE IF EXISTS SYSTEMADMINS;
CREATE TABLE SYSTEMADMINS (
	systemAdminID		int not null AUTO_INCREMENT,
    accountID		        int,
	PRIMARY KEY (systemAdminID),
    FOREIGN KEY (accountID) REFERENCES accounts(accountID)
);

INSERT INTO SYSTEMADMINS (systemAdminID, accountID) VALUES
(1, 11);



-- TOURISM AGENT
DROP TABLE IF EXISTS TOURISMAGENTS;
CREATE TABLE TOURISMAGENTS (
	tourismAgentID		    int not null AUTO_INCREMENT,
    accountID		        int,
	PRIMARY KEY (tourismAgentID),
    FOREIGN KEY (accountID) REFERENCES accounts(accountID)
    
);

INSERT INTO TOURISMAGENTS (tourismAgentID, accountID) VALUES
(1, 12);


-- CREDIT CARD
DROP TABLE IF EXISTS CREDITCARDS;
CREATE TABLE CREDITCARDS (
	cardID		    int not null AUTO_INCREMENT,
    cardDigits		varchar(16),
    expirationDate  varchar(5),
    securityCode    varchar(3),
	PRIMARY KEY (cardID)
);

INSERT INTO CREDITCARDS (cardID, cardDigits, expirationDate, securityCode) VALUES
(1, '1111111111111111', '05/30', '123'),
(2, '2222222222222222', '05/30', '123'),
(3, '3333333333333333', '05/30', '123'),
(4, '4444444444444444', '05/30', '123'),
(5, '5555555555555555', '05/30', '123'),
(6, '6666666666666666', '05/30', '123');

-- COMPANY CREDIT CARD
DROP TABLE IF EXISTS COMPANYCREDITCARDS;
CREATE TABLE COMPANYCREDITCARDS (
	companyCardID		    int not null AUTO_INCREMENT,
    companyCardDigits		varchar(30),
    companyExpirationDate  varchar(5),
    companySecurityCode    varchar(10),
	PRIMARY KEY (companyCardID)
);

INSERT INTO COMPANYCREDITCARDS (companyCardID, companyCardDigits, companyExpirationDate, companySecurityCode) VALUES
(1, "0000000000000000", '00/00', "000"),
(2, "1234567890123456", "05/12", "156");

-- REGISTERED USERS
DROP TABLE IF EXISTS REGISTEREDUSERS;
CREATE TABLE REGISTEREDUSERS (
	registeredUserID		int not null AUTO_INCREMENT,
    accountID		        int,
    airlineAgentID          int,
    hasMembership           varchar(3),
    companyCard             int,
    creditCard              int, 
	PRIMARY KEY (registeredUserID),
    FOREIGN KEY (accountID) REFERENCES accounts(accountID),
    FOREIGN KEY (airlineAgentID) REFERENCES airlineagents(airlineAgentID),
    FOREIGN KEY (companyCard) REFERENCES companycreditcards(companyCardID),
    FOREIGN KEY (creditCard) REFERENCES creditcards(cardID)

);

INSERT INTO REGISTEREDUSERS (registeredUserID, accountID, airlineAgentID, hasMembership, companyCard, creditCard) VALUES
(1, 1, 1, 'yes', 1, null),
(2, 2, 1, 'yes', 2, 2),
(3, 3, 2, 'yes', 1, 3),
(4, 4, 2, 'no', 1, 4);


-- AIRCRAFTS
DROP TABLE IF EXISTS AIRCRAFTS;
CREATE TABLE AIRCRAFTS (
	aircraftID		    int not null AUTO_INCREMENT,
    aircraftName		varchar(25),
	PRIMARY KEY (aircraftID)
);

INSERT INTO AIRCRAFTS (aircraftID, aircraftName) VALUES
(1, 'Spaceship'),
(2, 'Comet'),
(3, 'Cupid'),
(4, 'Donner'),
(5, 'Blitzen'),
(6, 'Dasher');


-- FLIGHTS
DROP TABLE IF EXISTS FLIGHTS;
CREATE TABLE FLIGHTS (
	flightID		    int not null AUTO_INCREMENT,
    aircraftID		    int,
    gate                varchar(3),
    departureDate       date,
    departureTime       time,
    arrivalTime         time,
    destination         varchar(25),
    origin              varchar(25),
    price               int,
	PRIMARY KEY (flightID),
    FOREIGN KEY (aircraftID) REFERENCES aircrafts(aircraftID)
    );

INSERT INTO FLIGHTS (flightID, aircraftID, gate, departureDate, departureTime, arrivalTime, destination, origin, price) VALUES
(1, 1, 'A01', '2023-11-30', '09:30:00', '11:45:00', 'Montreal', 'Calgary', 473),
(2, 2, 'A05', '2023-12-06', '18:30:00', '00:45:00', 'Winnipeg', 'Calgary', 251),
(3, 3, 'B23', '2024-01-10', '02:30:00', '05:45:00', 'Lethbridge', 'Quebec City', 607),
(4, 4, 'C08', '2024-05-23', '12:30:00', '14:15:00', 'Edmonton', 'Toronto', 539),
(5, 5, 'C20', '2024-07-15', '02:30:00', '06:45:00', 'Medicine Hat', 'Montreal', 312),
(6, 6, 'C22', '2024-08-05', '06:30:00', '07:30:00', 'Edmonton', 'Calgary', 123);


-- SEATS
DROP TABLE IF EXISTS SEATS;
CREATE TABLE SEATS (
	seatID		    int not null AUTO_INCREMENT,
    seatName		    varchar(3),
    seatClass           ENUM('ordinary', 'comfort', 'businessClass'),
	PRIMARY KEY (seatID)
);

INSERT INTO SEATS (seatID, seatName, seatClass) VALUES
(1, '01A', 'businessClass'),
(2, '01B', 'businessClass'),
(3, '01C', 'businessClass'),
(4, '01D', 'businessClass'),
(5, '02A', 'comfort'),
(6, '02B', 'comfort'),
(7, '02C', 'comfort'),
(8, '02D', 'comfort'),
(9, '03A', 'ordinary'),
(10, '03B', 'ordinary'),
(11, '03C', 'ordinary'),
(12, '03D', 'ordinary');

-- SEAT MAPPING
DROP TABLE IF EXISTS SEATMAPPING;
CREATE TABLE SEATMAPPING (
	seatMapID		    int not null AUTO_INCREMENT,
    seatID		        int,
    flightID            int,
    seatStatus          ENUM('occupied', 'available'),
	PRIMARY KEY (seatMapID),
    FOREIGN KEY (seatID) REFERENCES seats(seatID),
    FOREIGN KEY (flightID) REFERENCES flights(flightID)
);

INSERT INTO SEATMAPPING (seatMapID, seatID, flightID, seatStatus) VALUES
(1, 1, 1, 'available'),
(2, 2, 1, 'available'),
(3, 3, 1, 'available'),
(4, 4, 1, 'available'),
(5, 5, 1, 'available'),
(6, 6, 1, 'available'),
(7, 7, 1, 'available'),
(8, 8, 1, 'available'),
(9, 9, 1, 'occupied'),
(10, 10, 1, 'available'),
(11, 11, 1, 'available'),
(12, 12, 1, 'available'),
(13, 1, 2, 'available'),
(14, 2, 2, 'available'),
(15, 3, 2, 'available'),
(16, 4, 2, 'occupied'),
(17, 5, 2, 'available'),
(18, 6, 2, 'available'),
(19, 7, 2, 'available'),
(20, 8, 2, 'available'),
(21, 9, 2, 'available'),
(22, 10, 2, 'available'),
(23, 11, 2, 'available'),
(24, 12, 2, 'available'),
(25, 1, 3, 'available'),
(26, 2, 3, 'available'),
(27, 3, 3, 'occupied'),
(28, 4, 3, 'available'),
(29, 5, 3, 'available'),
(30, 6, 3, 'available'),
(31, 7, 3, 'available'),
(32, 8, 3, 'available'),
(33, 9, 3, 'available'),
(34, 10, 3, 'available'),
(35, 11, 3, 'available'),
(36, 12, 3, 'available'),
(37, 1, 4, 'available'),
(38, 2, 4, 'available'),
(39, 3, 4, 'available'),
(40, 4, 4, 'occupied'),
(41, 5, 4, 'available'),
(42, 6, 4, 'occupied'),
(43, 7, 4, 'available'),
(44, 8, 4, 'available'),
(45, 9, 4, 'available'),
(46, 10, 4, 'available'),
(47, 11, 4, 'available'),
(48, 12, 4, 'available'),
(49, 1, 5, 'available'),
(50, 2, 5, 'available'),
(51, 3, 5, 'available'),
(52, 4, 5, 'available'),
(53, 5, 5, 'available'),
(54, 6, 5, 'available'),
(55, 7, 5, 'occupied'),
(56, 8, 5, 'available'),
(57, 9, 5, 'available'),
(58, 10, 5, 'available'),
(59, 11, 5, 'available'),
(60, 12, 5, 'available'),
(61, 1, 6, 'available'),
(62, 2, 6, 'available'),
(63, 3, 6, 'available'),
(64, 4, 6, 'available'),
(65, 5, 6, 'available'),
(66, 6, 6, 'available'),
(67, 7, 6, 'available'),
(68, 8, 6, 'available'),
(69, 9, 6, 'available'),
(70, 10, 6, 'available'),
(71, 11, 6, 'available'),
(72, 12, 6, 'available');


-- TICKETS
DROP TABLE IF EXISTS TICKETS;
CREATE TABLE TICKETS (
	ticketID		    int not null AUTO_INCREMENT,
    accountID		    int,
    flightID            int,
    seatMapID            int,
    hasInsurance        varchar(3),
    cost                DOUBLE(10, 2),
	PRIMARY KEY (ticketID),
    FOREIGN KEY (accountID) REFERENCES accounts(accountID),
    FOREIGN KEY (flightID) REFERENCES flights(flightID),
    FOREIGN KEY (seatMapID) REFERENCES seatmapping(seatMapID)

);

INSERT INTO TICKETS (ticketID, accountID, flightID, seatMapID, hasInsurance, cost) VALUES
(1, 1, 1, 9, 'yes', 586.87),
(2, 1, 2, 16, 'yes', 900.01),
(3, 2, 3, 27, 'no', 876.55),
(4, 3, 4, 40, 'yes', 456.76),
(5, 4, 4, 42, 'no', 678.34),
(6, 3, 5, 55, 'yes', 343.78);



-- FLIGHT ATTENDENTS
DROP TABLE IF EXISTS FLIGHTATTENDENTS;
CREATE TABLE FLIGHTATTENDENTS (
	flightAttendentID		int not null AUTO_INCREMENT,
    accountID		        int,
	PRIMARY KEY (flightAttendentID),
    FOREIGN KEY (accountID) REFERENCES accounts(accountID)
);

INSERT INTO FLIGHTATTENDENTS (flightAttendentID, accountID) VALUES
(1, 7),
(2, 8),
(3, 9);


-- FLIGHT ATTENDENT ASSIGNMENT
DROP TABLE IF EXISTS FLIGHTATTENDENTASSIGNMENTS;
CREATE TABLE FLIGHTATTENDENTASSIGNMENTS (
    assignmentID                int not null AUTO_INCREMENT,
	flightAttendentID		    int,
    flightID                    int,
	PRIMARY KEY (assignmentID),
    FOREIGN KEY (flightAttendentID) REFERENCES flightattendents(flightAttendentID),
    FOREIGN KEY (flightID) REFERENCES flights(flightID)

);

INSERT INTO FLIGHTATTENDENTASSIGNMENTS (assignmentID, flightAttendentID, flightID) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 2),
(4, 1, 2),
(5, 2, 3),
(6, 3, 3),
(7, 1, 4),
(8, 2, 4),
(9, 3, 5),
(10, 1, 5),
(11, 2, 6),
(12, 3, 6);


-- BOOKING REQUESTS
DROP TABLE IF EXISTS BOOKINGREQUESTS;
CREATE TABLE BOOKINGREQUESTS (
    bookingRequestID            int not null AUTO_INCREMENT,
	registeredUserID		    int,
    flightID                    int,
    requestStatus               ENUM('pending', 'completed'),
	PRIMARY KEY (bookingRequestID),
    FOREIGN KEY (registeredUserID) REFERENCES registeredusers(registeredUserID),
    FOREIGN KEY (flightID) REFERENCES flights(flightID)
);

INSERT INTO BOOKINGREQUESTS (bookingRequestID, registeredUserID, flightID, requestStatus) VALUES
(1, 1, 3, 'pending'),
(2, 2, 4, 'pending'),
(3, 3, 5, 'pending');


-- PERKS
DROP TABLE IF EXISTS PERKS;
CREATE TABLE PERKS (
    perkID            int not null AUTO_INCREMENT,
	perkType          ENUM('priceDiscount', 'other'),
    perkDescription   varchar(100), 
	discountVal       int,
    PRIMARY KEY (perkID)
);

INSERT INTO PERKS (perkID, perkType, perkDescription, discountVal) VALUES
(1, 'priceDiscount', '10% off flight price', 10),
(2, 'other', 'Free meal on flight.', null),
(3, 'other', 'Free airport lounge pass.', null),
(4, 'other', 'Hotel Voucher.', null),
(5, 'other', 'Airport Meal Voucher', null);



DROP USER IF EXISTS 'user'@'localhost';

CREATE USER 'user'@'localhost' IDENTIFIED BY  'password';

GRANT ALL PRIVILEGES ON AIRLINE.* TO 'user'@'localhost' WITH GRANT OPTION;

GRANT CREATE USER, RELOAD ON *.* TO 'user'@'localhost';

FLUSH PRIVILEGES;



