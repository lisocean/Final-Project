package com.lisocean.musicplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.model.data.AudioMediaBean
import kotlinx.android.synthetic.main.item_singlemusic.view.*
import org.jetbrains.anko.toast

@Suppress("DEPRECATION")
class SingleMusicItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.item_singlemusic, this).apply {
            setOnClickListener {
                context.toast("主体")
            }
        }
        isLove.setOnClickListener {
            isSelected = when(isSelected){
                true -> false
                false -> true
            }
        }

        popUpMoreAbout.setOnClickListener {
        }
    }
    fun setData(index : Int,  audioMediaBean: AudioMediaBean){
        textOfIndex.text = index.toString()
        textOfArtist.text = audioMediaBean.artist
        textOfSong.text = audioMediaBean.title

    }
}