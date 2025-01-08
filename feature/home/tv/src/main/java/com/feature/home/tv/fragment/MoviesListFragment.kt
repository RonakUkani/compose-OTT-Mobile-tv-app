package com.feature.home.tv.fragment

import android.os.Bundle
import android.view.View
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import com.domain.model.Films
import com.feature.home.tv.presenter.ItemPresenter

class MoviesListFragment : RowsSupportFragment() {
    private var itemSelectedListener: ((Films.Result) -> Unit)? = null
    private var itemClickListener: ((Films.Result) -> Unit)? = null

    private val listRowPresenter =
        object : ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM) {
            override fun isUsingDefaultListSelectEffect(): Boolean = false
        }.apply {
            shadowEnabled = false
        }

    private var rootAdapter: ArrayObjectAdapter = ArrayObjectAdapter(listRowPresenter)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        adapter = rootAdapter

        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = ItemViewClickListener()
    }

    fun bindData(dataList: Films) {
        val arrayObjectAdapter = ArrayObjectAdapter(ItemPresenter())
        dataList.results.forEachIndexed { _, result ->
            arrayObjectAdapter.add(result)
        }
        val headerItem = HeaderItem("Movies")
        val listRow = ListRow(headerItem, arrayObjectAdapter)
        rootAdapter.add(listRow)
    }

    fun setOnContentSelectedListener(listener: (Films.Result) -> Unit) {
        this.itemSelectedListener = listener
    }

    fun setOnItemClickListener(listener: (Films.Result) -> Unit) {
        this.itemClickListener = listener
    }

    inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?,
        ) {
            if (item is Films.Result) {
                itemSelectedListener?.invoke(item)
            }
        }
    }

    inner class ItemViewClickListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?,
        ) {
            if (item is Films.Result) {
                itemClickListener?.invoke(item)
            }
        }
    }
}
