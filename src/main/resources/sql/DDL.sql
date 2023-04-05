CREATE TABLE Game (
    ID int  NOT NULL AUTO_INCREMENT,
    name varchar(50)  NOT NULL,
    description varchar(500)  NOT NULL,
    price double(6,2)  NOT NULL,
    game_type_ID int  NOT NULL,
    CONSTRAINT Game_pk PRIMARY KEY (ID)
);

-- Table: Game_Type
CREATE TABLE Game_Type (
    ID int  NOT NULL AUTO_INCREMENT,
    name varchar(20)  NOT NULL,
    CONSTRAINT Game_Type_pk PRIMARY KEY (ID)
);

-- Table: Role
CREATE TABLE Role (
    ID int  NOT NULL AUTO_INCREMENT,
    name varchar(20)  NOT NULL,
    CONSTRAINT Role_pk PRIMARY KEY (ID)
);

-- Table: Token
CREATE TABLE Token (
    ID int  NOT NULL AUTO_INCREMENT,
    token varchar(500)  NOT NULL,
    token_type varchar(50)  NOT NULL,
    expired bool  NOT NULL,
    revoked bool  NOT NULL,
    user_ID int  NOT NULL,
    CONSTRAINT Token_pk PRIMARY KEY (ID)
);

-- Table: User
CREATE TABLE User (
    ID int  NOT NULL AUTO_INCREMENT,
    email varchar(50)  NOT NULL,
    password varchar(500)  NOT NULL,
    confirmed bool  NOT NULL,
    CONSTRAINT User_pk PRIMARY KEY (ID)
);

-- Table: User_Role
CREATE TABLE User_Role (
    user_ID int  NOT NULL,
    role_ID int  NOT NULL,
    CONSTRAINT User_Role_pk PRIMARY KEY (user_ID,role_ID)
);

-- foreign keys
-- Reference: Game_Game_Type (table: Game)
ALTER TABLE Game ADD CONSTRAINT Game_Game_Type FOREIGN KEY Game_Game_Type (game_type_ID)
    REFERENCES Game_Type (ID);

-- Reference: Token_User (table: Token)
ALTER TABLE Token ADD CONSTRAINT Token_User FOREIGN KEY Token_User (user_ID)
    REFERENCES User (ID);

-- Reference: User_Role_Role (table: User_Role)
ALTER TABLE User_Role ADD CONSTRAINT User_Role_Role FOREIGN KEY User_Role_Role (role_ID)
    REFERENCES Role (ID);

-- Reference: User_Role_User (table: User_Role)
ALTER TABLE User_Role ADD CONSTRAINT User_Role_User FOREIGN KEY User_Role_User (user_ID)
    REFERENCES User (ID);