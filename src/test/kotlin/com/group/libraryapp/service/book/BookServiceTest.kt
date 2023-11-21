package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {
    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책을 저장할 수 있다.")
    fun saveBookTest() {
        // given
        val request = BookRequest("A")

        // when
        bookService.saveBook(request)

        // then
        val results = bookRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
    }


    @Test
    @DisplayName("책을 대출할 수 있다.")
    fun loanBookTest() {
        // given
        val book = bookRepository.save(Book("A"))
        val user = userRepository.save(User("userA",  null))
        val request = BookLoanRequest(user.name, book.name)

        // when
        bookService.loanBook(request)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo(book.name)
        assertThat(results[0].user.id).isEqualTo(user.id)
        assertThat(results[0].isReturn).isFalse()
    }

    @Test
    @DisplayName("이미 대출중인 책이 있으면 에러를 발생시킨다.")
    fun loanBookExceptionTest() {
        // given
        val book = bookRepository.save(Book("A"))
        val user = userRepository.save(User("userA", null))

        userLoanHistoryRepository.save(UserLoanHistory(user, book.name, false))

        val request = BookLoanRequest(user.name, book.name)

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책을 반납할 수 있다.")
    fun returnBookTest() {
        // given
        val book = bookRepository.save(Book("A"))
        val user = userRepository.save(User("userA", null))
        userLoanHistoryRepository.save(UserLoanHistory(user, book.name, false))
        val returnBookRequest = BookReturnRequest(user.name, book.name)

        // when
        bookService.returnBook(returnBookRequest)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].isReturn).isTrue()
    }

}