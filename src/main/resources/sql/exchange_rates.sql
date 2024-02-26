create table ExchangeRates
(
    id               INTEGER
        primary key autoincrement,
    BaseCurrencyId   INTEGER
        references Currencies
        references Currencies,
    TargetCurrencyId INTEGER
        references Currencies
        references Currencies,
    Rate             DECIMAL(6),
    unique (BaseCurrencyId, TargetCurrencyId)
);