package com.axonista.popularmovies.util

import com.axonista.popularmovies.data.model.MovieDetail
import com.axonista.popularmovies.data.model.MovieList


/**
 * dummy data to test
 */
object TestUtil {

    fun getNewMovieList():ArrayList<MovieList.Movie?>{
        val searchItem = MovieList.Movie(7.8,567,true,"MV5BYThjYzcyYzItNTVjNy00NDk0LTgwMWQtYjMwNmNlNWJhMzMyXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
            34567,true,5.6,"2019-09-09","factory","sdfsdfdfsd");
        return arrayListOf(searchItem)
    }

    private  val TAG = "TestUtil"


    fun getmovies():MovieList{
        val searchItem = MovieList.Movie(7.8,567,true,"MV5BYThjYzcyYzItNTVjNy00NDk0LTgwMWQtYjMwNmNlNWJhMzMyXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
            34567,true,5.6,"2019-09-09","factory","sdfsdfdfsd")
        val movieList = MovieList(1,1,50,arrayListOf(searchItem))
        return movieList;
    }

    fun getMovieDetail():MovieDetail {
        val movieDetail = MovieDetail("2019-09-09","website",null,70,7.8,null,null,"testing movie")
        return  movieDetail
    }
}