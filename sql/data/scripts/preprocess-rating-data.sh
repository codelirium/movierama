#!/usr/bin/env bash


tail -n +2 ratings.csv > ratings-data.csv

while IFS='' read -r line || [[ -n "${line}" ]];
do

	line=$(echo ${line} | tr -d '\n')

	userId="$(echo ${line} | awk -F, '{print $1}')"
	movieId="$(echo ${line} | awk -F, '{print $2}')"
	rating="$(echo ${line} | awk -F, '{print $3}')"
	rating="$(echo ${rating} | cut -d'.' -f1)"

	echo "INSERT INTO RATINGS (USER_ID, MOVIE_ID, RATING) VALUES (${userId}, ${movieId}, ${rating});"

done < ratings-data.csv
