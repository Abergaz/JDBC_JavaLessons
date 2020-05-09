create procedure booksCount(OUT cnt int)
BEGIN
    select count(*) into cnt from books;
end;

CREATE PROCEDURE getBooks (bookId INT)
BEGIN
    select * from books where id=bookId;
end;