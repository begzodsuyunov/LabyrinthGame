package com.example.labyrinthgame.ui.screen.game

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.labyrinthgame.R
import com.example.labyrinthgame.data.model.AppLevel
import com.example.labyrinthgame.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game), SensorEventListener {

    private val binding: FragmentGameBinding by viewBinding(FragmentGameBinding::bind)

    private lateinit var container: RelativeLayout



    private val sensorManager: SensorManager by lazy { requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val sensor: Sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    private lateinit var ballImg: ImageView
    private var cWidth = 0
    private var cHeight = 0
    private var ballPI = -1
    private var ballPJ = -1

    private var _height: Int? = null
    private var _width: Int? = null

    private var map: List<List<Int>> = listOf(
        listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        listOf(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1),
        listOf(1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1),
        listOf(1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1),
        listOf(1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1),
        listOf(1, 2, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
        listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
    )

    private var level: AppLevel = AppLevel(1, map.size, map[0].size, 10)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = binding.container

        container.post {
            cWidth = container.width
            cHeight = container.height
            loadView()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val y = it.values[0]
            val x = it.values[1]
            val z = it.values[2]

            if (_height != null) {
                var ballNewX = ballImg.x
                var ballNewY = ballImg.y

                if (x > 0) {
                    var wallX = 10000f

                    if (map[ballPI][ballPJ + 1] == 1) {
                        wallX = _width!! * (ballPJ + 1) * 1f
                    }

                    ballNewX = if (ballImg.x < (wallX - 5 - _width!!)) {
                        ballImg.x + x
                    } else {
                        ballImg.x
                    }

                    val newX = _width!! * (ballPJ + 1) * 1f

                    if ((y > 0 && map[ballPI + 1][ballPJ] == 1) || (y < 0 && map[ballPI - 1][ballPJ] == 1)) {
                        if (ballImg.x > (newX - 5)) {
                            ballPJ += 1
                        }
                    } else {
                        if (ballImg.x > (newX + 5 - _width!!)) {
                            ballPJ += 1
                        }
                    }

                } else {
                    var wallX = 0f

                    if (map[ballPI][ballPJ - 1] == 1) {
                        wallX = _width!! * (ballPJ - 1) * 1f
                    }

                    ballNewX = if (ballImg.x > (wallX + 5 + _width!!)) {
                        ballImg.x + x
                    } else {
                        ballImg.x
                    }

                    val newX = _width!! * (ballPJ - 1) * 1f

                    if ((y > 0 && map[ballPI + 1][ballPJ] == 1) || (y < 0 && map[ballPI - 1][ballPJ] == 1)) {
                        if (ballImg.x < (newX + 5)) {
                            ballPJ -= 1
                        }
                    } else {
                        if (ballImg.x < (newX - 5 + _width!!)) {
                            ballPJ -= 1
                        }
                    }
                }

                if (y > 0) {
                    var wallY = 10000f

                    if (map[ballPI + 1][ballPJ] == 1) {
                        wallY = _height!! * (ballPI + 1) * 1f
                    }

                    ballNewY = if (ballImg.y < (wallY - 5 - _height!!)) {
                        ballImg.y + y
                    } else {
                        ballImg.y
                    }

                    val newY = _height!! * (ballPI + 1) * 1f

                    if ((x > 0 && map[ballPI][ballPJ + 1] == 1) || (x < 0 && map[ballPI][ballPJ - 1] == 1)) {
                        if (ballImg.y > (newY - 5)) {
                            ballPI += 1
                        }
                    } else {
                        if (ballImg.y > (newY + 5 - _height!!)) {
                            ballPI += 1
                        }
                    }

                } else {
                    var wallY = 0f

                    if (map[ballPI - 1][ballPJ] == 1) {
                        wallY = _height!! * (ballPI - 1) * 1f
                    }

                    ballNewY = if (ballImg.y > (wallY + 5 + _height!!)) {
                        ballImg.y + y
                    } else {
                        ballImg.y
                    }

                    val newY = _height!! * (ballPI - 1) * 1f

                    if ((x > 0 && map[ballPI][ballPJ + 1] == 1) || (x < 0 && map[ballPI][ballPJ - 1] == 1)) {
                        if (ballImg.y < (newY + 5)) {
                            ballPI -= 1
                        }
                    } else {
                        if (ballImg.y < (newY - 5 + _height!!)) {
                            ballPI -= 1
                        }
                    }
                }

                Log.d("WWW", "wallI: $ballPI    wallJ: $ballPJ")
                ballImg.x = ballNewX
                ballImg.y = ballNewY

            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){}

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    private fun loadView() {
        _height = cHeight / level.row
        _width = cWidth / level.colum
        var bI = 0
        var bJ = 0
        var value = -1
        for (i in map.indices) {
//            Log.d("TTT", "line $i list ${map[i]}")
            for (j in 0 until map[i].size) {
                value = map[i][j]
//                Log.d("TTT" ,"index([$i],[$j]) value $value")
                if (value == 2) {
                    bI = i
                    bJ = j
                } else if (value == 1) {
                    val image = ImageView(requireContext())
                    image.scaleType = ImageView.ScaleType.FIT_XY
                    container.addView(image)
                    image.layoutParams.apply {
                        width = _width!!
                        height = _height!!
                    }
                    image.x = _width!! * j * 1f
                    image.y = _height!! * i * 1f
                    image.setImageResource(R.drawable.cube1)
                }
            }
        }
        addBall(bI, bJ, _width!!, _height!!)
    }

    private fun addBall(i: Int, j: Int, _width: Int, _height: Int) {
        ballPI = i
        ballPJ = j
        ballImg = ImageView(context)
        ballImg.setImageResource(R.drawable.metallball)
        ballImg.scaleType = ImageView.ScaleType.FIT_XY
        container.addView(ballImg)
        ballImg.layoutParams.apply {
            height = _height
            width = _width
        }

        ballImg.x = _width * j * 1f
        ballImg.y = _height * i * 1f

    }
}