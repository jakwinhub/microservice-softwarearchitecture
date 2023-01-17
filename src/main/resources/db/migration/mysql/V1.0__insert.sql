use mannschaftsmitglied;


CREATE TABLE IF NOT EXISTS Adresse
(
  ID           binary(16),
  Plz          Int(5),
  Ort          varchar(50),
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Mitglied
(
  ID            binary(16),
  Nachname      varchar(50),
  Mannschaft    int,
  Email         varchar(25),
  Geburtsdatum  date,
  Homepage      varchar(60),
  Geschlecht    varchar(1),
  Familienstand varchar(2),
  Fussballverein_ID binary(16),
  erzeugt       date,
  aktualisiert  date,
  version       int NOT NULL default 0,
  Adresse_ID    binary(16) REFERENCES Adresse,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Familienstand
(
  Ledig       varchar(2),
  Verheiratet varchar(2),
  Geschieden  varchar(2),
  Verwitwet   varchar(2),
  Mitglied_ID binary(16) REFERENCES Mitglied
);


CREATE TABLE IF NOT EXISTS Geschlecht
(
  Maennlich   varchar(1),
  Weiblich    varchar(1),
  Divers      varchar(1),
  Mitglied_ID varchar(16) REFERENCES Mitglied
);



insert into Adresse (ID, Plz, Ort)
values (UUID_TO_BIN('00000000-0000-0000-0000-000000000000'), 70776, 'Aachen'),
       (UUID_TO_BIN('00000000-0000-0000-0000-000000000001'), 11111, 'Augsburg'),
       (UUID_TO_BIN('00000000-0000-0000-0000-000000000002'), 22222, 'Aalen'),
       (UUID_TO_BIN('00000000-0000-0000-0000-000000000030'), 33333, 'Ahlen');

insert into Mitglied (ID, Nachname, Mannschaft, Email, Geburtsdatum, Homepage, Geschlecht, Familienstand,
                      Adresse_ID, Fussballverein_ID)
values (UUID_TO_BIN('00000000-0000-0000-0000-000000000000'), 'Admin', 0, 'admin@acme.com',  '2022-02-06',
        'https://www.acme.com', 'w', 'vh', UUID_TO_BIN('00000000-0000-0000-0000-000000000000'), UUID_TO_BIN('00000000-0000-0000-0000-000000000000')),
       (UUID_TO_BIN('00000000-0000-0000-0000-000000000001'), 'Alpha', 1, 'alpha@acme.com',  '2022-01-01',
        'https://www.acme.com', 'm', 'l', UUID_TO_BIN('00000000-0000-0000-0000-000000000001'),  UUID_TO_BIN('00000000-0000-0000-0000-000000000001')),
       (UUID_TO_BIN('00000000-0000-0000-0000-000000000002'), 'Alpha', 2, 'alpha@acme.edu',  '2022-01-02',
        'https://www.acme.edu', 'w', 'g', UUID_TO_BIN('00000000-0000-0000-0000-000000000002'),  UUID_TO_BIN('00000000-0000-0000-0000-000000000002')),
       (UUID_TO_BIN('00000000-0000-0000-0000-000000000030'), 'Aplha', 3, 'alpha@acme.com',  '2022-01-03',
        'https://www.acme.ch', 'm', 'vw', UUID_TO_BIN('00000000-0000-0000-0000-000000000030'),  UUID_TO_BIN('00000000-0000-0000-0000-000000000030'));



