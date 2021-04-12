package com.example.graphqlspring.model

import graphql.schema.DataFetchingEnvironment
import java.util.*

enum class SortBy {
  PRICE,
  ALPHABETICALLY
}

enum class SortOrder {
  ASC,
  DESC
}

open class Product(val id: String, var name: String, var price: Double, var discount: Double?) {
  fun getCategoryName(env: DataFetchingEnvironment): String {
    return env.getLocalContext() as String?
      ?: categories.first { category -> category.products.any { it.id === id } }.name
  }
}

class Book(
  id: String,
  name: String,
  price: Double,
  discount: Double?,
  var author: String,
  var releaseYear: Int,
  var isbn: String
  ): Product(id, name, price, discount)

class Movie(
  id: String,
  name: String,
  price: Double,
  discount: Double?,
  var director: String,
  var releaseYear: Int,
): Product(id, name, price, discount)

data class Category(val id: String, val name: String, var products: List<Product>)

val movies = mutableListOf(
  Movie(UUID.randomUUID().toString(), "Black Panther", 19.99, 0.80, "Ryan Coogler", 2018),
  Movie(UUID.randomUUID().toString(), "Citizen Kane", 7.99, null, "Orson Welles", 1941),
  Movie(UUID.randomUUID().toString(), "Parasite", 15.99, null, "Bong Joon-ho", 2019),
  Movie(UUID.randomUUID().toString(), "Avengers: Endgame", 12.99, null, " Anthony Russo, Joe Russo", 2019),
  Movie(UUID.randomUUID().toString(), "Casablanca", 5.00, null, "Michael Curtiz", 1942),
  Movie(UUID.randomUUID().toString(), "Knives Out", 13.49, null, "Rian Johnson ", 2019),
  Movie(UUID.randomUUID().toString(), "Us", 11.95, null, "Jordan Peele", 2019),
  Movie(UUID.randomUUID().toString(), "Toy Story 4", 17.98, 0.85, "Josh Cooley", 2019),
  Movie(UUID.randomUUID().toString(), "Lady Bird", 9.99, 0.50, "Greta Gerwig", 2017),
  Movie(UUID.randomUUID().toString(), "Mission: Impossible - Fallout", 16.79, null, "Christopher McQuarrie", 2018),
)

val books = listOf(
  Book(UUID.randomUUID().toString(), "THE FOUR WINDS", 12.49, null, "Kristin Hannah", 2021, """${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "LIFE AFTER DEATH", 27.00, 0.90, "Sister Soulja", 2021,"""${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "LATER", 14.95, 0.80, "Stephen King", 2021,"""${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "THE ROSE CODE", 17.99, null, "Kate Quinn", 2021,"""${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "2034", 15.32, null, "Elliot Ackerman and Adm. James Stavridis", 2021,"""${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "THE MIDNIGHT LIBRARY", 26.00, null, "Matt Haig", 2021,"""${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "DARK SKY", 25.76, null, "C.J. Box", 2021,"""${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "KLARA AND THE SUN", 12.99, null, "Kazuo Ishiguro", 2021,"""${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "THE DUKE AND I", 12.99, null, "Julia Quinn", 2021,"""${(1000000..9999999).shuffled().first()}"""),
  Book(UUID.randomUUID().toString(), "WE BEGIN AT THE END", 20.99, null, " Chris Whitaker", 2021,"""${(1000000..9999999).shuffled().first()}""")
)

val categories = listOf(
  Category(UUID.randomUUID().toString(), "Movies", movies),
  Category(UUID.randomUUID().toString(), "Books", books)
)