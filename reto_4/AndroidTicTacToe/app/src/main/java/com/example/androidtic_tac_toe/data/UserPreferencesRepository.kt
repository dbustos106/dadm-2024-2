package com.example.androidtic_tac_toe.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.androidtic_tac_toe.data.model.DifficultyLevel
import com.example.androidtic_tac_toe.data.model.UserPreferences
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
        val DIFFICULTY_LEVEL = stringPreferencesKey("difficulty_level")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val NUMBER_COMPUTER_WINS = intPreferencesKey("number_computer_wins")
        val NUMBER_HUMAN_WINS = intPreferencesKey("number_human_wins")
        val NUMBER_TIES = intPreferencesKey("number_ties")
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
        val numberComputerWins = preferences[PreferencesKeys.NUMBER_COMPUTER_WINS] ?: 0
        val numberHumanWins = preferences[PreferencesKeys.NUMBER_HUMAN_WINS] ?: 0
        val numberTies = preferences[PreferencesKeys.NUMBER_TIES] ?: 0

        return UserPreferences(
            difficultyLevel = difficultyLevel,
            soundEnabled = soundEnabled,
            numberComputerWins = numberComputerWins,
            numberHumanWins = numberHumanWins,
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
     * Updates the stored number of computer wins in the DataStore.
     * @param numberComputerWins The new count of computer wins to be saved.
     */
    suspend fun updateNumberComputerWins(numberComputerWins: Int){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMBER_COMPUTER_WINS] = numberComputerWins
        }
    }

    /**
     * Updates the stored number of human wins in the DataStore.
     * @param numberHumanWins The new count of human wins to be saved.
     */
    suspend fun updateNumberHumanWins(numberHumanWins: Int){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMBER_HUMAN_WINS] = numberHumanWins
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
