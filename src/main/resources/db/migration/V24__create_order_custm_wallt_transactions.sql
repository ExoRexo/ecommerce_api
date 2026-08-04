CREATE TABLE order_custm_wallt_transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES customer_orders(id),
    custm_wallet_tran_id BIGINT NOT NULL REFERENCES customer_wallet_transactions(id)
);

CREATE INDEX idx_order_wallet_tx_order_id ON order_custm_wallt_transactions(order_id);
CREATE INDEX idx_order_wallet_tx_tran_id ON order_custm_wallt_transactions(custm_wallet_tran_id);
