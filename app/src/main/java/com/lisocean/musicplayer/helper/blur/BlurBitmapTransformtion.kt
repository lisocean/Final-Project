package com.lisocean.musicplayer.helper.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.renderscript.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

@Suppress("DEPRECATION")
class BlurBitmapTransformtion(val context: Context) : BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("blur bitmap transform".toByteArray())
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        val widthHeightSize =
            (DisplayUtil.getScreenWidth(context) * 1.0 / DisplayUtil.getScreenHeight(
                context
            ) * 1.0).toFloat()
        val cropBitmapWidth = (widthHeightSize * toTransform.height).toInt()
//        val cropBitmapWidthX = ((toTransform.width - cropBitmapWidth) / 2.0).toInt()
//        /*切割部分图片*/
//        val cropBitmap = Bitmap.createBitmap(
//            toTransform, cropBitmapWidthX, 0, cropBitmapWidth,
//            toTransform.height)
        /*缩小图片*/
        val scaleBitmap = Bitmap.createScaledBitmap(
            toTransform, toTransform.width / 50, toTransform.height / 50, false)
        /*模糊化*/
        return adjustBrightness(
            FastBlurUtil.doBlur(scaleBitmap, 8, true)
        )


    }
    /**
     * set alpha 0.5f
     */
    private val BRIGHTNESS_ADJUSTMENT_FACTOR_MATRIX =
        Matrix3f(floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f))
    fun adjustBrightness(image: Bitmap): Bitmap {

        val inputBitmap = Bitmap.createBitmap(image)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rsColorMatrix = RenderScript.create(context)
        val scriptIntrinsicColorMatrix = ScriptIntrinsicColorMatrix.create(rsColorMatrix, Element.U8_4(rsColorMatrix))
        val colorMatrixIn = Allocation.createFromBitmap(rsColorMatrix, inputBitmap)
        val colorMatrixOut = Allocation.createFromBitmap(rsColorMatrix, outputBitmap)

        scriptIntrinsicColorMatrix.setColorMatrix(BRIGHTNESS_ADJUSTMENT_FACTOR_MATRIX)
        scriptIntrinsicColorMatrix.forEach(colorMatrixIn, colorMatrixOut)
        colorMatrixOut.copyTo(outputBitmap)
        rsColorMatrix.destroy()

        return outputBitmap
    }

}