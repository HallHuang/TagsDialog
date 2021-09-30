
public class TagsDialog extends Dialog {

    @BindView(R.id.easy_tip_drag_view)
    EasyTipDragView easyTipDragView;

    private OnDismissListener onDismissListener;

    private boolean isDataChannger;
    private int selectPosition=-1;

    public TagsDialog(@NonNull Context context) {
        this(context, R.style.Dialog_Fullscreen);
    }

    public TagsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogBottom); // 添加动画
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tag_select);
        ButterKnife.bind(this, this);
        initDialog();

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

        //删除或排序
        easyTipDragView.setDataResultCallback(new EasyTipDragView.OnDataChangeResultCallback() {
            @Override
            public void onDataChangeResult(ArrayList<Tip> tips) {
                isDataChannger=true;
                L.v( "onDataChangeResult:"+tips.toString());
                SharePreferenceManager.putObject(getContext(),"news_tags",tips);
            }
        });

        //下栏点击添加进上栏
        easyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
            @Override
            public void onComplete(ArrayList<Tip> tips) {
                L.d("onComplete：" + tips.toString());
                isDataChannger=true;
                SharePreferenceManager.putObject(getContext(),"news_tags",tips);
                dismiss();
            }
        });

    }

    //dialog靠右显示，左侧有空床（填充阴影）
    private void initDialog() {
        setTitle(R.string.all_tag);     //Set the title text for this dialog's window.
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(onDismissListener!=null){
            onDismissListener.onDismiss(isDataChannger, selectPosition);
        }
    }

    public  void setOnDismissListener(OnDismissListener listener){
        this.onDismissListener=listener;
    }

    public interface OnDismissListener{
        void onDismiss(boolean isDataChannger,int selectPosition);
    }
}
