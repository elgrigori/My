INSERT INTO participations (id, volunteerId, actionId, type, amount, productsSummary, startDate, endDate, status, notificationMessage, notificationHistory, notificationRead)
VALUES (1, 5, 1, 'ACTIVISM', NULL, NULL, '2026-06-10 10:00:00', '2026-06-10 14:00:00', 'CONFIRMED', 'Confirmation sent to volunteer5@example.com for action Beach cleanup', 'Confirmation sent to volunteer5@example.com for action Beach cleanup', FALSE);

INSERT INTO participations (id, volunteerId, actionId, type, amount, productsSummary, startDate, endDate, status, notificationMessage, notificationHistory, notificationRead)
VALUES (2, 5, 2, 'FUNDING', 25.00, NULL, '2026-06-10 10:00:00', '2026-06-30 18:00:00', 'CONFIRMED', 'Confirmation sent to volunteer5@example.com for action Food bank funding', 'Confirmation sent to volunteer5@example.com for action Food bank funding', FALSE);

INSERT INTO participations (id, volunteerId, actionId, type, amount, productsSummary, startDate, endDate, status, notificationMessage, notificationHistory, notificationRead)
VALUES (3, 6, 3, 'CONTRIBUTE', NULL, 'Rice:5,Milk:2', '2026-06-10 10:00:00', '2026-06-30 18:00:00', 'CONFIRMED', 'Confirmation sent to volunteer6@example.com for action Food collection', 'Confirmation sent to volunteer6@example.com for action Food collection', FALSE);

ALTER TABLE participations ALTER COLUMN id RESTART WITH 20;
