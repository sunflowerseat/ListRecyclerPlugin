# ListRecyclerPlugin
类似插件一样方便地为listview和recyclerview添加头部广告位，下拉刷新，上拉加载，滑动删除功能

listview的也写了，但不知道意义大不大，感觉没什么用，listview本身就有addHeader和Footer的方法

经过两个星期的努力，终于把这个插件初步封装完毕。对于这个库的封装过程，可以参考我的博客：http://blog.csdn.net/Fancy_xty/article/details/51958087
已上传到jcenter，recyclerview的依赖方式
`compile 'com.fancy.library:recyclerplugin:1.0.0'`
listview的依赖方式
`compile 'com.fancy.library:listplugin:1.0.0'`

效果图如下：

![alt text](https://github.com/sunflowerseat/ListRecyclerPlugin/blob/master/preview/header.png "Title" )

![alt text](https://github.com/sunflowerseat/ListRecyclerPlugin/blob/master/preview/swipe_loadmore.png "Title")

现在的使用方法是这样的。
```
//创建一个RecyclerPlugin
plugin = new RecyclerPlugin(this,recycler, mAdapter);
//创建广告位（提供多种方式）
plugin.createHeader(getLayoutInflater(),R.layout.headview);
//创建底部加载更多视图  this是一个监听，当加载更多界面显示时的调用的方法  
plugin.createAddMore(getLayoutInflater() ,this);
//把最终包装好的Adapter设置到RecyclerView中
recycler.setAdapter(plugin.getLastAdapter());

```
就这样简单四步就完成了添加广告位和底部加载更多的功能，无需改动任何的布局文件。
是不是非常简单。如果有类似的list，但不需要这些功能的时候，只需要把和plugin相关的所有代码删除即可。
listview使用这个插件，用法基本一致，创建一个ListPlugin即可。




接下来说说添加侧滑功能，首先要添加侧滑功能，布局文件要修改几乎是不可避免的了，不然怎么知道侧滑之后的布局，以及侧滑菜单的点击事件呢。
不过所幸的是，改动也不大，准确来说是添加一部分代码，原先写好的代码仍然不需要进行修改,在原先代码的基础上增加部分代码即可。
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
在Adapter中把整个SwipeLayout传入`plugin.addSwipe(holder.swipeLayout);`
主要是提供管理，保证在任何时候都只能有一个item侧滑，当上下滑动listview的时候，所有的侧滑菜单均保持收起状态。
如果要实现侧滑删除的功能，还需要在删除的事件中添加：
`plugin.deleteSwipe(holder.swipeLayout);`
保证在删除之后，所有的侧滑菜单都保持收起状态。

下拉刷新现在还没有封装起来，不过也提供了管理，下拉刷新可以在listview或Recyclerview外层包裹SwipeRefreshLayout，然后把SwipeRefreshLayout传给RecyclerPlugin或者ListPlugin来进行管理，主要是处理侧滑和下拉刷新的冲突，还有广告位的滑动冲突。
