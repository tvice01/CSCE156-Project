
start transaction;

-- Delete tables if they already exist
drop table if exists Purchase;
drop table if exists Products;
drop table if exists Invoice;
drop table if exists Email;
drop table if exists Customer;
drop table if exists Person;
drop table if exists Address;
drop table if exists Country;
drop table if exists State;

-- Build the needed tables with the relevant fields
create table State (
	state_id int not null unique primary key auto_increment,
    name varchar(100) not null
);

create table Country (
	country_id int not null unique primary key auto_increment,
    name varchar(100) not null
);

create table Address (
	address_id int not null unique primary key auto_increment,
    street varchar(100) not null,
    city varchar(100),
    zip varchar(15),
    state_id int,
    country_id int not null,
    foreign key (state_id) references State(state_id),
    foreign key (country_id) references Country(country_id),
    constraint UC_Address unique (street, city, zip, state_id)
);

create table Person (
  person_id int not null unique primary key auto_increment,
  personCode varchar(45) not null unique,
  firstName varchar(100) not null,
  lastName varchar(100) not null,
  address_id int not null,
  foreign key (address_id) references Address(address_id)
);

create table Customer (
  customer_id int not null unique primary key auto_increment,
  customerCode varchar(45) not null unique,
  customerName varchar(100) not null,
  customerType varchar(10) not null, -- Indicates account type: business = B, personal = P
  address_id int not null,
  person_id int not null, -- Indicates the personCode of the person associated with the account
  foreign key (address_id) references Address(address_id),
  foreign key (person_id) references Person(person_id)
);

create table Email (
  email_id int not null unique primary key auto_increment,
  emailName varchar(100) not null unique,
  person_id int not null,
  foreign key (person_id) references Person(person_id)
);

create table Invoice (
  invoice_id int not null unique primary key auto_increment,
  invoiceCode varchar(45) not null unique,
  person_id int not null,
  customer_id int not null,
  foreign key (person_id) references Person(person_id),
  foreign key (customer_id) references Customer(customer_id)
);

create table Products (
  product_id int not null unique primary key auto_increment,
  productCode varchar(45) not null unique,
  productType varchar(10) not null, -- rental (R), repair (F), concession (C), towing (T)
  label varchar(100) not null,
  dailyCost float, -- Used with: rental (R)
  deposit float, -- Used with: rental (R)
  cleaningFee float, -- Used with: rental (R)
  partsCost float, -- Used with: repair (F)
  hourlyLaborCost float, -- Used with: repair (F)
  unitCost float, -- Used with: concession (C)
  costPerMile float -- Used with: towing (T)
);

create table Purchase (
  purchase_id int not null unique primary key auto_increment,
  invoice_id int not null,
  product_id int not null,
  daysRented float, -- Used with: rental (R)
  hoursWorked FLOAT, -- Used with: repair (F)
  quantity INT, -- Used with: concession (C)
  associatedRepair INT, -- Used with: concession (C), (only if another Product type is associated with same invoice_id)
  milesTowed FLOAT, -- Used with: towing (T)
  foreign key (invoice_id) references Invoice(invoice_id),
  foreign key (product_id) references Products(product_id),
  foreign key (associatedRepair) references Products(product_id),
  constraint UC_Purchase unique (invoice_id, product_id, daysRented, hoursWorked, quantity, associatedRepair, milesTowed)
);

-- Populate the tables with some data
insert into Country (name) values ("USA"), ("Canada"), ("Mexico"), ("Germany"), ("Russia"), ("China"), ("South Korea"), ("Japan");

insert into State (name) values ("MA"), ("CA"), ("NE"), ("WA"), ("NY"), ("IA"), ("NC"), ("NM"), ("KS"), ("IL"), ("TX"), ("MN"), ("WV"), ("Bavaria"), ("Ontario"), ("Saskatchewan"), ("Quebec");

insert into Address (street, city, zip, state_id, country_id) values
	("233 Bay State Road", "Boston", "02101", (select state_id from State where name = "MA"), (select country_id from Country where name = "USA")),
	("450 Serra Mall", "Stanford", "94305", (select state_id from State where name = "CA"), (select country_id from Country where name = "USA")),
	("1011 Jones Street", "Omaha", "68102", (select state_id from State where name = "NE"), (select country_id from Country where name = "USA")),
	("5801 N 33rd Street", "Lincoln", "68504", (select state_id from State where name = "NE"), (select country_id from Country where name = "USA")),
	("16255 NE 36th Way", "Redmond", "98052", (select state_id from State where name = "WV"), (select country_id from Country where name = "USA")),
	("1920 Farnam Street", "Omaha", "68102", (select state_id from State where name = "NE"), (select country_id from Country where name = "USA")),
	("66 Perry Street", "New York", "10014", (select state_id from State where name = "NY"), (select country_id from Country where name = "USA")),
	("5301 Rockford Drive", "Lincoln", "68521", (select state_id from State where name = "NE"), (select country_id from Country where name = "USA")),
	("101 R Street", "Sioux City", "51106", (select state_id from State where name = "IA"), (select country_id from Country where name = "USA")),
	("300 S Street", "Wilmington", "28403", (select state_id from State where name = "NC"), (select country_id from Country where name = "USA")),
	("100 W Fake Street", "Albaquerque", "80706", (select state_id from State where name = "NM"), (select country_id from Country where name = "USA")),
	("2740 Madison Avenue", "Lincoln", "68504", (select state_id from State where name = "NE"), (select country_id from Country where name = "USA")),
	("16255 NE 36th Way", "Redmond", "98052", (select state_id from State where name = "WA"), (select country_id from Country where name = "USA")),
	("126 Roosevelt Ave", "Overland Park", "66013", (select state_id from State where name = "KS"), (select country_id from Country where name = "USA")),
	("1060 West Addison Street", "Chicago", "60613", (select state_id from State where name = "IL"), (select country_id from Country where name = "USA")),
	("6200 Hermann Park Dr", "Houston", "77030", (select state_id from State where name = "TX"), (select country_id from Country where name = "USA")),
	("123 N 1st Street", "Lincoln", "68508", (select state_id from State where name = "NE"), (select country_id from Country where name = "USA"));

insert into Person (personCode, firstName, lastName, address_id) values
("ddb007", "Mary", "Grey", (select address_id from Address where address_id = (select address_id from Address where street = "233 Bay State Road" and city = "Boston" and zip = "02101" and state_id = (select state_id from State where name = "MA")))),
("adc001", "Shelly", "Smith", (select address_id from Address where address_id = (select address_id from Address where street = "450 Serra Mall" and city = "Stanford" and zip = "94305" and state_id = (select state_id from State where name = "CA")))),
("cba321", "Andrew", "Rodgers", (select address_id from Address where address_id = (select address_id from Address where street = "1011 Jones Street" and city = "Omaha" and zip = "68102" and state_id = (select state_id from State where name = "NE")))),
("564hom", "Alex", "Richards", (select address_id from Address where address_id = (select address_id from Address where street = "5801 N 33rd Street" and city = "Lincoln" and zip = "68504" and state_id = (select state_id from State where name = "NE")))),
("len890", "Ava", "Robbins", (select address_id from Address where address_id = (select address_id from Address where street = "16255 NE 36th Way" and city = "Redmond" and zip = "98052" and state_id = (select state_id from State where name = "WA")))),
("lyz8f7", "Skyler", "Dang", (select address_id from Address where address_id = (select address_id from Address where street = "1920 Farnam Street" and city = "Omaha" and zip = "68102" and state_id = (select state_id from State where name = "NE")))),
("pol9t7", "Abby", "Olsen", (select address_id from Address where address_id = (select address_id from Address where street = "66 Perry Street" and city = "New York" and zip = "10014" and state_id = (select state_id from State where name = "NY")))),
("sko420", "Cristina", "Bailey", (select address_id from Address where address_id = (select address_id from Address where street = "5301 Rockford Drive" and city = "Lincoln" and zip = "68521" and state_id = (select state_id from State where name = "NE")))),
("987fyg", "Derek", "Bishop", (select address_id from Address where address_id = (select address_id from Address where street = "101 R Street" and city = "Sioux City" and zip = "51106" and state_id = (select state_id from State where name = "IA")))),
("396fwb", "Haley", "James", (select address_id from Address where address_id = (select address_id from Address where street = "300 S Street" and city = "Wilmington" and zip = "28403" and state_id = (select state_id from State where name = "NC"))));

insert into Customer (customerCode, customerName, customerType, address_id, person_id) values
	("iow070", "State of Nebraska", "B", 
		(select address_id from Address where address_id = (select address_id from Address where street = "100 W Fake Street" and city = "Albaquerque" and zip = "80706" and state_id = (select state_id from State where name = "NM"))), 
		(select person_id from Person where personCode = "987fyg")),
	("rac998", "Cars", "P", 
		(select address_id from Address where address_id = (select address_id from Address where street = "2740 Madison Avenue" and city = "Lincoln" and zip = "68504" and state_id = (select state_id from State where name = "NE"))), 
		(select person_id from Person where personCode = "cba321")),
	("dem432", "Demolition Derby", "B", 
		(select address_id from Address where address_id = (select address_id from Address where street = "126 Roosevelt Ave" and city = "Overland Park" and zip = "66013" and state_id = (select state_id from State where name = "KS"))), 
		(select person_id from Person where personCode = "len890")),
	("smt109", "Smith Industries", "P", 
		(select address_id from Address where address_id = (select address_id from Address where street = "1060 West Addison Street" and city = "Chicago" and zip = "60613" and state_id = (select state_id from State where name = "IL"))), 
		(select person_id from Person where personCode = "adc001")),
	("tms421", "Smith Foundation", "B", 
		(select address_id from Address where address_id = (select address_id from Address where street = "6200 Hermann Park Dr" and city = "Houston" and zip = "77030" and state_id = (select state_id from State where name = "TX"))), 
		(select person_id from Person where personCode = "adc001")),
	("gre223", "Grey Sloan", "P", 
		(select address_id from Address where address_id = (select address_id from Address where street = "123 N 1st Street" and city = "Lincoln" and zip = "68508" and state_id = (select state_id from State where name = "NE"))), 
		(select person_id from Person where personCode = "sko420"));

insert into Email (emailName, person_id) values 
	("marygrey@gmail.com", (select person_id from Person where personCode = "ddb007")), ("mgrey1@yahoo.com", (select person_id from Person where personCode = "ddb007")),
    ("ssmith15@stanford.edu", (select person_id from Person where personCode = "adc001")),
    ("arodgers3@hotmail.com", (select person_id from Person where personCode = "cba321")),
    ("alexrichards@gmail.com", (select person_id from Person where personCode = "564hom")),
    ("avarobbins@gmail.com", (select person_id from Person where personCode = "len890")), ("arobbins@yahoo.com", (select person_id from Person where personCode = "len890")),
    ("skylerdang@hotmail.com", (select person_id from Person where personCode = "lyz8f7")),
    ("crisbailey@gmail.com", (select person_id from Person where personCode = "sko420")),
    ("derekbishop@morningside.edu", (select person_id from Person where personCode = "987fyg")),
    ("haleyjames@gmail.com", (select person_id from Person where personCode = "396fwb"));

insert into Invoice (invoiceCode, person_id, customer_id) values
	("INV001", (select person_id from Person where personCode = "987fyg"), (select customer_id from Customer where customerCode = "iow070")),
    ("INV002", (select person_id from Person where personCode = "564hom"), (select customer_id from Customer where customerCode = "smt109")),
    ("INV003", (select person_id from Person where personCode = "987fyg"), (select customer_id from Customer where customerCode = "gre223")),
    ("INV004", (select person_id from Person where personCode = "ddb007"), (select customer_id from Customer where customerCode = "dem432"));

-- Insert all Rental products into Products table
insert into Products (productCode, productType, label, dailyCost, deposit, cleaningFee) values
	("zxc3c", "R", "Ford Taurus", 75.0, 200.0, 35.0), ("0sli7", "R", "Porsche 911", 2500.0, 15000.0, 100.0);

-- Insert all Repair products into Products table
insert into Products (productCode, productType, label, partsCost, hourlyLaborCost) values
	("poiu8yt", "F", "Oil Change", 25.0, 30.0), ("86sdf7", "F", "Windshield Replacement", 200.0, 50.0);
    
-- Insert all Concession products into Products table
insert into Products (productCode, productType, label, unitCost) values 
	("oasn02d", "C", "Coffee", 1.75);
    
-- Insert all Towing products into Products table
insert into Products (productCode, productType, label, costPerMile) values
	("39msn", "T", "Tow > 20 miles", 5.5), ("bz4y32", "T", "Tow < 20 miles", 4.5);

-- Insert all Rental purchases into Purchase table
insert into Purchase (invoice_id, product_id, daysRented) values
	((select invoice_id from Invoice where invoiceCode = "INV002"), (select product_id from Products where productCode = "0sli7"), 14),
    ((select invoice_id from Invoice where invoiceCode = "INV003"), (select product_id from Products where productCode = "zxc3c"), 4);
    
-- Insert all Repair puchases into Purchase table
insert into Purchase (invoice_id, product_id, hoursWorked) values 
	((select invoice_id from Invoice where invoiceCode = "INV001"), (select product_id from Products where productCode = "poiu8yt"), 4.0),
    ((select invoice_id from Invoice where invoiceCode = "INV003"), (select product_id from Products where productCode = "86sdf7"), 1.0);
    
-- Insert all Concession purchases into Purchase table
insert into Purchase (invoice_id, product_id, quantity, associatedRepair) values 
	((select invoice_id from Invoice where invoiceCode = "INV001"), (select product_id from Products where productCode = "oasn02d"), 1, (select product_id from Products where productCode = "poiu8yt")),
    ((select invoice_id from Invoice where invoiceCode = "INV002"), (select product_id from Products where productCode = "oasn02d"), 3, null),
    ((select invoice_id from Invoice where invoiceCode = "INV003"), (select product_id from Products where productCode = "oasn02d"), 1, (select product_id from Products where productCode = "86sdf7")),
    ((select invoice_id from Invoice where invoiceCode = "INV004"), (select product_id from Products where productCode = "oasn02d"), 2, null);
    
-- Insert all Towing purchases into Purchase table
insert into Purchase (invoice_id, product_id, milesTowed) values 
	((select invoice_id from Invoice where invoiceCode = "INV003"), (select product_id from Products where productCode = "bz4y32"), 6.0);
    
commit;
