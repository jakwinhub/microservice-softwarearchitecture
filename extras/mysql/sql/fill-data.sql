use mannschaftsmitglied;

-- Erstellen einer Tabelle für Entity Adresse
CREATE TABLE IF NOT EXISTS Adresse
(
  AdresseID    binary(16),
  Postleitzahl integer(5),
  Ort          varchar(50),
  PRIMARY KEY (AdresseID)
);
-- Erstellen einer Tabelle für Entity Mitglied
CREATE TABLE IF NOT EXISTS Mitglied
(
  ID            binary(16),
  Nachname      varchar(50),
  Mannschaft    int,
  Email         varchar(25),
  Newsletter    smallint,
  Geburtsdatum  date,
  Homepage      varchar(60),
  Geschlecht    varchar(1),
  Familienstand varchar(1),
  Adresse       varchar(80),
  PRIMARY KEY (ID),
  CONSTRAINT fk_Adresse FOREIGN KEY (Adresse)
    REFERENCES Adresse (AdresseID)
);
-- Erstellen einer Tabelle für Entity Familienstand
CREATE TABLE IF NOT EXISTS Familienstand
(
  Ledig       varchar(1),
  Verheiratet varchar(2),
  Geschieden  varchar(1),
  Verwitwet   varchar(2),
  MitgliedID  varchar(36),
  CONSTRAINT fk_MitgliedFamilienstand FOREIGN KEY (MitgliedID)
    REFERENCES Mitglied (ID)
);

-- Erstellen einer Tabelle für Entity Geschlecht
CREATE TABLE IF NOT EXISTS Geschlecht
(
  Maennlich  varchar(1),
  Weiblich   varchar(1),
  Divers     varchar(1),
  MitgliedID varchar(36),
  CONSTRAINT FK_Mitglied FOREIGN KEY (MitgliedID)
    REFERENCES Mitglied (ID)
);

-- Insert Statements

insert into Adresse (AdresseID, Postleitzahl, Ort)
values (UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000000), 00000, 'Aachen'),
       (UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000001), 11111, 'Augsburg'),
       (UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000002), 22222, 'Aalen'),
       (UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000030), 33333, 'Ahlen');

insert into Mitglied (ID, Nachname, Mannschaft, Email, Newsletter, Geburtsdatum, Homepage, Geschlecht, Familienstand,
                      Adresse)
values (UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000000), 'Admin', 0, 'admin@acme.com', true, '2022-02-31',
        'https://www.acme.com', 'w', 'v', UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000000)),
       (UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000001), 'Alpha', 1, 'alpha@acme.com', true, '2022-01-01',
        'https://www.acme.com', 'm', 'l', UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000001)),
       (UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000002), 'Alpha', 2, 'alpha@acme.edu', true, '2022-01-02',
        'https://www.acme.edu', 'w', 'gs', UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000002)),
       (UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000030), 'Aplha', 3, 'alpha@acme.com', true, '2022-01-03',
        'https://www.acme.ch', 'm', 'vw', UUID_TO_BIN(00000000 - 0000 - 0000 - 0000 - 000000000030));



