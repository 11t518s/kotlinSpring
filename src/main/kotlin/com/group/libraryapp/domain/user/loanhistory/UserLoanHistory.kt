package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.domain.user.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class UserLoanHistory(

    @ManyToOne
    val user: User,

    val bookName: String,
    var isReturn: Boolean,

    @Id
    @GeneratedValue
    val id: Int? = null,
) {

    fun doReturn() {
        this.isReturn = true
    }
}