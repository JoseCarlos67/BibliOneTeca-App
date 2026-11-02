-- V4__merge_editions_into_books_and_reset.sql
-- Limpeza dos dados dependentes e reestruturação unificando books e editions

-- 1. Limpeza de dados das tabelas afetadas pelo ciclo de vida do livro/exemplar/empréstimo
TRUNCATE TABLE client_suspensions CASCADE;
TRUNCATE TABLE loans CASCADE;
TRUNCATE TABLE physical_copies CASCADE;
TRUNCATE TABLE book_authors CASCADE;
TRUNCATE TABLE book_genres CASCADE;
TRUNCATE TABLE editions CASCADE;
TRUNCATE TABLE books CASCADE;

-- 2. Remoção da tabela editions e dependências
DROP TABLE IF EXISTS editions CASCADE;

-- 3. Adicionar atributos de edição diretamente na tabela books
ALTER TABLE books
    ADD COLUMN isbn VARCHAR(17) NOT NULL,
ADD COLUMN edition_number INTEGER NOT NULL DEFAULT 1,
ADD COLUMN year_publication SMALLINT NOT NULL,
ADD COLUMN language VARCHAR(50) NOT NULL,
ADD COLUMN page_number INTEGER NOT NULL,
ADD COLUMN cover_url VARCHAR(2048),
ADD COLUMN publisher_id UUID NOT NULL;

-- 4. Constraints da tabela books
ALTER TABLE books
    ADD CONSTRAINT uq_books_isbn UNIQUE (isbn),
ADD CONSTRAINT ck_books_edition_number CHECK (edition_number > 0),
ADD CONSTRAINT ck_books_year CHECK (year_publication BETWEEN 1000 AND 2200),
ADD CONSTRAINT ck_books_page_number CHECK (page_number > 0),
ADD CONSTRAINT fk_books_publisher FOREIGN KEY (publisher_id)
REFERENCES publishers (id) ON DELETE RESTRICT;

CREATE INDEX ix_books_publisher_id ON books (publisher_id);

-- 5. Ajustar physical_copies para apontar diretamente para books
ALTER TABLE physical_copies
DROP COLUMN IF EXISTS edition_id CASCADE;

ALTER TABLE physical_copies
    ADD COLUMN book_id UUID NOT NULL,
ADD CONSTRAINT fk_physical_copies_book FOREIGN KEY (book_id)
REFERENCES books (id) ON DELETE RESTRICT;

CREATE INDEX ix_physical_copies_book_id ON physical_copies (book_id);