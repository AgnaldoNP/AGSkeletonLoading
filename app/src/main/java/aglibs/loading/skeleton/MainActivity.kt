package aglibs.loading.skeleton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.forEach
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLoadingButton.setOnClickListener {
            loading(skeletonTextView.isLoading())
        }
    }

    private fun loading(loading: Boolean) {
        for (i in 0 until skeletonLoadingContainer.childCount) {
            (skeletonLoadingContainer.getChildAt(i) as? SkeletonTextView)?.let {
                if (!loading) {
                    it.startLoading()
                } else {
                    it.clearLoading()
                }
            }
        }
    }
}
