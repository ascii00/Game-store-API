INSERT INTO Game_Type (name)
VALUES ('RPG'),
       ('Action'),
       ('Adventure'),
       ('Strategy');

INSERT INTO Game (name, description, price, game_type_ID)
VALUES ('The Witcher', 'A fantasy RPG game developed by CD Projekt Red, based on the book series by Andrzej Sapkowski. Players control Geralt of Rivia, a monster hunter known as a Witcher.', 19.99, 1),
       ('The Witcher 2: Assassins of Kings', 'A sequel to The Witcher, this game expands on the story and world of the first game. Players continue to control Geralt of Rivia as he navigates the dangerous world of politics and monsters.', 29.99, 1),
       ('The Witcher 3: Wild Hunt', 'The third installment in the series, Wild Hunt is an open-world RPG that follows Geralt on his most personal and expansive quest yet.', 39.99, 1),
       ('Cyberpunk 2077', 'An open-world, action-adventure game from CD Projekt Red, set in the dystopian Night City. Players control a character named V, a mercenary with the ability to customize their abilities and appearance.', 59.99, 3),
       ('Diablo 2', 'An action role-playing game developed by Blizzard North, released in 2000. Players choose one of several character classes and embark on an epic journey to save the world of Sanctuary from the forces of evil.', 9.99, 2),
       ('Diablo 2: Lord of Destruction', 'An expansion pack for Diablo 2, introducing two new character classes, a fifth act, and new items and features.', 9.99, 2);

INSERT INTO Role (name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');