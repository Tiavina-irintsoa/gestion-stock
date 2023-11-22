create table unite (
    idunite serial primary key, 
    nomUnite varchar,
    abreviation varchar
);
create table article (
    idArticle varchar primary key, 
    nomArticle varchar,
    idunite  integer references unite(idunite),
    methodeStockage integer
); 
create table magasin (
    idmagasin serial primary key, 
    nomMagasin varchar
);
create table mouvement (
    idmouvement serial primary key, 
    idArticle varchar references article(idArticle),
    dateMouvement date,
    quantite_entree numeric,
    quantite_sortie numeric,
    entree integer references mouvement(idmouvement), 
    idmagasin integer references magasin(idmagasin), 
    prixUnitaire numeric,
    etat integer 
);
create table validation(
    idmouvement integer references mouvement(idmouvement),
     dateValidation timestamp
);