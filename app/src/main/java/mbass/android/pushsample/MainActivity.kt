package mbass.android.pushsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBPush

class MainActivity : AppCompatActivity() {
    val appKey: String      = "[APP_KEY]"
    val clientKey: String   = "[CLIENT_KEY]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NCMB.initialize(this, appKey, clientKey)
    }

    public override fun onResume() {
        super.onResume()
        //リッチプッシュ通知の表示
        NCMBPush.richPushHandler(this, intent)
        //リッチプッシュを再表示させたくない場合はintentからURLを削除します
        intent.removeExtra("com.mbaas.nifcloud.RichUrl")
    }
}
