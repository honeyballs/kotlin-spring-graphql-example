package com.example.graphqlspring.config

import com.example.graphqlspring.resolvers.Resolvers
import com.google.common.io.Resources
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GraphQLProvider(val resolvers: Resolvers) {

  private lateinit var graphQL: GraphQL

  @Bean
  fun graphQL() = graphQL

  @PostConstruct
  fun init() {
    val url = Resources.getResource("schema.graphqls")
    val schema = buildSchema(Resources.toString(url, Charsets.UTF_8))
    this.graphQL = GraphQL.newGraphQL(schema).build()
  }

  private fun buildSchema(sdl: String): GraphQLSchema {
    val typeRegistry = SchemaParser().parse(sdl)
    val runtimeWiring = buildWiring()
    return SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring)
  }

  private fun buildWiring() = newRuntimeWiring()
    .type("Query") {
      it.dataFetcher("categories", resolvers.categoriesDataFetcher())
      it.dataFetcher("categoryByName", resolvers.categoryByNameDataFetcher())
      it.dataFetcher("productById", resolvers.productByIdDataFetcher())
    }
    .type("Mutation") { it.dataFetcher("addMovies", resolvers.addMovieMutationDataFetcher()) }
    .type("Category") { it.dataFetcher("products", resolvers.categoryProductsDataFetcher()) }
    .type("Product") { it.typeResolver(resolvers.productTypeResolver()) }
    .type("Movie") { it.dataFetcher("price", resolvers.priceDataFetcher()) }
    .type("Book") { it.dataFetcher("price", resolvers.priceDataFetcher()) }
    .build()
}