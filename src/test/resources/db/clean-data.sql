DELETE FROM cart_items;
DELETE FROM shopping_carts;
DELETE FROM books_categories;
DELETE FROM books;
DELETE FROM categories;

ALTER TABLE cart_items AUTO_INCREMENT = 1;
ALTER TABLE shopping_carts AUTO_INCREMENT = 1;
ALTER TABLE books_categories AUTO_INCREMENT = 1;
ALTER TABLE books AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;