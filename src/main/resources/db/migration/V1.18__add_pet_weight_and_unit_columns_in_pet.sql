ALTER TABLE pet
    ADD COLUMN weight DECIMAL(6,2) NOT NULL DEFAULT 0.00 AFTER uuid,
    ADD COLUMN unit ENUM('KG','LBS') NOT NULL DEFAULT 'KG' AFTER weight,
    ADD CONSTRAINT chk_weight_positive CHECK (weight >= 0.00);
