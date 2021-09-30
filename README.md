# TagsDialog
顶部导航最右侧标签选择跳窗，可进行拖动排序、删除等

## 1. 文件组成：
  自定义视图：TagsDialog.java;  
  辅助XML: style.xml（主题，name="Dialog_Fullscreen"；进入/离开动画 name="DialogBottom"）, 
          dialog_tag_select.xml
  标签选择视图功能模块: easytagdragview
  
## 2. 基本使用发法：

  在某个Activity/Fragment中，通过点击事件触发TagsDialog以动画形式显示：
  
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
