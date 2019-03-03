package com.lisocean.musicplayer.ui.activity

import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.model.AudioMediaBean
import com.lisocean.musicplayer.ui.adapter.LmPagerAdapter
import com.lisocean.musicplayer.util.parseCursor
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setData()
        setListener()
    }
    lateinit var list: List<AudioMediaBean>
    private fun setListener() {

    }

    private fun setData() {
        setSupportActionBar(toolbar)
        val adapter = LmPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        loadSongData()

    }
    private fun loadSongData(){
        //加载音乐数据
        val handler = @SuppressLint("HandlerLeak")
        object : AsyncQueryHandler(contentResolver) {
            override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
                logCursor(cursor)
                list = parseCursor(cursor)
            }
        }
        //开始查询
        handler.startQuery(0, null, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.ARTIST_ID),
            null, null, null)
    }

    fun logCursor(cursor: Cursor?){
        cursor?.let {
            it.moveToPosition(-1)
            while (it.moveToNext()){
                for (index in 0 until it.columnCount){
                    println("key= ${it.getColumnName(index)} --value= ${it.getString(index)}")
                }
            }
        }
    }
    /**
     * be of toolbar
     */
    private lateinit var searchView: SearchView
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbarmenu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_search -> {
                searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }

                })
            }
            R.id.menu_other -> toast("其他")
            R.id.menu_cycling -> toast("扫描本地音乐")
        }
        return true
    }


//    class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "content:/" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.path, null, 1) {
//        companion object {
//            private var instance: MyDatabaseOpenHelper? = null
//
//            @Synchronized
//            fun getInstance(ctx: Context): MyDatabaseOpenHelper {
//                if (instance == null) {
//                    instance = MyDatabaseOpenHelper(ctx.applicationContext)
//                }
//                return instance!!
//            }
//        }
//
//        override fun onCreate(db: SQLiteDatabase) {
//            // Here you create tables
//        }
//
//        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//            // Here you can upgrade tables, as usual
//        }
//    }
//
//    // Access property for Context
//    private val Context.database: MyDatabaseOpenHelper
//        get() = MyDatabaseOpenHelper.getInstance(applicationContext)
//    private fun asyncGetMusic() {
//
//        try {
//            database.use {
//                select(
//                    MediaStore.Audio.Media.DATA
//                ).exec {
//                    logCursor(this)
//                    this.close()
//                }
//            }
//        }catch (e : Exception){
//            println("452543534543543"+e.message)
//
//
//        }
//
//    }


}
