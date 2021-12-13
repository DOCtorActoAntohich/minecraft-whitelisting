package com.example.whitelistverification.backend.rest

import com.example.whitelistverification.backend.dao.PendingRepository
import com.example.whitelistverification.backend.dao.UserRepository
import com.example.whitelistverification.backend.dto.UserDto
import com.example.whitelistverification.backend.dto.UserResponse
import com.example.whitelistverification.backend.model.PendingRegistration
import com.example.whitelistverification.backend.model.User
import com.example.whitelistverification.backend.smtp.EmailServiceImpl
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/user")
class UserController(
    private val userRepository: UserRepository,
    private val pendingRepository: PendingRepository,
    private val emailService: EmailServiceImpl
) {

    @GetMapping("/all")
    fun getAllUsers(): Iterable<User> {
        return userRepository.findAll()
    }

    @GetMapping("/id")
    fun getUserById(@RequestParam("id") id: Int): User? {
        return userRepository.findByIdOrNull(id)
    }

    @CrossOrigin
    @PostMapping("/register")
    fun registerUser(@RequestBody newUser: UserDto): UserResponse {
        var newPending = PendingRegistration()
        var user: User = User()
        if (userRepository.checkExistsUserByEmail(newUser.email) || userRepository.checkExistsUserByNickname(newUser.nickname))
        {
            return UserResponse(false,"Email is already used.")
        }
        user.email = newUser.email
        user.nickname = newUser.nickname
        val newEntry = userRepository.save(user)
        newPending.userId = newEntry.id
        val newPin = (1000..9999).random()
        newPending.pin = newPin
        pendingRepository.save(newPending)
        emailService.sendVerificationMessage(
            newEntry.email,
            "Your verification code for Minecraft Server Whitelist",
            "<p>Hello! You requested adding your nickname to the game server whitelist.</p>" + 
            "<p>To confirm your registration, use the following code:</p>" + 
            "<h2>$newPin</h2>"
        )
        return UserResponse(true,"Registration done. Awaiting for confirmation" )
    }

    @CrossOrigin
    @PostMapping("/confirm")
    fun confirmRegister(@RequestBody confirm: UserDto): UserResponse {
        if (userRepository.checkExistsUserByEmail(confirm.email)) {
            val user = userRepository.findUserByEmail(confirm.email)
            val userId = user?.id
            if (userId != null) {
                val pendingEntry = pendingRepository.findPendingByUserId(userId)
                if (confirm.code == pendingEntry.pin.toString()) {
                    try {
                        val nickname = userRepository.findById(pendingEntry.userId).get().nickname
                        val headers = HttpHeaders()
                        headers.setContentType(MediaType.APPLICATION_JSON)
                        val entity = HttpEntity("{\"nickname\": \"$nickname\"}", headers)
                        val restTemplate = RestTemplateBuilder().build()
                        val response = restTemplate.exchange("http://whitelist:8080", HttpMethod.POST, entity, UserResponse::class.java)
                        pendingRepository.deleteById(pendingEntry.id)
                        return if (response.hasBody())
                            response.body!!
                        else
                            UserResponse(false, "No answer from whitelist server")
                    }
                    catch (e: Exception)
                    {
                        return UserResponse(false, "Some exception occured while sending nickname to whitelist server. Reason: ${e.message}")
                    }

                    return UserResponse(true, "Successful confirmation")
                } else
                    return UserResponse(false, "Confirmation failed")
            } else
                return UserResponse(false, "Confirmation failed")
        } else
            return UserResponse(false, "Confirmation failed")
    }

}
