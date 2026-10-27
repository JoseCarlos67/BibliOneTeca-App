ALTER TABLE authors
    ADD COLUMN gender VARCHAR(20),
    ADD CONSTRAINT ck_authors_gender CHECK (
        gender IS NULL OR gender IN (
            'FEMALE',
            'MALE',
            'NON_BINARY',
            'OTHER',
            'COLLECTIVE',
            'UNKNOWN',
            'NOT_APPLICABLE'
        )
    );