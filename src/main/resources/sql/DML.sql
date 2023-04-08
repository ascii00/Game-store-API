--  Game_Type
INSERT INTO Game_Type (ID, name)
VALUES
    (1, 'Action'),
    (2, 'Adventure'),
    (3, 'Strategy'),
    (4, 'Simulation'),
    (5, 'Sports');

-- Game
INSERT INTO Game (name, description, price, game_type_ID)
VALUES
    ('Grand Theft Auto V', 'An action-packed open-world game.', 59.99, 1),
    ('The Legend of Zelda: Breath of the Wild', 'An immersive adventure game.', 49.99, 2),
    ('Civilization VI', 'A turn-based strategy game.', 39.99, 3),
    ('The Sims 4', 'A life simulation game.', 29.99, 4),
    ('FIFA 22', 'A soccer sports game.', 69.99, 5);

-- Role
INSERT INTO Role (name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');