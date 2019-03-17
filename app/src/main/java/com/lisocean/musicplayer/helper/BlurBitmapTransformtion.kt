package com.lisocean.musicplayer.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
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
            (DisplayUtil.getScreenWidth(context) * 1.0 / DisplayUtil.getScreenHeight(context) * 1.0).toFloat()
        val cropBitmapWidth = (widthHeightSize * toTransform.height).toInt()
        val cropBitmapWidthX = ((toTransform.width - cropBitmapWidth) / 2.0).toInt()
        /*切割部分图片*/
        val cropBitmap = Bitmap.createBitmap(
            toTransform, cropBitmapWidthX, 0, cropBitmapWidth,
            toTransform.height)
        /*缩小图片*/
        val scaleBitmap = Bitmap.createScaledBitmap(
            cropBitmap, toTransform.width / 50, toTransform.height / 50, false)
        /*模糊化*/
        val blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true)

        val foregroundDrawable = BitmapDrawable(blurBitmap)
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        return foregroundDrawable.bitmap
    }
}