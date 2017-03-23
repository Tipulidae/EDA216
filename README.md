Axel Nyström
d14
mat04any@student.lu.se

Viktor Stagge
d14
ine12vst@student.lu.se

## Introduction

## Requirements

## System Outline

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
