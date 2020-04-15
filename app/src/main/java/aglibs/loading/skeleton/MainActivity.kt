package aglibs.loading.skeleton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLoadingButton.setOnClickListener {
            if (!skeletonTextView.isLoading()) {
                skeletonTextView.startLoading()
                skeletonTextView2.startLoading()
            } else {
                skeletonTextView.clearLoading()
                skeletonTextView2.clearLoading()
            }
        }
    }
}
