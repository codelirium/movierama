# movierama

A movie recommendation system in the form of an API. It contains the implementation of a User/User collaborative filtering algorithm to recommend movies.



Instructions
-

The `docker-compose` command will automatically bring up a postgres server, load it with data and finally bring up the movierama service.


* **Compile:** `$ mvn clean package`

* **Run:** `$ docker-compose -f docker/docker-compose.yml up`

* **Swagger URL:** `http://localhost:8080/swagger-ui.html`



Endpoints
-

* A user can rate a movie he viewed.
  * **POST:** `http://localhost:8080/users/1/movies/1/ratings/5`

* A user can retrieve information about the movies he viewed. The response is paginated.
  * **GET:** `http://localhost:8080/users/1/movies/pages/1`

* A user can retrieve a number of movie recommendations.
  * **GET** `http://localhost:8080/users/1/movies/recommendations/5`

**NOTE:** The movie descriptions are consumed from the external API: `https://api.themoviedb.org`.

Data
-

The `movielens` dataset has been used to populate the user profiles.

* **Location:** `$ wget http://files.grouplens.org/datasets/movielens/ml-latest-small.zip`

The `ratings.csv` and `movies.csv` have been preprocessed with the scripts located at: `sql/data/scripts`. These are:

* `preprocess-movie-data.sh`

* `preprocess-rating-data.sh`

The SQL statements which are produced by these two scripts and that populate the DB are located at `sql/schema/DATA-[MOVIES|RATINGS].sql`.

The DB schema is located at `sql/schema/TABLES.sql`
