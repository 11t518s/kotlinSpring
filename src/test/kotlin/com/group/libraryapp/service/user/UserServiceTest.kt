package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장을 할 수 있다.")
    fun saveUserTest() {
        // given
        val request = UserCreateRequest("최봉수", null)

        // when
        userService.saveUser(request)

        // then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("최봉수")
        assertThat(results[0].age).isNull()
    }

    @Test
    @DisplayName("유저 조회를 할 수 있다.")
    fun getUserTest() {
        // given
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id, "B")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제를 할 수 있다.")
    fun deleteUserTest() {
        // given
        userRepository.save(User("A", null))

        // when
        userService.deleteUser("A")

        // then
        val results = userRepository.findAll()
        assertThat(results).hasSize(0)
    }


    @Test
    @DisplayName("유저 이름은 변경할 수 있다.")
    fun getUpdateUserNameTest() {
        // given
        val newUser = userRepository.save(User("A", 20))
        val request = UserUpdateRequest(newUser.id, "B")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("B")
    }
}