#!/usr/bin/env bash


tail -n +2 movies.csv > movies-data.csv

while IFS='' read -r line || [[ -n "${line}" ]];
do

	line=$(echo ${line} | tr -d '\n')

	commaCount=$(echo ${line} | tr -cd , | wc -c)

	indexOne=2
	indexTwo=$(( ${commaCount} + 1))

	title="$(cut -d',' -f${indexOne} <<< ${line})"

	if [[ $title == \"* ]];
	then

		title="$(echo ${line} | grep -o '".*"' | sed 's/"//g')"

	fi 

	if [[ $title == *\'* ]];
	then

		title="$(echo ${title} | sed s/\'/"\\\'"/g)";

	fi

	genres="$(cut -d',' -f${indexTwo} <<< ${line})"

	echo "INSERT INTO MOVIES (TITLE, GENRES) VALUES ('${title}', '${genres}');"

done < movies-data.csv
