-- Drops deprecated columns from pet table
ALTER TABLE pet DROP COLUMN IF EXISTS dewormed, DROP COLUMN IF EXISTS vaccinated;