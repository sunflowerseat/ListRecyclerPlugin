# ListRecyclerPlugin
类似插件一样方便地为listview和recyclerview添加头部广告位，下拉刷新，上拉加载，滑动删除功能

listview的还没写，不知道意义大不大，感觉没什么用，listview本身就有addHeader和Footer的方法

RecyclerView 和Listview在项目中算是非常常用的了，不过在博客的开头，我还是要重申一下这个库的特点，几乎无需修改原有代码，而且所有功能全都独立。RecyclerView 和Listview用法大家都非常清楚，有时候我们经常在编写好listview和Recyclerview中的内容之后，忽然想起要添加刷新，上拉加载，侧滑，甚至是广告位的功能。
这个时候就会开始去找各种开源的被封装好的具有这些功能的RecyclerView和listview
好吧，博主自己就是这样的，可以说没什么计划，或者有时候经理忽然说要给list添加一个新功能，这时候要为list添加其他新功能，通常都要做挺大的改动。
说说我自己的经历吧，刚开始把list都写好之后，Adapter也已经写好，忽然要添加侧滑的功能，于是上网找了一个侧滑的例子，SwipeList，经过几多波折，终于如愿的增加了这个功能，忽然又有一天，又要增加刷新和加载功能，只好又去找带刷新和加载的list。当然是有的，但是后来发现，这些都被封装成自定义控件，和之前的SwipeList是无法同时使用的。所以最后只好去找三种功能都有的List了。最后当然是找到了，不过找到的三者兼具的第三方控件侵入性实在是太强了，要导入很多个包不说，在使用侧滑的时候必须用封装好的SwipeAdapter，这就导致了我要对之前的Adapter进行大幅度的修改，如果我不是特别清楚里面的规则很容易就写错，事实上我在改来改去的过程中就改错了，但由于没有去看源码，也没仔细去理解那一大堆代码，问题很难被找出来。
对于有一定开发经验的开发者来说，很有可能都有自己对Adapter的独特封装，对于这种需要大面积修改Adapter的行为，我还是不太赞同，即使你真的比别人封装的好，但是全面改变用户的使用习惯，还是有些难以让人接受的。
有了这些失败的经历以后，我开始思考怎样在不改动原来代码的基础上，只简单的添加一些代码就能实现这些新添加的功能。
恰好在公众号上看到洋神的博客，通过装饰者模式，对Adapter进行一层包裹实现其他功能，这是个不错的思路，重点是不去自定义ListView或者RecyclerView。
看完洋神博客之后，我就开始跃跃欲试，把代码clone下来研究，希望能够在这基础上进一步封装。不过很快我就发现了很多问题，不过最终我还是把这种装饰者模式的思想保留了下来，并且通过一个插件类去管理header、footer/Swipe/Refresh。主要是为它们提供接口和解决各种滑动冲突。
经过两个星期的努力，终于把这个插件初步封装完毕。
现在的使用方法是这样的。
```
//创建一个RecyclerPlugin
plugin = new RecyclerPlugin(this,recycler, mAdapter);
//创建广告位（提供多种方式）
plugin.createBannerHeader(getLayoutInflater(), localImages);
//创建底部加载更多视图
plugin.createAddMore(getLayoutInflater() ,this);
//把最终包装好的Adapter设置到RecyclerView中
recycler.setAdapter(plugin.getLastAdapter());
```
就这样简单四步就完成了添加广告位和底部加载更多的功能，无需改动任何的布局文件。
是不是非常简单。如果有类似的list，但不需要这些功能的时候，只需要把和plugin相关的所有代码删除即可。
listview使用这个插件，用法基本一致，创建一个ListPlugin即可。

接下来说说添加侧滑功能，首先要添加侧滑功能，布局文件要修改几乎是不可避免的了，不然怎么知道侧滑之后的布局，以及侧滑菜单的点击事件呢。
不过所幸的是，改动也不大，准确来说是添加一部分代码，原先写好的代码仍然不需要进行修改。
```
<com.fancy.recycler_plugin.swipe.SwipeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/swipe">
        <!-- listview原有的布局文件 -->
        <XXLayout
            android:layout_width="match_parent"
            android:layout_height="54dp"
            android:text="Test"
            android:padding="8dp"
            android:textSize="18sp"
            android:gravity="center_vertical"
            android:id="@+id/id_item_list_title"/>
        <!-- 侧滑菜单的布局 -->
        <SwipeMenuXXLayout
            android:layout_width="60dp"
            android:layout_height="match_parent"
            android:background="#ff0000"
            android:text="delete"
            android:textColor="#ffffff"
            android:id="@+id/swipemenu"
            />

    </com.fancy.recycler_plugin.swipe.SwipeLayout>
```
在Adapter中为原有的XXLayout添加点击事件代替setOnItemClickListener();
为SwipeMenuXXLayout添加点击事件，或者为这个布局内部的控件添加点击事件，这都是自己控制的了。
在Adapter中把整个SwipeLayout传入`SwipeLayout.addSwipeView(holder.swipeLayout);`
主要是提供管理，保证在任何时候都只能有一个item侧滑，当上下滑动listview的时候，所有的侧滑菜单均保持收起状态。
如果要实现侧滑删除的功能，还需要在删除的事件中添加：
`SwipeLayout.removeSwipeView(holder.swipeLayout);`
保证在删除之后，所有的侧滑菜单都保持收起状态。

下拉刷新现在还没有封装起来，不过也提供了管理，下拉刷新可以在listview或Recyclerview外层包裹SwipeRefreshLayout，然后把SwipeRefreshLayout传给RecyclerPlugin或者ListPlugin来进行管理，主要是处理侧滑和下拉刷新的冲突，还有广告位的滑动冲突。