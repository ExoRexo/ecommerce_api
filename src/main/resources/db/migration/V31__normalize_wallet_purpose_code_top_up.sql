-- Normalizes wallet purpose code to enum-name based format.
-- Needed for environments where historical migrations inserted TOP-UP.

UPDATE c_wallt_transaction_purpose_types
SET code = 'TOP_UP'
WHERE code = 'TOP-UP';
