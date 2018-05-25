package com.bat.gokulkannan.cryptocoinanalyz.Adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bat.gokulkannan.cryptocoinanalyz.Common.Common
import com.bat.gokulkannan.cryptocoinanalyz.Interface.ILoadMore
import kotlinx.android.synthetic.main.coin_layout.view.*
import com.bat.gokulkannan.cryptocoinanalyz.Model.CoinModel
import com.bat.gokulkannan.cryptocoinanalyz.R
import com.squareup.picasso.Picasso


/**
 * Created by gokulkannan on 10/03/18.
 */

class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var coinIcon = itemView.coinIcon
    var coinSymbol = itemView.coinSymbol
    var coinName = itemView.coinName
    var coinPrice = itemView.priceUsd
    var oneHourChange = itemView.oneHour
    var twentyFourChange = itemView.twentyFourHour
    var sevenDayChange = itemView.sevenDay

}

class CoinAdapter(recyclerView: RecyclerView,internal var activity: Activity,var items:List<CoinModel>): RecyclerView.Adapter<CoinViewHolder>()
{
    internal var loadMore:ILoadMore?=null
    var isLoading:Boolean=false
    var visibleThreshold=5
    var lastVisibleItem:Int=0
    var totalItemCount:Int=0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int){
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                if(!isLoading && totalItemCount <= lastVisibleItem+visibleThreshold)
                {
                    if (loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }
            }
        })
    }

    fun setLoadMore(loadMore: ILoadMore)
    {
        this.loadMore = loadMore
    }

    override fun onBindViewHolder(holder: CoinViewHolder?, position: Int) {
        val CoinModel = items.get(position)

        val item = holder as CoinViewHolder

        item.coinName.text = CoinModel.name
        item.coinSymbol.text = CoinModel.symbol
        item.coinPrice.text = CoinModel.price_usd
        item.oneHourChange.text = CoinModel.percent_change_1h+"%"
        item.twentyFourChange.text = CoinModel.percent_change_24h+"%"
        item.sevenDayChange.text = CoinModel.percent_change_7d+"%"

        Picasso.with(activity.baseContext)
                .load(StringBuilder(Common.imageUrl)
                        .append(CoinModel.symbol!!.toLowerCase())
                        .append(".png")
                        .toString())
                .into(item.coinIcon)


        item.oneHourChange.setTextColor(if (CoinModel.percent_change_1h!!.contains("-"))
        Color.parseColor("#FF0000")
        else
        Color.parseColor("#32CD32"))

        item.twentyFourChange.setTextColor(if (CoinModel.percent_change_24h!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32"))

        item.sevenDayChange.setTextColor(if (CoinModel.percent_change_7d!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32"))
    }

    fun setLoaded(){
        isLoading = false
    }

    fun updateData(coinModels: List<CoinModel>)
    {
        this.items = coinModels
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CoinViewHolder {
        val  view = LayoutInflater.from(activity)
                .inflate(R.layout.coin_layout,parent,false)
        return CoinViewHolder(view)
    }

}