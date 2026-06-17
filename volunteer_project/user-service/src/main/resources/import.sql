INSERT INTO users (user_type, id, username, email, password, address, city, postalCode, phone, afm, organizationName, description, mission, foundedYear, firstName, lastName)
VALUES ('ORGANIZATION', 1, 'org1', 'org1@example.com', 'secret', 'Stadiou 10', 'Athens', '10562', '2100000000', '123456789', 'Help Org', 'Community support organization', 'Volunteer actions and social support', 2010, NULL, NULL);

INSERT INTO users (user_type, id, username, email, password, address, city, postalCode, phone, afm, organizationName, description, mission, foundedYear, firstName, lastName)
VALUES ('VOLUNTEER', 5, 'volunteer5', 'volunteer5@example.com', 'secret', 'Patision 10', 'Athens', '10434', '6900000005', NULL, NULL, NULL, NULL, NULL, 'Nikos', 'Papadopoulos');

INSERT INTO users (user_type, id, username, email, password, address, city, postalCode, phone, afm, organizationName, description, mission, foundedYear, firstName, lastName)
VALUES ('VOLUNTEER', 6, 'volunteer6', 'volunteer6@example.com', 'secret', 'Akadimias 20', 'Athens', '10671', '6900000006', NULL, NULL, NULL, NULL, NULL, 'Maria', 'Georgiou');

ALTER TABLE users ALTER COLUMN id RESTART WITH 20;
