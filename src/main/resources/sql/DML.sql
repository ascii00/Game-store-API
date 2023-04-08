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
    ('Witcher 3', 'It is the sequel to the 2011 game The Witcher 2: Assassins of Kings and the third game in The Witcher video game series, played in an open world with a third-person perspective', 159.99, 1),
    ('Diablo 2', 'Diablo II is an action role-playing hack-and-slash video game developed by Blizzard North and published by Blizzard Entertainment in 2000', 49.99, 3),
    ('Witcher 2', 'The Witcher 2: Assassins of Kings is a 2011 action role-playing video game', 89.99, 1),
    ('Cyberpunk 2077', 'Cyberpunk 2077 is a 2020 action role-playing video game developed by CD Projekt Red', 89.99, 1),
    ('FIFA 22', 'A soccer sports game.', 69.99, 5);

-- Role
INSERT INTO Role (name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');