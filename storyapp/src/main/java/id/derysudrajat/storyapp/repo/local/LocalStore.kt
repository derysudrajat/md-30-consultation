package id.derysudrajat.storyapp.repo.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import id.derysudrajat.storyapp.data.model.LoginResult
import id.derysudrajat.storyapp.di.RepoModule.userStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class LocalStore constructor(private val context: Context) {

    private val TOKEN_KEY = stringPreferencesKey("token_key")
    private val USER_ID_KEY = stringPreferencesKey("user_id_key")
    private val NAME_KEY = stringPreferencesKey("name_key")

    suspend fun getUserLoginResult() = flow {
        val token = context.userStore.data.first()[TOKEN_KEY] ?: ""
        val userId = context.userStore.data.first()[USER_ID_KEY] ?: ""
        val name = context.userStore.data.first()[NAME_KEY] ?: ""
        emit(LoginResult(name, userId, token))
    }

    suspend fun putLoginResult(loginResult: LoginResult) {
        context.userStore.edit { it[TOKEN_KEY] = loginResult.token }
        context.userStore.edit { it[USER_ID_KEY] = loginResult.userId }
        context.userStore.edit { it[NAME_KEY] = loginResult.name }
    }
}