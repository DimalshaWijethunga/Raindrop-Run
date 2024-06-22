import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random
import com.example.nightwinggame.R
import com.example.nightwinggame.GameTask


class GameView(var c: Context, var gameTask: GameTask) : View(c) {
    private var myPaint: Paint = Paint()
    private var speed = 1
    private var time = 0
    private var score = 0
    private var foxPosition = 1 // Start fox in the middle lane
    private val raindrops = ArrayList<HashMap<String, Any>>()

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint.color = Color.BLACK
        myPaint.textSize = 60f
        myPaint.style = Paint.Style.FILL_AND_STROKE
        myPaint.isFakeBoldText = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["position"] = Random.nextInt(3) // Random position
            map["startTime"] = time
            raindrops.add(map)
        }
        time += 10 + speed

        drawFox(canvas)
        drawRaindrops(canvas)

        // Remove raindrops that are out of screen
        for (i in raindrops.indices.reversed()) {
            val dropY = time - raindrops[i]["startTime"] as Int
            if (dropY > viewHeight + RAINDROP_HEIGHT) {
                raindrops.removeAt(i)
                score++
                speed = 1 + score / 8
            }
        }

        canvas.drawText("Score: $score", 80f, 80f, myPaint)
        canvas.drawText("Speed: $speed", 380f, 80f, myPaint)
        invalidate()
    }

    private fun drawFox(canvas: Canvas) {
        val foxDrawable = resources.getDrawable(R.drawable.fox, null)
        val left = foxPosition * viewWidth / 3 + viewWidth / 15 + 25
        val top = viewHeight - 2 - FOX_HEIGHT
        val right = left + FOX_WIDTH - 50 // Adjusted right bound
        val bottom = viewHeight - 2
        foxDrawable.setBounds(left, top, right, bottom)
        foxDrawable.draw(canvas)
    }

    private fun drawRaindrops(canvas: Canvas) {
        for (i in raindrops.indices) {
            val position = raindrops[i]["position"] as Int
            val dropX = position * viewWidth / 3 + viewWidth / 15
            val dropY = time - raindrops[i]["startTime"] as Int
            val dropDrawable = resources.getDrawable(R.drawable.raindrop_1, null)
            val left = dropX + 25
            val top = dropY - RAINDROP_HEIGHT
            val right = left + RAINDROP_WIDTH - 50 // Adjusted right bound
            val bottom = dropY
            dropDrawable.setBounds(left, top, right, bottom)
            dropDrawable.draw(canvas)
            if (position == foxPosition && dropY > viewHeight - 2 - FOX_HEIGHT && dropY < viewHeight - 2) {
                gameTask.closeGame(score)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (foxPosition > 0) {
                        foxPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (foxPosition < 2) {
                        foxPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }

    companion object {
        private const val FOX_WIDTH = 300 // Adjust fox width as needed
        private const val FOX_HEIGHT = 300 // Adjust fox height as needed
        private const val RAINDROP_WIDTH = 100 // Adjust raindrop width as needed
        private const val RAINDROP_HEIGHT = 100 // Adjust raindrop height as needed
    }
}
