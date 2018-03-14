# StarFloatView
### 在工程目录的build.gradle中添加JitPack.io的代码仓库地址

    allprojects {
      repositories {
        ...
        maven { url 'https://jitpack.io' }
      }
    }
    
### 项目目录中的build.gradle中添加依赖

    dependencies {
            compile 'com.github.ErChenZhang:StarFloatView:v1.0'
    }
    
## 使用方法：
  ### 在布局文件中使用
    
         <com.smoke.zhangchen.floatviewlib.FloatView
            android:id="@+id/float_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content
            app:childTextColor="#ff0000"
            app:defaultViewText="正在生成中"
            app:parentViewBackground="@mipmap/star_bg"
            app:childViewBackground="@drawable/shape_circle">
            
1. **childTextColor**:小球文字颜色大小
2. **defaultViewText**:默认小球显示文字
3. **parentViewBackground**：整体背景
4. **childViewBackground**：小球背景
        
   ### 设置数据
    
         List<Float> list = new ArrayList<>();
            list.add((float) 1.245);
            list.add((float) 1.567);
            list.add((float) 0.261);
            list.add((float) 2.455);
            list.add((float) 4.643);
            list.add((float) 3.211);
            list.add((float) 1.425);
            list.add((float) 1.043);
            list.add((float) 0.221);
            list.add((float) 0.455);
            floatview.setList(list);
            
  ### 小球点击回调
     
           floatview.setOnItemClickListener(new FloatView.OnItemClickListener() {
               @Override
               public void itemClick(int position, float value) {
                   Toast.makeText(MainActivity.this, "当前是第"+position+"个，其值是"+value, Toast.LENGTH_SHORT).show();
               }
           });
            