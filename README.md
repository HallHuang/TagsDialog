# TagsDialog
顶部导航最右侧标签选择跳窗，可进行拖动排序、删除等

![tag11.jpg](http://zuohaotu.com/Download/202183023537872tag2.jpg)      ![tag11.jpg](http://zuohaotu.com/Download/2021830235110203tag11.jpg)![tag11.jpg](http://h9.zuohaotu.com/Download/202183023589674tag3.jpg)

## 1. 文件组成：
<img src="https://i.loli.net/2021/09/30/6QsVg19cnuhxbAE.png" width="400">
自定义视图：TagsDialog.java;   

辅助XML: style.xml（主题，name="Dialog_Fullscreen";进入/离开动画 name="DialogBottom"), dialog_tag_select.xml(主视图);  
标签选择视图功能模块: easytagdragview;  
  
## 2. 基本使用方法：

   (1)在TagsDialog自定义视图中，加载easytagdragview功能包中的自定义视图Easytagdragview作为主视图
   ```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    android:orientation="horizontal">

    <com.XXX.easytagdragview.EasyTipDragView
        android:id="@+id/easy_tip_drag_view"
        android:background="@android:color/white"
        android:layout_width="wrap_content"
        android:layout_gravity="center"
        android:layout_height="match_parent"/>

</LinearLayout>
   ```
   
  (2)在某个Activity/Fragment中，通过点击事件触发TagsDialog以动画形式显示：
  
  ```
  @Override
  public void onClick(View v) {
    switch (v.getId()){
        case R.id.iv_tag_edit:
            TagsDialog tagsDialog=new TagsDialog(getContext());
            tagsDialog.show();
            backView.setVisibility(View.VISIBLE);
            
            //数据和视图刷新;片段的定位跳转
            tagsDialog.setOnDismissListener(new TagsDialog.OnDismissListener() {
                @Override
                public void onDismiss(boolean isDataChannger, int selectPosition) {
                   //
                }
            });
            break;
```
  (3) 在TagsDialog中进行主题标签的数据载入、事件响应处理
  
```
//下部
easyTipDragView.setAddData(TagsManager.getNewsAddTips());
//上部
easyTipDragView.setDragData(TagsManager.getNewsDragTips());

//在easyTipDragView处于非编辑模式下点击item的回调（编辑模式下点击item作用为删除item）
easyTipDragView.setSelectedListener(new TipItemView.OnSelectedListener() {
  @Override
  public void onTileSelected(Tip entity, int position, View view) {
      List<Boolean> mSelected= easyTipDragView.getDragTipAdapter().getmTextSelected();

      //set(position, true) --> Data Changed --> View Changed
          selectPosition=position;
          for (int i=0;i<mSelected.size();i++){
              if (i!=position){
                  easyTipDragView.getDragTipAdapter().getmTextSelected().set(i,false);
              }else{
                  easyTipDragView.getDragTipAdapter().getmTextSelected().set(i,true);
              }
          }
          easyTipDragView.getDragTipAdapter().notifyDataSetChanged();

          dismiss();
      }
  });

  //拖拉标签完成后的数据刷新回调
  easyTipDragView.setDataResultCallback(new EasyTipDragView.OnDataChangeResultCallback() {
      @Override
      public void onDataChangeResult(ArrayList<Tip> tips) {
          isDataChannger=true;
          L.v( "onDataChangeResult:"+tips.toString());
          SharePreferenceManager.putObject(getContext(),"news_tags",tips);
      }
  });

  //点击"完成"按钮对修改后的数据进行传递，并关闭该dialog
  easyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
      @Override
      public void onComplete(ArrayList<Tip> tips) {
          L.d("onComplete：" + tips.toString());
          isDataChannger=true;
          SharePreferenceManager.putObject(getContext(),"news_tags",tips);
          dismiss();
      }
  });
```
