create table IF NOT EXISTS users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(225) NOT NULL UNIQUE,
    email VARCHAR(225) NOT NULL UNIQUE,
    password VARCHAR(225) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS auction_items(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(225) NOT NULL,
    description TEXT,
    start_price DECIMAL(19,2) NOT NULL,
    current_bid DECIMAL(19,2) NOT NULL,
    high_bidder_user_id BIGINT,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (high_bidder_user_id) REFERENCES users(id)
    ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS bids(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auction_item_id BIGINT NOT NULL,
    bidder_user_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    bid_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_successful BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (auction_item_id) REFERENCES auction_items(id)
    ON DELETE CASCADE
    FOREIGN KEY (bidder_user_id) REFERENCES users(id)
    ON DELETE RESTRICT

);

CREATE INDEX idx_bids_auction_item_id ON bids(auction_item_id);

