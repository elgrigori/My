INSERT INTO actions (action_type, id, title, description, startDate, endDate, location, category, organizationId, status, currentParticipants, minParticipants, maxParticipants, requiredItems, targetAmount, raisedAmount)
VALUES ('ACTIVISM', 1, 'Beach cleanup', 'Clean the beach with local volunteers', '2026-06-10 10:00:00', '2026-06-10 14:00:00', 'Athens', 'community', 1, 'OPEN', 1, 2, 20, NULL, NULL, NULL);

INSERT INTO actions (action_type, id, title, description, startDate, endDate, location, category, organizationId, status, currentParticipants, minParticipants, maxParticipants, requiredItems, targetAmount, raisedAmount)
VALUES ('FUNDING', 2, 'Food bank funding', 'Raise money for the food bank', '2026-06-10 10:00:00', '2026-06-30 18:00:00', 'Athens', 'funding', 1, 'OPEN', 0, NULL, NULL, NULL, 5000.00, 0.00);

INSERT INTO actions (action_type, id, title, description, startDate, endDate, location, category, organizationId, status, currentParticipants, minParticipants, maxParticipants, requiredItems, targetAmount, raisedAmount)
VALUES ('CONTRIBUTE', 3, 'Food collection', 'Collect basic products', '2026-06-10 10:00:00', '2026-06-30 18:00:00', 'Athens', 'food', 1, 'OPEN', 0, NULL, NULL, 'Rice, Milk', NULL, NULL);

INSERT INTO action_products (action_id, name, targetQuantity, remainingQuantity)
VALUES (3, 'Rice', 100, 100);

INSERT INTO action_products (action_id, name, targetQuantity, remainingQuantity)
VALUES (3, 'Milk', 50, 50);

ALTER TABLE actions ALTER COLUMN id RESTART WITH 20;
