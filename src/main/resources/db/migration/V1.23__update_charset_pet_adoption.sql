-- Flyway migration: change charset of pet_adoption table from latin1 to utf8mb4
-- This allows storing emoji and full Unicode characters in the description column
-- References: https://github.com/josdem/vetlog-spring-boot/issues/892

ALTER TABLE pet_adoption
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
