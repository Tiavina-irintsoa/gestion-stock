CREATE OR REPLACE FUNCTION getLastMouvement(article text, magasin integer)
RETURNS TABLE (
    idmouvement int,
    idarticle varchar,
    dateMouvement date,
    quantite_entree numeric,
    quantite_sortie numeric,
    entree integer,
    idmagasin integer,
    prixUnitaire numeric,
    etat integer,
    dateValidation date
)
AS $$
BEGIN 
    RETURN QUERY
        EXECUTE 'SELECT * FROM v_sortie WHERE idarticle = $1 AND idmagasin = $2 AND datevalidation = (SELECT MAX(datevalidation) FROM v_sortie WHERE idarticle = $1 AND idmagasin = $2)'
        USING article, magasin;
END;
$$ LANGUAGE plpgsql;


create or replace function getEtatStockMouvement(datebefore date,magasin integer,article text,orderby text) 
RETURNS TABLE (
    idmouvement int, idarticle varchar,dateMouvement date,quantite_entree numeric,quantite_sortie numeric,entree integer,idmagasin integer,prixUnitaire numeric,etat integer, dateValidation date,utilise numeric
)
AS
    $$ BEGIN
        RETURN QUERY execute'
           select v_mouvement_valide.*, coalesce(sum(v_sortie.quantite_sortie),0) as utilise from v_mouvement_valide left join v_sortie
        on v_sortie.entree = v_mouvement_valide.idmouvement
        and v_sortie.datevalidation <'''||datebefore||''' where v_mouvement_valide.idmagasin = '||magasin||' and v_mouvement_valide.idarticle = '''||article||''' and v_mouvement_valide.datevalidation < '''||datebefore||''' group by v_mouvement_valide.idmouvement,v_mouvement_valide.quantite_entree,v_mouvement_valide.quantite_sortie,v_mouvement_valide.idmagasin,v_mouvement_valide.entree,v_mouvement_valide.prixUnitaire,v_mouvement_valide.idarticle,v_mouvement_valide.datemouvement,v_mouvement_valide.etat,v_mouvement_valide.dateValidation order by v_mouvement_valide.datemouvement '||orderby||'';
    END;
    $$ LANGUAGE plpgsql;
select * from getEtatStockMouvement('2022-10-11',3,'R101','asc');
