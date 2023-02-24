package com.innovative.wearapp

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.text.format.DateFormat
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.color
import com.innovative.wearapp.databinding.ActivityMainBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutParams: ViewGroup.LayoutParams = binding.liWatchLayer.layoutParams
        layoutParams.height = resources.displayMetrics.widthPixels
        binding.liWatchLayer.layoutParams = layoutParams
        binding.liFasting.layoutParams = layoutParams
        binding.liWeek.layoutParams = layoutParams
        //adjustInset()
        binding.circularProgressBar.apply {
            progress = 0f
            setProgressWithAnimation(0f, 1000) // =1s
            progressMax = 100f
            progressBarColor = Color.RED
            progressBarColorStart = Color.parseColor("#F6D239")
            progressBarColorEnd = Color.parseColor("#FB9640")
            progressBarColorDirection = CircularProgressBar.GradientDirection.BOTTOM_TO_END
            backgroundProgressBarColor = Color.WHITE
            progressBarWidth = 10f // in DP
            backgroundProgressBarWidth = 10f // in DP
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_LEFT
        }

        binding.tvStartEnd.setOnClickListener {
            it as TextView
            if (it.text.toString() == "Start") {
                val fasting =
                    SpannableStringBuilder().color(Color.parseColor("#FFFFFF")) { append("Fasting ") }
                        .color(Color.parseColor("#FB9640")) { append("00%") }
                binding.tvFastingStatus.text = fasting
                it.text = "End"
                timer.start()
            } else {
                timer.onFinish()
                timer.cancel()
            }
        }

        val fasting =
            SpannableStringBuilder().color(Color.parseColor("#FFFFFF")) { append(getCurrentTime()) }
        binding.tvFastingStatus.text = fasting
        binding.tvTiming.text = "01:40"
    }


    //private var endTime : Long = 1000*60*1
    private var endTime: Long = 1000 * 101
    private var timeInterval: Long = 1000
    private var currentTime: Long = 0
    private var percentage: Int = 0
    private var currentProgress: Float = 0f
    private val timer = object : CountDownTimer(endTime, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.tvTiming.text = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                ),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                )
            )
            if (timeInterval == currentTime) {
                percentage += 1
                currentProgress += 1
                binding.circularProgressBar.apply {
                    progress = currentProgress
                    setProgressWithAnimation(currentProgress, 1000) // =1s
                }
                val fasting =
                    SpannableStringBuilder().color(Color.parseColor("#FFFFFF")) { append("Fasting ") }
                        .color(Color.parseColor("#FB9640")) {
                            append(
                                String.format(
                                    "%02d",
                                    percentage
                                ) + "%"
                            )
                        }
                binding.tvFastingStatus.text = fasting
                currentTime = 0
            }
            currentTime += 1000
        }

        override fun onFinish() {
            binding.tvTiming.text = "01:40"
            val fasting = SpannableStringBuilder().color(Color.parseColor("#FFFFFF")) {
                append(getCurrentTime())
            }
            binding.tvFastingStatus.text = fasting
            currentTime = 0
            percentage = 0
            binding.circularProgressBar.apply {
                progress = currentProgress
                setProgressWithAnimation(0f, 1000) // =1s
            }
            currentProgress = 0f
            binding.tvStartEnd.text = "Start"
        }
    }

    private fun getCurrentTime(): String {
        val delegate = "hh:mm aaa"
        val calendar = Calendar.getInstance()
        //calendar.timeZone = TimeZone.getTimeZone("Asia/Calcutta")
        calendar.timeZone = TimeZone.getDefault()
        calendar.time
        return DateFormat.format(delegate, calendar).toString()
    }

    private fun adjustInset() {
        if (applicationContext.resources.configuration.isScreenRound) {
            val inset = (FACTOR * resources.displayMetrics.widthPixels).toInt()
            binding.root.setPadding(inset, inset, inset, inset)
        }
    }

    companion object {
        private const val FACTOR = 0.146467f // c = a * sqrt(2)
    }
}