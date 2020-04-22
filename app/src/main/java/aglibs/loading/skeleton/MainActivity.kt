package aglibs.loading.skeleton

import aglibs.loading.skeleton.drawer.ISkeletonDrawer
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var loading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLoadingButton.setOnClickListener {
            loading(skeletonLoadingContainer, loading)
            loading = !loading
        }
    }

    private fun loading(view: View, loading: Boolean) {
        view.takeIf { view is ViewGroup }?.let {
            for (i in 0 until (view as ViewGroup).childCount) {
                val childAt = view.getChildAt(i)
                (childAt as? ISkeletonDrawer)?.let {
                    if (!loading) {
                        it.startLoading()
                    } else {
                        it.stopLoading()
                    }
                }
                run { loading(childAt, loading) }
            }
        }

    }
}
