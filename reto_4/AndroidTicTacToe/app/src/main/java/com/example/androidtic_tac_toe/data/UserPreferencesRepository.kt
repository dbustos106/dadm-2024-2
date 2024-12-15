package com.example.androidtic_tac_toe.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.androidtic_tac_toe.model.DifficultyLevel
import com.example.androidtic_tac_toe.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Class that handles saving and retrieving user preferences
 */
@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val DIFFICULTY_LEVEL = stringPreferencesKey("difficulty_level_")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled_")
        val NUMBER_LOSSES = intPreferencesKey("number_losses_")
        val NUMBER_WINS = intPreferencesKey("number_wins_")
        val NUMBER_TIES = intPreferencesKey("number_ties_")
    }

    /**
     * Get the user preferences flow.
     */
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    /**
     * Maps the stored preferences from DataStore to a UserPreferences object.
     */
    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val difficultyLevel = DifficultyLevel.valueOf(
            preferences[PreferencesKeys.DIFFICULTY_LEVEL] ?: DifficultyLevel.EXPERT.name
        )

        val soundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: true
        val numberLosses = preferences[PreferencesKeys.NUMBER_LOSSES] ?: 0
        val numberWins = preferences[PreferencesKeys.NUMBER_WINS] ?: 0
        val numberTies = preferences[PreferencesKeys.NUMBER_TIES] ?: 0

        Log.d("", "$numberTies")

        return UserPreferences(
            difficultyLevel = difficultyLevel,
            soundEnabled = soundEnabled,
            numberLosses = numberLosses,
            numberWins = numberWins,
            numberTies = numberTies
        )
    }

    /**
     * Updates the stored difficulty level in the DataStore.
     * @param difficultyLevel The new difficulty level to be saved.
     */
    suspend fun updateDifficultyLevel(difficultyLevel: DifficultyLevel){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DIFFICULTY_LEVEL] = difficultyLevel.name
        }
    }

    /**
     * Updates the stored sound-enabled preference in the DataStore.
     * @param soundEnabled True if sound should be enabled, false otherwise.
     */
    suspend fun updateSoundEnabled(soundEnabled: Boolean){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = soundEnabled
        }
    }

    /**
     * Updates the stored number of losses in the DataStore.
     * @param numberLosses The new count of losses to be saved.
     */
    suspend fun updateNumberLosses(numberLosses: Int){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMBER_LOSSES] = numberLosses
        }
    }

    /**
     * Updates the stored number of wins in the DataStore.
     * @param numberWins The new count of wins to be saved.
     */
    suspend fun updateNumberWins(numberWins: Int){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMBER_WINS] = numberWins
        }
    }

    /**
     * Updates the stored number of ties in the DataStore.
     * @param numberTies The new count of ties to be saved.
     */
    suspend fun updateNumberTies(numberTies: Int){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMBER_TIES] = numberTies
        }
    }

}
