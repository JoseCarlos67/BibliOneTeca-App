-- 1. Adicionar atributos de Book
ALTER TABLE books
    ADD COLUMN subtitle VARCHAR(255),
    ADD COLUMN synopsis TEXT;

-- 2. Resolver eventuais dados de synopsis em editions
-- Antes de remover a coluna, migrar os dados existentes,
-- quando necessário.
ALTER TABLE editions
DROP COLUMN synopsis;


-- 3. Atualizar os estados dos exemplares
ALTER TABLE physical_copies
DROP CONSTRAINT ck_physical_copies_status;

UPDATE physical_copies
SET inventory_status = 'ACTIVE'
WHERE inventory_status = 'AVAILABLE';

ALTER TABLE physical_copies
    ALTER COLUMN inventory_status SET DEFAULT 'ACTIVE';

ALTER TABLE physical_copies
    ADD CONSTRAINT ck_physical_copies_status
        CHECK (
            inventory_status IN (
                                 'ACTIVE',
                                 'LOST',
                                 'DAMAGED',
                                 'MAINTENANCE',
                                 'WITHDRAWN'
                )
            );

ALTER TABLE authors
DROP CONSTRAINT ck_authors_gender;

ALTER TABLE authors
    ADD CONSTRAINT ck_authors_gender
        CHECK (
            gender IS NULL OR gender IN (
                                         'FEMALE',
                                         'MALE',
                                         'NON_BINARY',
                                         'OTHER',
                                         'UNKNOWN'
                )
            );