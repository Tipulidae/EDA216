## Authors
Axel Nyström, d14, mat04any@student.lu.se

Viktor Stagge, d14, ine12vst@student.lu.se

## Introduction
This project aims to create a planning system for the cookie production in Krusty Kookies Sweden AB. All relevant information regarding the production, orders, and deliveries is maintained in a local database. The software system is intended to be split into three parts, and only the one handling production is implemented here.

## Requirements
# Implemented
* Complete database
* Can add a pallet to database, of specified type and time of production.
* Can block all pallets containing a certain product, within specified time interval.
* Can check how many pallets that have been produced within a specified time interval.
* Can check how many pallets that have been produced of a specific type.
* Can check how many pallets that have been blocked.
* Can monitor the correct location of a pallet.
* Can remove a pallet from the freezer, if there is a valid order for it.
* Deducts material from storage when a pallet of cookies is produced.

# Unimplemented
* Can read/add/update/remove recipes.
* Can add/read/remove orders.
* Can add/read/remove material to storage.
* Can list all pallets that have been delivered to a specific customer, including time of delivery.
* Can create a loading order.
* Can create a loading bill.

## System Outline
sqlite3 is used to create the initial database, which is then manipulated through Java and JDBC. 

A custom database class implements a set amount of methods. The method arguments are used to query the database through prepared statements, which sanitizes the input and guarantees prevention of SQL-injections. 

## E/R diagram


## Relations

## SQL Statements
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

## User Manual
Superfluous.
