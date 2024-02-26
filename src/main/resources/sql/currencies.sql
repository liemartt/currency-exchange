create table Currencies
(
    id       INTEGER
        primary key autoincrement,
    Code     VARCHAR(3)
        unique,
    FullName VARCHAR(40),
    Sign     VARCHAR(1)
);