delete from validation;
delete from mouvement;
delete from magasin;
delete from article;
delete from unite;

alter sequence  magasin_idmagasin_seq restart with 1;
alter sequence  mouvement_idmouvement_seq  restart with 1;
alter sequence  unite_idunite_seq  restart with 1;