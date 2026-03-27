ALTER TABLE pet
    ADD COLUMN going_out_often bit(1) NOT NULL DEFAULT 0 AFTER sterilized;
