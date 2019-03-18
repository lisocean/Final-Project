package com.lisocean.musicplayer.helper.blur

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.lisocean.renderscript.ScriptC_sketch
import java.security.MessageDigest


@Suppress("DEPRECATION")
class BlurTransformation(val context: Context, var radius : Float) : BitmapTransformation() {

   private val rs by lazy { RenderScript.create(context) }
    /**
     * set alpha 0.5f
     */
    private val BRIGHTNESS_ADJUSTMENT_FACTOR_MATRIX =
        Matrix3f(floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f))

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("blur transform".toByteArray())
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        var blur = toTransform.copy(Bitmap.Config.ARGB_8888, true)
        while (radius >= 0){
            blur = if(radius >25.0f)
                blur.blurTranfrom(25.0f)
            else
                blur.blurTranfrom(radius)
            radius -= 25.0f
        }
        blur = adjustBrightness(blur)
        rs.destroy()
        return blur
    }
    private fun Bitmap.blurTranfrom(radius_temp : Float) : Bitmap{
        var blurredBitmap = copy(Bitmap.Config.ARGB_8888, true)
        //(1) create the RenderScript
        // Allocate memory for Renderscript to work with
        //(2)
        //分配用于渲染脚本的内存
        val input = Allocation.createFromBitmap(rs, blurredBitmap,Allocation.MipmapControl.MIPMAP_FULL,Allocation.USAGE_SHARED)
        val output = Allocation.createTyped(rs, input.type)
        //(3)
        // Load up an instance of the specific script that we want to use.
        //加载我们想要使用的特定脚本的实例
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
//        val s = ScriptC_sketch(rs)
        //(4)
        script.setInput(input)
        //(5)
        // Set the blur radius
        //设置模糊半径[0,25.0f]
        script.setRadius(radius_temp)
        //(6)
        // Start the ScriptIntrinisicBlur
        //启动ScriptIntrinisicBlur
        script.forEach(output)
        //(7)
        // Copy the output to the blurred bitmap
        //将输出复制到模糊的位图
        output.copyTo(blurredBitmap)
        return blurredBitmap
    }

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