 ALTER TABLE pet ADD COLUMN pet_id bigint DEFAULT NULL;
 ALTER TABLE pet ADD CONSTRAINT pet_id_constraint FOREIGN KEY (`pet_id`) REFERENCES pet_adoption (`id`);
 ALTER TABLE pet_log ADD COLUMN uuid varchar(255) NOT NULL;
 ALTER TABLE registration_code MODIFY status tinyint NOT NULL;
 ALTER TABLE user ADD COLUMN country_code varchar(10) NULL;