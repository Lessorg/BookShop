DELETE FROM books_categories;
DELETE FROM books;
DELETE FROM categories;

ALTER TABLE books_categories AUTO_INCREMENT = 1;
ALTER TABLE books AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;

INSERT INTO categories (name, description, is_deleted)
VALUES
    ('Fiction', 'Books that contain fictional stories and characters.', '0'),
    ('Science', 'Books that explore scientific concepts and discoveries.', '0'),
    ('History', 'Books that delve into historical events and figures.', '0');

INSERT INTO books (title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    ('Pride and Prejudice', 'Jane Austen', '978-3-16-148410-0', 9.99, 'A classic novel about love and social standing.', 'http://example.com/pride_and_prejudice.jpg', '0'),
    ('1984', 'George Orwell', '978-0-452-28423-4', 14.99, 'A dystopian novel set in a totalitarian society.', 'http://example.com/1984.jpg', '0'),
    ('To Kill a Mockingbird', 'Harper Lee', '978-0-06-112008-4', 12.99, 'A novel about racial injustice in the Deep South.', 'http://example.com/to_kill_a_mockingbird.jpg', '0'),
    ('The Great Gatsby', 'F. Scott Fitzgerald', '978-0-7432-7356-5', 10.99, 'A novel about the American dream and the Roaring Twenties.', 'http://example.com/the_great_gatsby.jpg', '0'),
    ('Moby Dick', 'Herman Melville', '978-0-14-243724-7', 11.99, 'A novel about the obsessive quest of Ahab for revenge on Moby Dick.', 'http://example.com/moby_dick.jpg', '0');

INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1), -- 'Pride and Prejudice' belongs to 'Fiction'
    (2, 2), -- '1984' belongs to 'Science'
    (3, 3), -- 'To Kill a Mockingbird' belongs to 'History'
    (4, 1), -- 'The Great Gatsby' belongs to 'Fiction'
    (5, 1); -- 'Moby Dick' belongs to 'Fiction'