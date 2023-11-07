create or replace view v_mouvement as (
    select
    * 
    from mouvement 
    where (quantite_entree = 0 and sortie is not null)
    or (quantite_sortie = 0 )
);

create or replace view v_entree as (
    select
    * 
    from v_mouvement 
    where quantite_sortie = 0
);
create or replace view v_sortie as(
    select 
    * 
    from v_mouvement
    where (quantite_entree = 0 and sortie is not null)
);
create or replace view v_entree_reste as(
    select 
    v_entree.*,  v_entree.quantite_entree - sum(v_sortie.quantite_sortie) as reste
    from v_entree
    join v_sortie
        on v_sortie.entree = v_entree.idmouvement
    group by v_entree.idmouvement, v_entree.idArticle, v_entree.dateMouvement, v_entree.quantite_entree,v_entree.quantite_sortie,v_entree.entree,v_entree.sortie,v_entree.idmagasin, v_entree.prixUnitaire
);

create or replace view v_article as (
    select 
    article.*, unite.nomUnite, unite.abreviation
    from article
    natural join unite
);