package org.bmsk.finease.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.children
import org.bmsk.finease.databinding.WidgetShuffleNumberKeyboardBinding
import kotlin.random.Random

class ShuffleNumberKeyboard @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    GridLayout(context, attrs, defStyleAttr), View.OnClickListener {

    // TODO 커스텀 뷰에 연결된 인스턴스는 자동으로 참조가 끊어지지 않기 때문에 뷰가 끝날 때 참조를 끊어주는 작업을 해 주어야 한다.
    private var _binding: WidgetShuffleNumberKeyboardBinding? = null
    private val binding
        get() = _binding
    private var listener: KeypadListener? = null

    init {
        _binding =
            WidgetShuffleNumberKeyboardBinding.inflate(LayoutInflater.from(context), this, true)
        _binding?.view = this
        _binding?.clickListener = this
        shuffle()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null // 참조를 끊어주는 작업
    }

    fun shuffle() {
        val keyNumberArray = mutableListOf<String>()
        for (i in 0..9) {
            keyNumberArray.add(i.toString())
        }

        binding?.gridLayout?.children?.forEach { view ->
            if (view is TextView && view.tag != null) {
                val randIndex = Random.nextInt(keyNumberArray.size)
                view.text = keyNumberArray[randIndex]
                keyNumberArray.removeAt(randIndex)
            }
        }
    }

    fun setKeypadListener(keypadListener: KeypadListener) {
        this.listener = keypadListener
    }

    fun onClickDelete() {
        listener?.onClickDelete()
    }

    fun onClickDone() {
        listener?.onClickDone()
    }

    interface KeypadListener {
        fun onClickNum(num: String)
        fun onClickDelete()
        fun onClickDone()
    }

    override fun onClick(v: View) {
        if (v is TextView && v.tag != null) {
            listener?.onClickNum(v.text.toString())
        }
    }
}