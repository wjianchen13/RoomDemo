# 房间demo

# 上下切换房间方案
1. 滑动时房间内容不滚动
2. VerticalViewPager参考
   https://github.com/ilpanda/live_demo
   android快速仿花椒，映客直播上下滑动切换直播间
   https://blog.csdn.net/duolaimila/article/details/72189790
   Android - 直播上下滑动布局（1）
   https://blog.csdn.net/stupid56862/article/details/107774374


3. 小视频
4. 声网秒开

# 参考文档
用来测试的小视频地址集
https://segmentfault.com/a/1190000008427710

# 注意问题
live_demo里面的切换逻辑
当ViewPager往上滑动时（查看之后的数据），onPageScrolled的position到最后一刻才变成当前选中item的position，其余时刻一直都是前一个item的position
从第0个划到第1个时
第0个item的position从0变成-1.0
第1个item的position从1.0变成0
从第1个滑动到第2个时
第0个item的position从-1.0f变成-2.0f
第1个item的position从0变成-1.0f
第2个item的position从1.0变成0
以此类推，这种情况满足(position < 0 && viewGroup.getId() != mCurrentItem)
但是从第一个划到第二个是，虽然满足上面的条件，但是不会触发removeView，因为内部的条件不满足了，它已经移除了View
这是触发removeView1，移除了前一个item的显示界面。
接着当前item的position=0,View Id 也等于当前选中item的Id，并且当前item position不等于mLastPosition
所以会请求数据，并且把显示界面的View添加到当前选中的item中。
从上往下都满足上面的条件。

如果往下滑动（查看之前的数据），onPageScrolled的position一开始就变成当前选中item的position，
后一个item的position从1.0f变成2.0f
当前item的position从0f变成1.0f
前一个item的position从-1.0f变成0f
这种情况触发(viewGroup.getId() == mCurrentItem && (position == 0) && mCurrentItem != mLastPosition)
会先把显示界面从先前的viewGroup移除，再添加到当前item的ViewGroup中。

