INSERT INTO unite (nomUnite, abreviation) VALUES
('Tonne', 'T'),
('Litre', 'L'),
('Unité', 'U');

INSERT INTO article (idArticle, nomArticle, idunite, methodeStockage) VALUES
('R101', 'Vary mena', 1, -1),
('R111', 'Vary fotsy', 2, 2),
('R1011', 'Vary mena stock', 1, 1);

INSERT INTO magasin (nomMagasin) VALUES
('Magasin A'),
('Magasin B'),
('Magasin C');

INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, idmagasin,prixUnitaire,etat) VALUES
('R101', '2023-11-01', 50.5, 0,NULL, 1,1000,10),
('R101', '2023-11-02', 15, 0,NULL, 1,1200,10),
('R101', '2023-11-02', 5, 0,NULL, 1,1050,10);

INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, idmagasin,prixUnitaire,etat) VALUES
('R1011', '2023-11-01', 50.5, 0,NULL, 1,1000,10),
('R1011', '2023-11-02', 15, 0,NULL, 1,1200,10),
('R1011', '2023-11-02', 5, 0,NULL, 1,1050,10);

insert into validation values
(62,'2023-11-01'),
(63,'2023-11-02'),
(64,'2023-11-03');