package com.example.graphqlspring.resolvers

import com.example.graphqlspring.model.*
import graphql.execution.DataFetcherResult
import graphql.schema.DataFetcher
import graphql.schema.TypeResolver
import org.springframework.stereotype.Component
import java.util.*

/**
 * Provides the mapping between the schema and the data.
 * Each DataFetcher receives a query environment object to interact with the query parameters,
 * its parent or retrieve further information.
 */
@Component
class Resolvers {

  // Queries

  fun categoriesDataFetcher(): DataFetcher<List<Category>> = DataFetcher { categories }

  fun productByIdDataFetcher(): DataFetcher<Product> = DataFetcher { env ->
    listOf<Product>().plus(books).plus(movies).firstOrNull { it.id == env.getArgument("id") }
  }

  fun categoryByNameDataFetcher(): DataFetcher<Category> = DataFetcher { env ->
    categories.firstOrNull { it.name == env.getArgument("name") }
  }

  fun categoryProductsDataFetcher(): DataFetcher<DataFetcherResult<List<Product>>> = DataFetcher { env ->
    val category = env.getSource<Category>()
    val sortBy = SortBy.valueOf(env.getArgument("sortBy"))
    val sortOrder = SortOrder.valueOf(env.getArgument("sortOrder"))
    var products = categories.first { it.id == category.id }.products
    products = when (sortBy) {
      SortBy.PRICE -> if (sortOrder == SortOrder.ASC) products.sortedBy { it.price } else products.sortedByDescending { it.price }
      SortBy.ALPHABETICALLY -> if (sortOrder == SortOrder.ASC) products.sortedBy { it.name } else products.sortedByDescending { it.name }
    }
    DataFetcherResult.newResult<List<Product>>().data(products).localContext(category.name).build()
  }

  fun priceDataFetcher(): DataFetcher<Double> = DataFetcher { env ->
    val product = env.getSource<Product>()
    if (product.discount != null) product.price * product.discount!! else product.price
  }

  // Type resolver

  fun productTypeResolver(): TypeResolver = TypeResolver { env ->
    val product: Product = env.getObject()
    if (product is Movie) {
      return@TypeResolver env.schema.getObjectType("Movie")
    } else {
      return@TypeResolver env.schema.getObjectType("Book")
    }
  }

  // Mutations

  fun addMovieMutationDataFetcher(): DataFetcher<List<String>> = DataFetcher { env ->
    val input = env.getArgument<List<Map<String, Any>>>("movies")
    input.map {
      val movie = Movie(
        UUID.randomUUID().toString(),
        it["name"] as String,
        it["price"] as Double,
        it["discount"] as Double?,
        it["director"] as String,
        it["releaseYear"] as Int
      )
      movies.add(movie)
      movie.id
    }
  }



}