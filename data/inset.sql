INSERT INTO unite (nomUnite, abreviation) VALUES
('Tonne', 'T'),
('Litre', 'L'),
('Unité', 'U');

INSERT INTO article (idArticle, nomArticle, idunite, methodeStockage) VALUES
('R101', 'Vary mena', 1, 1),
('R111', 'Vary fotsy', 2, 2),
('R1011', 'Vary mena stock', 1, 1);

INSERT INTO magasin (nomMagasin) VALUES
('Magasin A'),
('Magasin B'),
('Magasin C');

INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, sortie, idmagasin) VALUES
('R101', '2023-11-01', 100, 0, NULL, NULL, 1),
('R101', '2023-11-02', 40, 0, NULL, NULL, 1),
('R1011', '2023-11-03', 90, 0, NULL, NULL, 1),
('R101', '2023-11-04', 0, 90, NULL, NULL, 1),
('R101', '2023-11-04', 0, 40, 2, 4, 1),
('R101', '2023-11-04', 0, 50, 1, 4, 1),
('R1011', '2023-11-05', 0, 50, NULL, NULL, 1),
('R1011', '2023-11-05', 0, 50, 3, 5, 1);









