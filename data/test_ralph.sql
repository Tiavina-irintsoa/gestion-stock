INSERT INTO unite (nomUnite, abreviation) VALUES
('Kilogramme', 'kg'),
('Tonne', 'T'),
('Litre', 'L'),
('Unité', 'U');

INSERT INTO article (idArticle, nomArticle, idunite, methodeStockage) VALUES
('R11', 'Riz rouge', 1, 1),
('R12', 'Riz blanc', 1, -1),
('R111', 'Riz rose', 1, 1);

INSERT INTO magasin (nomMagasin) VALUES
('M1'),
('M2'),
('M3');

INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, sortie, idmagasin,prixUnitaire) VALUES
('R11', '2019-08-07', 300, 0, NULL, NULL, 1,2000),
('R12', '2019-08-07', 400, 0, NULL, NULL, 1,2500),
('R111', '2019-08-07', 800, 0, NULL, NULL, 2,4000),
('R12', '2019-08-08', 500, 0, NULL, NULL, 1,2500),
('R11', '2019-12-12', 100, 0, NULL, NULL, 1,3500),
('R12', '2020-01-01', 200, 0, NULL, NULL, 1,3000),
('R11', '2020-01-06', 150, 0, NULL, NULL, 2,4000);

INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, sortie, idmagasin,prixUnitaire) VALUES
('R11', '2019-08-08', 0, 200, NULL, NULL, 1,0),
('R11', '2019-08-08', 0, 200, 1, 8, 1,0),
('R12', '2019-08-08', 0, 400, NULL, NULL, 1,0),
('R12', '2019-08-08', 0, 400, 2, 10, 1,0),
('R11', '2019-12-13', 0, 200, NULL, NULL, 1,0),
('R11', '2019-12-13', 0, 100, 1, 12, 1,0),
('R11', '2019-12-13', 0, 100, 5, 12, 1,0),
('R12', '2020-01-02', 0, 600, NULL, NULL, 1,0),
('R12', '2020-01-02', 0, 500, 4, 15, 1,0),
('R12', '2020-01-02', 0, 100, 7, 15, 1,0),
('R111', '2019-08-08', 0, 750, NULL,NULL , 2,0),
('R111', '2019-08-08', 0, 750, 3,18, 2,0);