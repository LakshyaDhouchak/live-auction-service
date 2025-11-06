
-- ========================
-- USERS TABLE
-- ========================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER'
);

-- ========================
-- AUCTION ITEMS TABLE
-- ========================
CREATE TABLE IF NOT EXISTS auction_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_price DECIMAL(19,2) NOT NULL,
    current_bid DECIMAL(19,2) DEFAULT 0.00,
    high_bidder_user_id BIGINT,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_high_bidder FOREIGN KEY (high_bidder_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);

-- ========================
-- BIDS TABLE
-- ========================
CREATE TABLE IF NOT EXISTS bids (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auction_item_id BIGINT NOT NULL,
    bidder_user_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL CHECK (amount >= 0),
    bid_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_successful BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_bid_item FOREIGN KEY (auction_item_id)
        REFERENCES auction_items(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_bid_user FOREIGN KEY (bidder_user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT
);

-- ========================
-- INDEXES
-- ========================
CREATE INDEX idx_bids_auction_item_id ON bids(auction_item_id);
CREATE INDEX idx_bids_bidder_user_id ON bids(bidder_user_id);
CREATE INDEX idx_auction_status ON auction_items(status);
