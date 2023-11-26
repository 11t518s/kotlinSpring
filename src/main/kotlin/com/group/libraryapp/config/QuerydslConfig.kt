package com.group.libraryapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.querydsl.jpa.impl.JPAQueryFactory
import javax.persistence.EntityManager

@Configuration
class QuerydslConfig constructor(
    private val em: EntityManager,
) {

    @Bean
    fun  querydsl(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}