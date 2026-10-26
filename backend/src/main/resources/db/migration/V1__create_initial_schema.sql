-- BibliOneTeca - PostgreSQL initial schema
-- This migration is a clean-start baseline. Do not edit a migration after it has
-- been applied to a shared database; create a new V2, V3, ... migration instead.

CREATE TABLE persons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cpf VARCHAR(11) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    date_of_birth DATE NOT NULL,
    email VARCHAR(254) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_persons_cpf_digits CHECK (cpf ~ '^[0-9]{11}$'),
    CONSTRAINT uq_persons_cpf UNIQUE (cpf)
);

-- Treat email uniqueness as case-insensitive (e.g. User@example.com = user@example.com).
CREATE UNIQUE INDEX ux_persons_email_lower ON persons (LOWER(email));

CREATE TABLE librarians (
    person_id UUID PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    CONSTRAINT fk_librarians_person
        FOREIGN KEY (person_id) REFERENCES persons (id) ON DELETE CASCADE
);

CREATE TABLE clients (
    person_id UUID PRIMARY KEY,
    cell_number VARCHAR(20) NOT NULL,
    registered_at DATE NOT NULL DEFAULT CURRENT_DATE,
    registration_expiration DATE NOT NULL,
    CONSTRAINT fk_clients_person
        FOREIGN KEY (person_id) REFERENCES persons (id) ON DELETE CASCADE,
    CONSTRAINT ck_clients_registration_period
        CHECK (registration_expiration >= registered_at)
);

CREATE TABLE authors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    date_of_death DATE,
    nationality VARCHAR(100),
    CONSTRAINT ck_authors_dates CHECK (
        date_of_birth IS NULL OR date_of_death IS NULL OR date_of_death >= date_of_birth
    )
);

CREATE TABLE books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE genres (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX ux_genres_name_lower ON genres (LOWER(name));

CREATE TABLE publishers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    legal_name VARCHAR(255) NOT NULL,
    trade_name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(14) NOT NULL,
    site_url VARCHAR(2048),
    CONSTRAINT ck_publishers_cnpj_digits CHECK (cnpj ~ '^[0-9]{14}$'),
    CONSTRAINT uq_publishers_cnpj UNIQUE (cnpj)
);

CREATE TABLE book_authors (
    book_id UUID NOT NULL,
    author_id UUID NOT NULL,
    CONSTRAINT pk_book_authors PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_book_authors_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    CONSTRAINT fk_book_authors_author FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE CASCADE
);
CREATE INDEX ix_book_authors_author_id ON book_authors (author_id);

CREATE TABLE book_genres (
    book_id UUID NOT NULL,
    genre_id UUID NOT NULL,
    CONSTRAINT pk_book_genres PRIMARY KEY (book_id, genre_id),
    CONSTRAINT fk_book_genres_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    CONSTRAINT fk_book_genres_genre FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE
);
CREATE INDEX ix_book_genres_genre_id ON book_genres (genre_id);

CREATE TABLE editions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    isbn VARCHAR(17) NOT NULL,
    edition_number INTEGER NOT NULL DEFAULT 1,
    year_publication SMALLINT NOT NULL,
    language VARCHAR(50) NOT NULL,
    page_number INTEGER NOT NULL,
    cover_url VARCHAR(2048),
    synopsis TEXT,
    book_id UUID NOT NULL,
    publisher_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_editions_isbn UNIQUE (isbn),
    CONSTRAINT ck_editions_number CHECK (edition_number > 0),
    CONSTRAINT ck_editions_year CHECK (year_publication BETWEEN 1000 AND 2200),
    CONSTRAINT ck_editions_pages CHECK (page_number > 0),
    CONSTRAINT fk_editions_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE RESTRICT,
    CONSTRAINT fk_editions_publisher FOREIGN KEY (publisher_id) REFERENCES publishers (id) ON DELETE RESTRICT
);
CREATE INDEX ix_editions_book_id ON editions (book_id);
CREATE INDEX ix_editions_publisher_id ON editions (publisher_id);

-- A copy's stored status describes inventory/maintenance, not whether it is on loan.
-- Whether a copy is on loan is derived from a loan row whose returned_at is NULL.
CREATE TABLE physical_copies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    edition_id UUID NOT NULL,
    asset_code VARCHAR(50) NOT NULL,
    inventory_status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    acquired_at DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_physical_copies_asset_code UNIQUE (asset_code),
    CONSTRAINT ck_physical_copies_status CHECK (
        inventory_status IN ('AVAILABLE', 'LOST', 'MAINTENANCE')
    ),
    CONSTRAINT fk_physical_copies_edition
        FOREIGN KEY (edition_id) REFERENCES editions (id) ON DELETE RESTRICT
);
CREATE INDEX ix_physical_copies_edition_id ON physical_copies (edition_id);

-- One row represents one copy borrowed by one client. Multiple copies borrowed
-- at the same counter visit are currently represented by multiple loan rows.
CREATE TABLE loans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    date_loan DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date DATE NOT NULL,
    date_return DATE,
    renewal_count SMALLINT NOT NULL DEFAULT 0,
    client_person_id UUID NOT NULL,
    librarian_person_id UUID NOT NULL,
    physical_copy_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_loans_due_date CHECK (due_date >= date_loan),
    CONSTRAINT ck_loans_return_date CHECK (date_return IS NULL OR date_return >= date_loan),
    CONSTRAINT ck_loans_renewal_count CHECK (renewal_count BETWEEN 0 AND 4),
    CONSTRAINT uq_loans_client_id UNIQUE (client_person_id, id),
    CONSTRAINT fk_loans_client FOREIGN KEY (client_person_id) REFERENCES clients (person_id) ON DELETE RESTRICT,
    CONSTRAINT fk_loans_librarian FOREIGN KEY (librarian_person_id) REFERENCES librarians (person_id) ON DELETE RESTRICT,
    CONSTRAINT fk_loans_physical_copy FOREIGN KEY (physical_copy_id) REFERENCES physical_copies (id) ON DELETE RESTRICT
);
CREATE INDEX ix_loans_client_person_id ON loans (client_person_id);
CREATE INDEX ix_loans_librarian_person_id ON loans (librarian_person_id);
CREATE INDEX ix_loans_physical_copy_id ON loans (physical_copy_id);
CREATE INDEX ix_loans_active_client_due_date ON loans (client_person_id, due_date)
    WHERE date_return IS NULL;

-- Database-level protection against two simultaneous active loans for one copy.
CREATE UNIQUE INDEX ux_loans_one_active_loan_per_copy ON loans (physical_copy_id)
    WHERE date_return IS NULL;

-- Audit trail for suspensions. A loan may only trigger the same suspension reason once.
-- RN08's exact operational policy should be confirmed before implementing the service.
CREATE TABLE client_suspensions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_person_id UUID NOT NULL,
    source_loan_id UUID,
    reason VARCHAR(40) NOT NULL,
    starts_at DATE NOT NULL,
    ends_at DATE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_client_suspensions_dates CHECK (ends_at >= starts_at),
    CONSTRAINT ck_client_suspensions_reason CHECK (
        reason IN ('OVERDUE_OVER_14_DAYS', 'MANUAL')
    ),
    CONSTRAINT fk_client_suspensions_client
        FOREIGN KEY (client_person_id) REFERENCES clients (person_id) ON DELETE RESTRICT,
    CONSTRAINT fk_client_suspensions_source_loan
        FOREIGN KEY (client_person_id, source_loan_id) REFERENCES loans (client_person_id, id) ON DELETE RESTRICT,
    CONSTRAINT uq_client_suspensions_source_reason UNIQUE (source_loan_id, reason)
);
CREATE INDEX ix_client_suspensions_client_dates
    ON client_suspensions (client_person_id, starts_at, ends_at);
