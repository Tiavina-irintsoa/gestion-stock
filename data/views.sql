create or replace view v_mouvement as (
     select
    mouvement.*,validation.dateValidation
    from mouvement 
    left join validation
        on validation.idmouvement = mouvement.idmouvement
);
create or replace view v_mouvement_valide as (
    select
    *
    from v_mouvement
    where dateValidation is not null
);

create or replace view v_entree as (
    select
    * 
    from v_mouvement_valide
    where quantite_sortie = 0
);
create or replace view v_sortie as(
    select 
    * 
    from v_mouvement_valide
    where quantite_entree = 0 
);
create or replace view v_article as (
    select 
    article.*, unite.nomUnite, unite.abreviation
    from article
    natural join unite
);