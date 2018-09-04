package mbass.android.pushsample

import android.app.NotificationManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.firebase.messaging.RemoteMessage
import com.nifcloud.mbaas.core.NCMBDialogPushConfiguration
import com.nifcloud.mbaas.core.NCMBFirebaseMessagingService
import com.nifcloud.mbaas.core.NCMBPush
import java.util.*

class MyCustomService : NCMBFirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage != null && remoteMessage.data != null) {
            val bundle = getBundleFromRemoteMessage(remoteMessage)
            if (bundle.containsKey("com.nifty.Dialog")) {

                //ダイアログを設定しないタイプ
                //dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_NONE);

                //標準的なダイアログを表示するタイプ
                dialogPushConfiguration.displayType = NCMBDialogPushConfiguration.DIALOG_DISPLAY_DIALOG

                //背景画像を設定するタイプ
                //dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_BACKGROUND);

                //オリジナルのレイアウトを設定するタイプ
                //dialogPushConfiguration.setDisplayType(NCMBDialogPushConfiguration.DIALOG_DISPLAY_ORIGINAL);

                //ダイアログ表示のハンドリング
                NCMBPush.dialogPushHandler(this, bundle, dialogPushConfiguration)

            } else {
                sendNotification(bundle)
            }

        }
    }

    private fun sendNotification(pushData: Bundle) {

        //サイレントプッシュ
        if (!pushData.containsKey("message") && !pushData.containsKey("title")) {
            return
        }

        val notificationBuilder = notificationSettings(pushData)

        /*
         * 通知重複設定
         * 0:常に最新の通知のみ表示
         * 1:最新以外の通知も複数表示
         */
        var appInfo: ApplicationInfo? = null
        try {
            appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            throw IllegalArgumentException(e)
        }

        val containsKey = appInfo!!.metaData.containsKey(NOTIFICATION_OVERLAP_KEY)
        val overlap = appInfo.metaData.getInt(NOTIFICATION_OVERLAP_KEY)

        //デフォルト複数表示
        var notificationId = Random().nextInt()

        if (overlap == 0 && containsKey) {
            //最新のみ表示
            notificationId = 0
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        internal val NOTIFICATION_OVERLAP_KEY = "notificationOverlap"
        internal var dialogPushConfiguration = NCMBDialogPushConfiguration()
    }
}