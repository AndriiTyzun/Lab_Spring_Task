CREATE TABLE User_entity (
                             ID BIGSERIAL PRIMARY KEY,
                             First_Name VARCHAR(50) NOT NULL,
                             Last_Name VARCHAR(50) NOT NULL,
                             Username VARCHAR(50) NOT NULL UNIQUE,
                             Password VARCHAR(100) NOT NULL,
                             IsActive BOOLEAN NOT NULL
);

CREATE TABLE Trainee (
                         ID BIGSERIAL PRIMARY KEY,
                         Date_Of_Birth DATE,
                         Address VARCHAR(255),
                         UserID INTEGER UNIQUE,
                         FOREIGN KEY (UserID) REFERENCES User_entity(ID) ON DELETE CASCADE
);

CREATE TABLE Trainer (
                         ID BIGSERIAL PRIMARY KEY,
                         Specialization_ID INTEGER UNIQUE,
                         UserID INTEGER UNIQUE,
                         FOREIGN KEY (UserID) REFERENCES User_entity(ID) ON DELETE CASCADE,
                         FOREIGN KEY (Specialization_ID) REFERENCES Training_Type(ID)
);

CREATE TABLE Training_Type (
                               ID BIGSERIAL PRIMARY KEY,
                               Training_Type_Name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Training (
                          ID BIGSERIAL PRIMARY KEY,
                          Trainee_ID BIGINT,
                          Trainer_ID BIGINT,
                          Training_Name VARCHAR(100) NOT NULL,
                          Training_Type_ID BIGINT NOT NULL,
                          Training_Date DATE NOT NULL,
                          Training_Duration INTEGER NOT NULL,
                          FOREIGN KEY (Trainee_ID) REFERENCES Trainee(ID) ON DELETE CASCADE,
                          FOREIGN KEY (Trainer_ID) REFERENCES Trainer(ID) ON DELETE CASCADE,
                          FOREIGN KEY (Training_Type_ID) REFERENCES Training_Type(ID)
);

CREATE TABLE Trainer_Trainee (
                                 Trainer_ID BIGINT,
                                 Trainee_ID BIGINT,
                                 PRIMARY KEY (Trainer_ID, Trainee_ID),
                                 FOREIGN KEY (Trainer_ID) REFERENCES Trainer(ID) ON DELETE CASCADE,
                                 FOREIGN KEY (Trainee_ID) REFERENCES Trainee(ID) ON DELETE CASCADE
);

INSERT INTO Training_Type (Training_Type_Name) VALUES
                                                   ('Strength'),
                                                   ('Balance'),
                                                   ('Functional'),
                                                   ('Mobility'),
                                                   ('Agility');