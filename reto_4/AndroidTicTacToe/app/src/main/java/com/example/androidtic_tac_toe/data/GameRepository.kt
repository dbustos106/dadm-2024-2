package com.example.androidtic_tac_toe.data

import com.example.androidtic_tac_toe.model.Game
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val database: DatabaseReference
) {

    // Available games listener
    private var availableGamesListener: ChildEventListener? = null

    // Current Game listener
    private var gameListener: ValueEventListener? = null

    /**
     * Starts observing available games in the Firebase database.
     * @param onInitialData A callback invoked when the initial data is loaded.
     * @param onGameAdded A callback invoked when a new game is added.
     * @param onGameRemoved A callback invoked when a game is removed.
     * @param onError A callback invoked if an error occurs.
     */
    fun observeAvailableGames(
        onInitialData: (List<Game>) -> Unit, onGameAdded: (Game) -> Unit,
        onGameRemoved: (String) -> Unit, onError: (Exception) -> Unit
    ) {
        if (availableGamesListener != null) return

        database.child("games")
            .orderByChild("available")
            .equalTo(true)
            .get()
            .addOnSuccessListener { snapshot ->
                val games = mutableListOf<Game>()
                snapshot.children.forEach { child ->
                    val game = child.getValue(Game::class.java)
                    game?.let { games.add(it) }
                }
                onInitialData(games)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }

        availableGamesListener = database.child("games")
            .orderByChild("available")
            .equalTo(true)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val game = snapshot.getValue(Game::class.java)
                    game?.let { onGameAdded(it) }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // It's not relevant in this case
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val gameId = snapshot.key
                    gameId?.let { onGameRemoved(it) }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // It's not relevant in this case
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.toException())
                }
            })
    }

    /**
     * Stops observing available games.
     * @return A Result indicating success or failure.
     */
    fun stopObservingAvailableGames(): Result<Unit> {
        return try {
            if (availableGamesListener == null) return Result.success(Unit)
            database.child("games").removeEventListener(availableGamesListener!!)
            availableGamesListener = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Creates a new game in the Firebase database.
     * @param game The game object to create.
     * @return A Result indicating success or failure.
     */
    suspend fun createNewGame(game: Game): Result<Unit> {
        return try {
            database.child("games")
                .child(game.gameId!!)
                .setValue(game)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Selects a game by updating its availability status to false.
     * @param gameId The ID of the game to select.
     * @return A Result containing the updated Game object or an error.
     */
    suspend fun selectGame(gameId: String): Result<Game> {
        return try {
            val gameRef = database.child("games").child(gameId)
            gameRef.child("available").setValue(false).await()

            val updatedGameSnapshot = gameRef.get().await()
            val updatedGame = updatedGameSnapshot.getValue(Game::class.java)

            updatedGame?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Game not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates the current game with a new move.
     * @param updatedGame The updated game object with the new move.
     * @param onFailure A callback invoked if an error occurs.
     */
    fun updateMove(updatedGame: Game, onFailure: (Exception) -> Unit) {
        database.child("games").child(updatedGame.gameId!!)
            .setValue(updatedGame)
            .addOnSuccessListener { }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    /**
     * Starts observing a specific game for updates.
     * @param gameId The ID of the game to observe.
     * @param onGameUpdate A callback invoked when the game data is updated.
     * @param onFailure A callback invoked if an error occurs.
     */
    fun observeGame(gameId: String, onGameUpdate: (Game) -> Unit, onFailure: (Exception) -> Unit) {
        if (gameListener != null) return

        gameListener = database.child("games").child(gameId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.getValue(Game::class.java)
                    game?.let { onGameUpdate(it) }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.toException())
                }
            })
    }

    /**
     * Removes the listener for a specific game.
     * @param gameId The ID of the game to remove the listener for.
     * @return A Result indicating success or failure.
     */
    fun removeGameListener(gameId: String): Result<Unit> {
        return try {
            if (gameListener == null) return Result.success(Unit)
            database.child("games").child(gameId).removeEventListener(gameListener!!)
            gameListener = null
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    /**
     * Deletes a game from the Firebase database.
     * @param gameId The ID of the game to delete.
     * @param onSuccess A callback invoked when the game is successfully deleted.
     * @param onFailure A callback invoked if an error occurs.
     */
    fun deleteGame(gameId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        database.child("games").child(gameId).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}
