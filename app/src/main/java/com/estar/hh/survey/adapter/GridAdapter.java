package com.estar.hh.survey.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.adapter.BaseRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.vo.ImageVO;
import com.estar.hh.survey.utils.BaseSpinner;
import com.rey.material.widget.CheckBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 *
 *图片列表适配器
 */
public class GridAdapter extends BaseRecyclerViewAdapter<ImageVO, GridAdapter.MyViewHolder> {

    private List<ImageVO> ll = new ArrayList<ImageVO>();
    private Context context2;
    private  BaseSpinner bs;

    public GridAdapter(Context context, List<ImageVO> list) {
        super(list);
        this.context2=context;
        bs = new BaseSpinner(context);

    }

    public void clean(List<ImageVO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void changePosition(List<ImageVO> list,int position){
        this.mList=list;
        //position数据发生了改变
        notifyItemChanged(position);
    }
    /**
     * 获取选中的图片列表
     *
     * @return
     */
    public List<ImageVO> getCheckList() {
        ll.clear();
        for (ImageVO imageVO : mList) {
            if (1 == imageVO.getChecked()) {
                ll.add(imageVO);
            }
        }
        return ll;
    }



    /**
     * 更新正在上传状态
     */
    public void cleanStatus2() {
        int i = 0;
        for (ImageVO imageVO : mList) {

            if (1 == imageVO.getChecked()) {//原来被选中
                mList.get(i).setUpcliamflag(1);//改成正在上传状态
                mList.get(i).setChecked(0);//改成未选中状态
                notifyItemChanged(i);
            }
            ++i;
        }
    }

    @Override
    protected void bindDataToItemView(final MyViewHolder myViewHolder, final ImageVO item, final int position) {
        myViewHolder.setText(R.id.name_tv, item.getMark());
        Glide.with(context2)
                .load(new File(item.getImagePath()))
//					.placeholder(com.yongchun.library.R.mipmap.ic_placeholder)//设置加载时的照片
//					.error(com.yongchun.library.R.mipmap.ic_placeholder)//设置加载错误时的照片
//                  .centerCrop()
                .into((ImageView) myViewHolder.getView(R.id.id_item_image));
        if(item.getPicCls().equals("2")){
            myViewHolder.setText(R.id.picCls_picDtl, bs.getSpinnerVlaueByCode(R.array.documents_Dls_Value, R.array.documents_Dls_Code,item.getPicCls())+"     "+bs.getSpinnerVlaueByCode(R.array.documents_cls1_Value, R.array.documents_cls1_Code,item.getPicDtl()));
        }else if(item.getPicCls().equals("3")){
            myViewHolder.setText(R.id.picCls_picDtl, bs.getSpinnerVlaueByCode(R.array.documents_Dls_Value, R.array.documents_Dls_Code,item.getPicCls())+"     "+bs.getSpinnerVlaueByCode(R.array.documents_cls2_Value, R.array.documents_cls2_Code,item.getPicDtl()));
        }else{
            myViewHolder.setText(R.id.picCls_picDtl, bs.getSpinnerVlaueByCode(R.array.documents_Dls_Value, R.array.documents_Dls_Code,item.getPicCls())+"     "+bs.getSpinnerVlaueByCode(R.array.documents_cls1_Value, R.array.documents_cls1_Code,item.getPicDtl()));
        }
        if (0 == item.getUpcliamflag()) {// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败  每张图片最多上传3次
            myViewHolder.setText(R.id.tv, "未上传");
            myViewHolder.setTextColor(R.id.tv, context.getResources().getColor(R.color.red));
            myViewHolder.setVisible(R.id.cb, true);
            ((CheckBox)myViewHolder.getView(R.id.cb)).setChecked(false);
            myViewHolder.setColorFilter(R.id.id_item_image, Color.parseColor("#00000000"));
        } else if (2 == item.getUpcliamflag()) {
            myViewHolder.setText(R.id.tv, "已上传");
            myViewHolder.setTextColor(R.id.tv, context.getResources().getColor(R.color.blue));
            item.setChecked(0);
            myViewHolder.setVisible(R.id.cb, false);
        } else if (1 == item.getUpcliamflag()) {
            item.setChecked(0);
            myViewHolder.setVisible(R.id.cb, false);
            myViewHolder.setText(R.id.tv, "正在上传");
            myViewHolder.setTextColor(R.id.tv, context.getResources().getColor(R.color.green));
        } else if (3 == item.getUpcliamflag()) {
            myViewHolder.setVisible(R.id.cb, true);
            myViewHolder.setText(R.id.tv, item.getMsg());
            myViewHolder.setTextColor(R.id.tv, context.getResources().getColor(R.color.red));
            ((CheckBox)myViewHolder.getView(R.id.cb)).setChecked(false);
            myViewHolder.setColorFilter(R.id.id_item_image, Color.parseColor("#00000000"));
        }
        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (0 == item.getChecked()) {
            ((CheckBox)myViewHolder.getView(R.id.cb)).setChecked(false);
            myViewHolder.setColorFilter(R.id.id_item_image, Color.parseColor("#00000000"));
        } else {
            ((CheckBox)myViewHolder.getView(R.id.cb)).setChecked(true);
            myViewHolder.setColorFilter(R.id.id_item_image, Color.parseColor("#77000000"));
        }

        ((CheckBox)myViewHolder.getView(R.id.cb)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    item.setChecked(1);
                    myViewHolder.setColorFilter(R.id.id_item_image, Color.parseColor("#77000000"));
                } else {
                    item.setChecked(0);
                    myViewHolder.setColorFilter(R.id.id_item_image, Color.parseColor("#00000000"));
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int t) {
        return new MyViewHolder(inflateItemView(viewGroup, R.layout.adapter_grid_item));

    }


    public class MyViewHolder extends BaseRecyclerViewAdapter.SparseArrayViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }


    }
}