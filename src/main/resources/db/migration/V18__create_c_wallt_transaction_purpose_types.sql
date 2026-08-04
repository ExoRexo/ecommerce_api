CREATE TABLE c_wallt_transaction_purpose_types (
    id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    description TEXT NOT NULL
);
