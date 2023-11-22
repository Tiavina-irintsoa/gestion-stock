INSERT INTO unite (nomUnite, abreviation) VALUES
('Kilogramme', 'kg'),
('Tonne', 'T'),
('Litre', 'L'),
('Unité', 'U');
--fifo -1
--lifo 1

INSERT INTO article (idArticle, nomArticle, idunite, methodeStockage) VALUES
('R101', 'Riz Gasy', 1, -1),
('R1011', 'Riz Gasy Fotsy', 1, 1),
('A201', 'Ail en poudre', 1, -1),
('A202', 'Ail Frais', 1, 1);

INSERT INTO magasin (nomMagasin) VALUES
('TNR101'),
('TNR102'),
('TUL201');

INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree,  idmagasin,prixUnitaire, etat) VALUES
('R101', '2023-01-15', 100, 0, NULL, 1,4500,10),
('R101', '2022-02-20', 75, 0, NULL, 1,5000,10),
('A201', '2021-03-10', 150, 0, NULL, 1,3200,10),
('R1011', '2020-04-05', 50, 0, NULL, 2,3050,10),
('A202', '2020-05-12', 120, 0, NULL, 3,2500.5,10),
('R101', '2021-06-18', 80, 0, NULL, 3,6250.5,10),
('R1011', '2023-07-22', 200, 0, NULL, 1,2100,10),
('R101', '2023-06-03', 50, 0, NULL, 1,5550,10);

INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree,  idmagasin,prixUnitaire, etat) VALUES
('R101', '2023-11-15', 0, 10, 6, 3,0,0);


INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree,  idmagasin,prixUnitaire, etat) VALUES
('R101', '2022-10-08', 100, 0, NULL, 1,2000,10);

insert into validation values(1,'2023-01-15');
insert into validation values(2,'2022-02-20');
insert into validation values(3,'2021-03-10');
insert into validation values(4,'2020-04-05');
insert into validation values(5,'2020-05-12');
insert into validation values(6,'2021-06-18');
insert into validation values(7,'2023-07-22');
insert into validation values(8,'2023-06-03');


select v_entree.idmouvement, v_entree.quantite_entree - sum(v_sortie.quantite_sortie) as reste from v_entree left join v_sortie on v_sortie.entree = v_entree.idmouvement AND v_sortie.dateMouvement < '2023-06-01' where  v_entree.idMagasin =1 and v_entree.idarticle = 'R101' group by v_entree.idmouvement, v_entree.quantite_entree, v_entree.datemouvement order by v_entree.datemouvement  asc


INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, sortie, idmagasin,prixUnitaire) VALUES
('R1011', '2022-11-01', 200, 0, NULL, NULL, 2,5000);
INSERT INTO mouvement (idArticle, dateMouvement, quantite_entree, quantite_sortie, entree, sortie, idmagasin,prixUnitaire) VALUES
('R1011', '2021-11-01', 10, 0, NULL, NULL, 2,4500);

