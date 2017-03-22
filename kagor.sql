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
	blocked			boolean default 0,
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
INSERT INTO Recipes VALUES ('Nut cookie','Ground, roasted nuts',0.625);
INSERT INTO Recipes VALUES ('Nut cookie','Bread crumbs',0.125);
INSERT INTO Recipes VALUES ('Nut cookie','Sugar',0.375);
INSERT INTO Recipes VALUES ('Nut cookie','Egg whites',0.35);
INSERT INTO Recipes VALUES ('Nut cookie','Chocolate',0.05);

INSERT INTO Recipes VALUES ('Amneris','Marzipan',0.75);
INSERT INTO Recipes VALUES ('Amneris','Butter',0.25);
INSERT INTO Recipes VALUES ('Amneris','Eggs',0.25);
INSERT INTO Recipes VALUES ('Amneris','Potato starch',0.025);
INSERT INTO Recipes VALUES ('Amneris','Wheat flour',0.025);

INSERT INTO Recipes VALUES ('Tango','Butter',0.2);
INSERT INTO Recipes VALUES ('Tango','Sugar',0.25);
INSERT INTO Recipes VALUES ('Tango','Flour',0.3);
INSERT INTO Recipes VALUES ('Tango','Sodium bicarbonate',0.004);
INSERT INTO Recipes VALUES ('Tango','Vanilla',0.002);

INSERT INTO Recipes VALUES ('Almond delight','Butter',0.4);
INSERT INTO Recipes VALUES ('Almond delight','Sugar',0.27);
INSERT INTO Recipes VALUES ('Almond delight','Chopped almonds',0.279);
INSERT INTO Recipes VALUES ('Almond delight','Flour',0.4);
INSERT INTO Recipes VALUES ('Almond delight','Cinnamon',0.01);

INSERT INTO Recipes VALUES ('Berliner','Flour',0.35);
INSERT INTO Recipes VALUES ('Berliner','Butter',0.25);
INSERT INTO Recipes VALUES ('Berliner','Icing sugar',0.1);
INSERT INTO Recipes VALUES ('Berliner','Eggs',0.05);
INSERT INTO Recipes VALUES ('Berliner','Vanilla sugar',0.005);
INSERT INTO Recipes VALUES ('Berliner','Chocolate',0.05);



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

INSERT INTO Orders VALUES (1,'Köbekagor AB',1510198999124);
INSERT INTO OrderItems VALUES (1,'Berliner',3);
INSERT INTO OrderItems VALUES (1,'Almond delight',2);
INSERT INTO OrderItems VALUES (1,'Amneris',10);

INSERT INTO Orders VALUES (2,'Småbröd AB',1510198999124);
INSERT INTO OrderItems VALUES (2,'Tango',3);
INSERT INTO OrderItems VALUES (2,'Nut ring',1);
INSERT INTO OrderItems VALUES (2,'Nut cookie',5);

INSERT INTO Orders VALUES (3,'Köbekagor AB',1510198999124);
INSERT INTO OrderItems VALUES (3,'Tango',1);

INSERT INTO Orders VALUES (4,'Partykagor AB',1510198999124);
INSERT INTO OrderItems VALUES (4,'Nut ring',1);
INSERT INTO OrderItems VALUES (4,'Nut cookie',1);
INSERT INTO OrderItems VALUES (4,'Amneris',1);
INSERT INTO OrderItems VALUES (4,'Tango',1);
INSERT INTO OrderItems VALUES (4,'Almond delight',1);
INSERT INTO OrderItems VALUES (4,'Berliner',1);


INSERT INTO Orders VALUES (5,'Köbekagor AB',1520198999124);
INSERT INTO OrderItems VALUES (5,'Nut ring',100);
INSERT INTO OrderItems VALUES (5,'Nut cookie',100);
INSERT INTO OrderItems VALUES (5,'Amneris',100);
INSERT INTO OrderItems VALUES (5,'Tango',100);
INSERT INTO OrderItems VALUES (5,'Almond delight',100);
INSERT INTO OrderItems VALUES (5,'Berliner',100);

INSERT INTO Pallets VALUES (1,null,'Nut ring','FREEZER',1490198999124,0);
INSERT INTO Pallets VALUES (2,null,'Nut ring','FREEZER',1450198999124,0);
INSERT INTO Pallets VALUES (3,null,'Nut ring','FREEZER',1410198999124,0);
INSERT INTO Pallets VALUES (4,null,'Nut ring','FREEZER',1370198999124,0);
INSERT INTO Pallets VALUES (5,null,'Nut ring','FREEZER',1330198999124,0);

INSERT INTO Pallets VALUES (6,5,'Nut cookie','DELIVERED',1490198999124,0);
INSERT INTO Pallets VALUES (7,null,'Nut cookie','FREEZER',1490198999124,0);
INSERT INTO Pallets VALUES (8,null,'Nut cookie','FREEZER',1490198999124,0);
INSERT INTO Pallets VALUES (9,null,'Nut cookie','FREEZER',1490198999124,0);
INSERT INTO Pallets VALUES (10,null,'Nut cookie','FREEZER',1490198999124,0);

INSERT INTO Pallets VALUES (11,null,'Berliner','FREEZER',1490198999124,1);

INSERT INTO Pallets VALUES (12,null,'Almond delight','FREEZER',-6000000000000,0);





