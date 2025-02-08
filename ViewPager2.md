# ViewPager2 切换过程transformPage()方法 position值变化过程
第0个滑动到第1个
第0个View  0.0f  ->  -1.0f
第1个View  1.0f  ->  0.0f
第2个View  2.0f  ->  1.0f

第1个滑动到第2个
第0个View  -1.0f  ->  -2.0f
第1个View  0.0f  -->  -1.0f
第2个View  1.0f  -->  0.0f
第3个View  2.0f  -->  1.0f

第2个滑动到第3个
第1个View  -1.0f  ->  -2.0f
第2个View  0.0f  -->  -1.0f
第3个View  1.0f  -->  0.0f
第4个View  2.0f  -->  1.0f

第3个到第2个
第1个View -2.0f  -->  -1.0f
第2个View -1.0f  -->  0.0f
第3个View 0.0f  -->  1.0f
第4个View 1.0f  -->  2.0f

第2个到第1个
第0个View -2.0f  -->  -1.0f
第1个View -1.0f  -->  0.0f
第2个View 0.0f  -->  1.0f
第3个View 1.0f  -->  2.0f

第1个到第0个
第0个View -1.0f  -->  0.0f
第1个View 0.0f  -->  1.0f
第2个View 1.0f  -->  2.0f