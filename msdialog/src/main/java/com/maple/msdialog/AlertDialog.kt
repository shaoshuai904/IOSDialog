package com.maple.msdialog

import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.maple.msdialog.databinding.DialogAlertBinding
import com.maple.msdialog.utils.DensityUtils
import com.maple.msdialog.utils.DialogUtil.setScaleWidth

/**
 * 警告框式Dialog [ 标题 + 消息文本 + 左按钮 + 右按钮 ]
 *
 * @author maple
 * @time 2017/3/21
 */
class AlertDialog(
        private val mContext: Context,
        val config: DialogConfig = DialogConfig(mContext)
) : Dialog(mContext, R.style.AlertDialogStyle) {
    private val binding: DialogAlertBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.dialog_alert, null, false)
    private var showTitle = false
    private var showMsg = false
    private var showRightBtn = false
    private var showLeftBtn = false

    constructor(mContext: Context) : this(mContext, DialogConfig(mContext))

    init {
        // get custom Dialog layout
        binding.apply {
            tvTitle.visibility = View.GONE
            tvMsg.visibility = View.GONE
            btLeft.visibility = View.GONE
            btRight.visibility = View.GONE
            ivLine.visibility = View.GONE
        }

        // set Dialog style
        setContentView(binding.root)
    }

    fun setScaleWidth(scWidth: Double): AlertDialog {
        setScaleWidth(binding.root, scWidth)
        return this
    }

    fun getRootView() = binding.root
    fun getTitleView() = binding.tvTitle
    fun getMessageView() = binding.tvMsg
    fun getLeftBtnView() = binding.btLeft
    fun getRightBtnView() = binding.btRight

    // 点击外围是否可取消
    fun setDialogCancelable(cancelable: Boolean = true): AlertDialog {
        setCancelable(cancelable)
        setCanceledOnTouchOutside(cancelable)
        return this
    }

    fun setDialogTitle(title: CharSequence?): AlertDialog {
        return setTitle(title, isBold = false)
    }

    override fun setTitle(title: CharSequence?) {
        this.setTitle(title, isBold = false)
    }

    fun setTitle(
            title: CharSequence?,
            color: Int = config.titleColor,// 字体颜色
            spSize: Float = config.titleTextSizeSp,// 字体大小
            isBold: Boolean = false// 是否加粗
    ): AlertDialog {
        showTitle = true
        binding.tvTitle.apply {
            text = title
            setTextColor(color)
            textSize = spSize
            setPadding(config.titlePaddingLeft, config.titlePaddingTop, config.titlePaddingRight, config.titlePaddingBottom)
            setTypeface(typeface, if (isBold) Typeface.BOLD else Typeface.NORMAL)
        }
        return this
    }

    fun setHtmlMessage(message: String?): AlertDialog {
        return setMessage(Html.fromHtml(message))
    }

    fun setMessage(message: CharSequence?) = setMessage(message, isBold = false)

    fun setMessage(
            message: CharSequence?,
            color: Int = config.messageColor,// 字体颜色
            spSize: Float = config.messageTextSizeSp, // 字体大小
            isBold: Boolean = false, // 是否加粗
            gravity: Int = Gravity.CENTER// 偏左，居中，偏右
    ): AlertDialog {
        showMsg = true
        binding.tvMsg.apply {
            text = message
            setTextColor(color)
            this.gravity = gravity
            textSize = spSize
            setPadding(config.messagePaddingLeft, config.messagePaddingTop, config.messagePaddingRight, config.messagePaddingBottom)
            setTypeface(typeface, if (isBold) Typeface.BOLD else Typeface.NORMAL)
        }
        return this
    }

    fun setBottomViewHeightDp(heightDp: Float = config.bottomViewHeightDp) = setBottomViewHeight(DensityUtils.dp2px(mContext, heightDp))

    fun setBottomViewHeight(heightPixels: Int): AlertDialog {
        with(binding.llBottom) {
            layoutParams = layoutParams.apply { height = heightPixels }
        }
        return this
    }

    fun setLeftButton(
            text: CharSequence?,
            listener: View.OnClickListener? = null
    ) = setLeftButton(text, listener, config.leftBtnColor, config.leftBtnTextSizeSp, false)

    fun setLeftButton(
            text: CharSequence?,
            listener: View.OnClickListener? = null,
            color: Int = config.leftBtnColor,
            spSize: Float = config.leftBtnTextSizeSp,
            isBold: Boolean = false
    ): AlertDialog {
        showLeftBtn = true
        binding.btLeft.apply {
            this.text = text
            setTextColor(color)
            textSize = spSize
            setTypeface(typeface, if (isBold) Typeface.BOLD else Typeface.NORMAL)
            setOnClickListener { v ->
                listener?.onClick(v)
                dismiss()
            }
        }
        return this
    }

    fun setRightButton(
            text: CharSequence?,
            listener: View.OnClickListener? = null
    ) = setRightButton(text, listener, config.rightBtnColor, config.rightBtnTextSizeSp, false)

    fun setRightButton(
            text: CharSequence?,
            listener: View.OnClickListener? = null,
            color: Int = config.rightBtnColor,
            spSize: Float = config.rightBtnTextSizeSp,
            isBold: Boolean = false
    ): AlertDialog {
        showRightBtn = true
        binding.btRight.apply {
            this.text = text
            setTextColor(color)
            textSize = spSize
            setTypeface(typeface, if (isBold) Typeface.BOLD else Typeface.NORMAL)
            setOnClickListener { v ->
                listener?.onClick(v)
                dismiss()
            }
        }
        return this
    }

    private fun setLayout() {
        setScaleWidth(binding.root, config.scaleWidth)
        setBottomViewHeightDp()

        binding.apply {
            if (!showTitle && !showMsg) {
                tvTitle.text = ""
                tvTitle.visibility = View.VISIBLE
            }
            tvTitle.visibility = if (showTitle) View.VISIBLE else View.GONE
            tvMsg.visibility = if (showMsg) View.VISIBLE else View.GONE
            // one button
            if (!showRightBtn && !showLeftBtn) {
                btRight.text = "确定"
                btRight.visibility = View.VISIBLE
                btRight.setBackgroundResource(R.drawable.sel_alert_dialog_single)
                btRight.setOnClickListener { dismiss() }
            }
            if (showRightBtn && !showLeftBtn) {
                btRight.visibility = View.VISIBLE
                btRight.setBackgroundResource(R.drawable.sel_alert_dialog_single)
            }
            if (!showRightBtn && showLeftBtn) {
                btLeft.visibility = View.VISIBLE
                btLeft.setBackgroundResource(R.drawable.sel_alert_dialog_single)
            }
            // two button
            if (showRightBtn && showLeftBtn) {
                btRight.visibility = View.VISIBLE
                btRight.setBackgroundResource(R.drawable.sel_alert_dialog_right)
                btLeft.visibility = View.VISIBLE
                btLeft.setBackgroundResource(R.drawable.sel_alert_dialog_left)
                ivLine.visibility = View.VISIBLE
            }
        }
    }

    override fun show() {
        setLayout()
        super.show()
    }

}