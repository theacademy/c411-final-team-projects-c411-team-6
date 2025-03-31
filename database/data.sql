use flowtrack;

INSERT INTO users (username, password, created_at, updated_at)
VALUES
    ('user1', 'password123', NOW(), NOW()),
    ('user2', 'securepass456', NOW(), NOW());