-- Delete the tables if they exist.
-- Disable foreign key checks, so the tables can 
-- be dropped in arbitrary order.
PRAGMA foreign_keys=OFF;
DROP TABLE IF EXISTS Ingredients;
DROP TABLE IF EXISTS Cookies;
DROP TABLE IF EXISTS Pallets;
DROP TABLE IF EXISTS Recipes;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS OrderItems;

PRAGMA foreign_keys=ON;

-- Create the tables.
CREATE TABLE Ingredients (
	ingredient			varchar(32) not null,
	amount				numeric not null,
	lastDelivery		datetime,
	deliveredAmount	numeric,
	primary key (ingredient)
);

CREATE TABLE Cookies (
	cookieName		varchar(32) not null,
	primary key (cookieName)
);

CREATE TABLE Pallets (
	palletId			integer not null,
	orderId			integer,
	cookieName		varchar(32) not null,
	location			varchar(32)	not null,
	timestamp		datetime not null,
	blocked			boolean default false,
	primary key (palletId),
	foreign key (cookieName) references Cookies(cookieName),
	foreign key (orderId) references Orders(orderId)
);


CREATE TABLE Recipes (
	cookieName		varchar(32) not null,
	ingredient		varchar(32) not null,
	quantity			numeric not null,
	primary key (cookieName,ingredient),
	foreign key (cookieName) references Cookies(cookieName),
	foreign key	(ingredient) references Ingredients(ingredient)
);

CREATE TABLE Customers (
	customerName	varchar(32) not null,
	address			varchar(32) not null,
	primary key (customerName)
);

CREATE TABLE Orders (
	orderId			integer not null,
	customerName	varchar(32) not null,
	deliveryTime	datetime not null,
	primary key (orderId),
	foreign key (customerName) references Customers(customerName)
);

CREATE TABLE OrderItems (
	orderId			integer not null,
	cookieName		varchar(32) not null,
	quantity			numeric not null,
	primary key (orderId,cookieName),
	foreign key (orderId) references Orders(orderId),
	foreign key (cookieName) references Cookies(cookieName)
);


INSERT INTO Ingredients VALUES ('Flour', 100, null, null);
INSERT INTO Ingredients VALUES ('Butter', 100, null, null);
INSERT INTO Ingredients VALUES ('Icing sugar', 100, null, null);
INSERT INTO Ingredients VALUES ('Roasted, chopped nuts', 100, null, null);
INSERT INTO Ingredients VALUES ('Fine-ground nuts', 100, null, null);
INSERT INTO Ingredients VALUES ('Ground, roasted nuts', 100, null, null);
INSERT INTO Ingredients VALUES ('Bread crumbs', 100, null, null);
INSERT INTO Ingredients VALUES ('Sugar', 100, null, null);
INSERT INTO Ingredients VALUES ('Egg whites', 100, null, null);
INSERT INTO Ingredients VALUES ('Chocolate', 100, null, null);
INSERT INTO Ingredients VALUES ('Marzipan', 100, null, null);
INSERT INTO Ingredients VALUES ('Eggs', 100, null, null);
INSERT INTO Ingredients VALUES ('Potato starch', 100, null, null);
INSERT INTO Ingredients VALUES ('Wheat flour', 100, null, null);
INSERT INTO Ingredients VALUES ('Sodium bicarbonate', 100, null, null);
INSERT INTO Ingredients VALUES ('Vanilla', 100, null, null);
INSERT INTO Ingredients VALUES ('Chopped almonds', 100, null, null);
INSERT INTO Ingredients VALUES ('Cinnamon', 100, null, null);
INSERT INTO Ingredients VALUES ('Vanilla sugar', 100, null, null);



INSERT INTO Cookies VALUES ('Nut ring');
INSERT INTO Cookies VALUES ('Nut cookie');
INSERT INTO Cookies VALUES ('Amneris');
INSERT INTO Cookies VALUES ('Tango');
INSERT INTO Cookies VALUES ('Almond delight');
INSERT INTO Cookies VALUES ('Berliner');


--INSERT INTO Pallets 

INSERT INTO Recipes VALUES ('Nut ring','Flour',0.45);
INSERT INTO Recipes VALUES ('Nut ring','Butter',0.45);
INSERT INTO Recipes VALUES ('Nut ring','Icing sugar',0.19);
INSERT INTO Recipes VALUES ('Nut ring','Roasted, chopped nuts',0.225);

INSERT INTO Recipes VALUES ('Nut cookie','Fine-ground nuts',0.75);



INSERT INTO Customers VALUES ('Finkagor AB', 'Helsingborg');
INSERT INTO Customers VALUES ('Köbekagor AB', 'Hassle-Bösarp');
INSERT INTO Customers VALUES ('Småbröd AB', 'Malmö');
INSERT INTO Customers VALUES ('Kaffebröd AB', 'Landskrona');
INSERT INTO Customers VALUES ('Bjudkagor AB', 'Ystad');
INSERT INTO Customers VALUES ('Kalaskagor AB', 'Trelleborg');
INSERT INTO Customers VALUES ('Partykagor AB', 'Kristianstad');
INSERT INTO Customers VALUES ('Gästkagor AB', 'Hässleholm');
INSERT INTO Customers VALUES ('Skånekagor AB', 'Perstorp');


--INSERT INTO Orders VALUES ();
--INSERT INTO OrderItems VALUES ();
















