/* 
 * CSCE 156, Assignment 4 Test Queries
 * Authors: Treyvor Vice, Ann Lee
*/

-- # 1: A query to retrieve the major fields for every person
	-- Columns: person_id, personCode, lastName, firstName, address, email
	select p.person_id, p.personCode, p.lastName, p.firstName, a.street, a.city, a.zip, s.name as state, c.name as country, e.emailName as "primary email"
	from Person p
	join Address a on p.address_id = a.address_id
	join State s on a.state_id = s.state_id
	join Country c on a.country_id = c.country_id
	left join Email e on p.person_id = e.person_id
    group by p.person_id;


-- # 2 A query to retrieve the email(s) of a given person (ary Grey, code: ddb007)
	select p.personCode, p.lastName, p.firstName, e.emailName as email
	from Person p
	left join Email e on p.person_id = e.person_id
	where p.personCode = "ddb007";


-- # 3: A query to add an email to a specific person (add some_new@email.address to Abby Olsen, code: pol9t7)
	insert into Email (emailName, person_id) values
	("some_new@email.address", (select person_id from Person where personCode = "pol9t7"));


-- # 4: A query to change the email address of a given email record (change arobbins@yahoo.com to new@email.address)
	update Email set emailName = "new@email.address" 
	where emailName = "arobbins@yahoo.com";


-- # 5: A query (or series of queries) to remove a given person record (Mary Grey, code: adc001)
	-- first delete the email addresses associated with the person
	delete from Email 
	where person_id = (
		select person_id from Person where personCode = "adc001"
	);

	-- next delete the purchases associated with the invoices associated with the person
	delete from Purchase
	where invoice_id = (
		select invoice_id from Invoice 
        where person_id = (
			select person_id from Person where personCode = "adc001"
		)
	);
    
	-- delete the invoices associated with the person
	delete from Invoice
	where person_id = (
		select person_id from Person where personCode = "adc001"
	);

	-- delete the PersonCustomer entries associated with the person
    delete from PersonCustomer
    where person_id = (
		select person_id from Person where personCode = "adc001"
	);
    
    -- finally, delete the person record itself
    delete from Person
    where personCode = "adc001";


-- # 6: A query to create a person record (imaC0d3, John Doe, 123 New Street, Big City, 8675309, Quebec, Canada, jdoe@email.com)
	-- start by creating a new Address
	insert into Address (street, city, zip, state_id, country_id) values
		("123 New Street", "Big City", 8675309, 
			(select state_id from State where name = "Quebec"),
            (select country_id from Country where name = "Canada")
		);
        
	-- now create the new Person
    insert into Person (personCode, firstName, lastName, address_id) values
		("imaC0d3", "John", "Doe",
			(select address_id from Address
            where street = "123 New Street" and city = "Big City" and zip = 8675309 and state_id = (
				select state_id from State where name = "Quebec"
			))
		);
        
	-- lastly, add new Email(s)
	insert into Email (emailName, person_id) values
		("jdoe@email.com", (select person_id from Person where personCode = "imaC0d3"));
        

-- # 7: A query to get all the products in a particular invoice (invoice code: INV002)	
    select i.invoiceCode, prod.productCode, prod.productType, prod.label as product, purch.daysRented, purch.hoursWorked, purch.quantity, purch.milesTowed
    from Invoice i
    join Purchase purch on i.invoice_id = purch.invoice_id
    join Products prod on purch.product_id = prod.product_id
    where i.invoiceCode = "INV002";
    
    
-- # 8: A query to get all the products of a particular person (Derek Bishop, code: 987fyg)
	select pers.personCode, prod.productCode, prod.productType, prod.label as product, purch.daysRented, purch.hoursWorked, purch.quantity, purch.milesTowed
    from Person pers
    left join Invoice i on pers.person_id = i.person_id
    left join Purchase purch on i.invoice_id = purch.invoice_id
    left join Products prod on purch.product_id = prod.product_id
    where pers.personCode = "987fyg";
    

-- # 9: A query to get all the invoices of a particular owner (Derek Bishop, code: 987fyg)
	select pers.personCode, i.invoiceCode, c.customerCode, c.customerName as account
    from Person pers
    left join Invoice i on pers.person_id = i.person_id
    left join Customer c on i.customer_id = c.customer_id
    where pers.personCode = "987fyg";
    
    
-- # 10: A query that “adds” a particular product to a particular invoice 
	-- invoice: INV004, product: Martini (code: asd93d), quantity: 2
    insert into Purchase (invoice_id, product_id, quantity) values
		(
			(select invoice_id from Invoice where invoiceCode = "INV004"),
            (select product_id from Products where productCode = "asd93d"),
            2
		);
        
	
-- # 11: A query to find the total of all unitCost of all Concession products
	select sum(unitCost) as "Cost of one of each Concession"
    from Products
    where productType = "C";
    

-- # 12: A query to find the customers with more than 2 invoices
	select c.customerCode, c.customerName, count(i.customer_id) as "Number of invoices"
    from Invoice i
    join Customer c on i.customer_id = c.customer_id
    group by i.customer_id
    having count(i.customer_id) > 2;
    
    
-- # 13: A query to find the number of invoices that include Repair as one of the products in the invoice
	select count(distinct purch.invoice_id) as "Number of invoices containing a repair"
    from Purchase purch
    join Products prod on purch.product_id = prod.product_id
    group by prod.productType
    having prod.productType = "F";
    
    
-- # 14: A query to find the total revenue generated (excluding fees and taxes) by towing from all invoices	
	select cast(sum(purch.milesTowed * prod.costPerMile) as decimal(10,2)) as "Total revenue from towings ($)"
    from Purchase purch
    join Products prod on purch.product_id = prod.product_id
    where prod.productType = "T";
    

-- #15: Write a query to find any invoice that includes multiple products of the same type
	select i.invoiceCode as "Invoices with multiple products of same type"
    from Invoice i
    join Purchase purch on i.invoice_id = purch.invoice_id
    join Products prod on purch.product_id = prod.product_id
    group by i.invoiceCode
    having count(distinct prod.productType) < count(prod.productType);
