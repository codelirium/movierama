CREATE TABLE IF NOT EXISTS MOVIES (
	ID     BIGSERIAL PRIMARY KEY CONSTRAINT no_null NOT NULL,
	TITLE  TEXT                  CONSTRAINT no_null NOT NULL,
	GENRES TEXT                  CONSTRAINT no_null NOT NULL
);


CREATE TABLE IF NOT EXISTS RATINGS (
	ID       BIGSERIAL PRIMARY KEY CONSTRAINT no_null NOT NULL,
	USER_ID  BIGINT                CONSTRAINT no_null NOT NULL,
	MOVIE_ID BIGINT                CONSTRAINT no_null NOT NULL, -- REFERENCES MOVIES (ID),
	RATING   INTEGER               CONSTRAINT no_null NOT NULL
);

CREATE INDEX USER_ID_IDX  ON RATINGS (USER_ID);
CREATE INDEX MOVIE_ID_IDX ON RATINGS (MOVIE_ID);
