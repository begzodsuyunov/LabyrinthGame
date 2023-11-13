package com.example.labyrinthgame.domain.repository

import com.example.labyrinthgame.data.model.AppLevel
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun getMatrix(level: AppLevel): Flow<List<List<Int>>>

}