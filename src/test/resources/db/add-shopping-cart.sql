DELETE FROM shopping_carts;
DELETE FROM categories;

INSERT INTO shopping_carts (user_id, is_deleted)
VALUES
    (1, '0');

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES
    (1, 1, 1, 2);