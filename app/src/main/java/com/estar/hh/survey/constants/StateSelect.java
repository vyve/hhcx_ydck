package com.estar.hh.survey.constants;

import android.content.Context;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/18 0018.
 * 状态选择封装
 */

public class StateSelect {

//    public static List<KeyValue> firstState = new ArrayList<>();

    /**
     * 全部状态初始化
     */
    public static void init(Context context){
        /**
         * 初始化第一个数组
         */
//        firstState.clear();
//        firstState.addAll(initArray(context, R.array.CLLB_value, R.array.CLLB_key));
    }


    /**
     * 通过资源文件键值对组装keyValue
     * @param context
     * @param keyResource
     * @param codeResource
     * @return
     */
    public static List<KeyValue> initArray(Context context, int keyResource, int codeResource){
        String[] firstKey = context.getResources().getStringArray(keyResource);
        String[] firstCode = context.getResources().getStringArray(codeResource);
        int firstLengh = firstKey.length;
        List<KeyValue> states = new ArrayList<>();
        for (int i=0; i < firstLengh; i++){
            KeyValue keyValue = new KeyValue();
            keyValue.setKey(firstKey[i]);
            keyValue.setValue(firstCode[i]);
            states.add(keyValue);
        }
        return states;
    }

    /**
     * 通过资源文件值获取
     * @param code
     * @param keyValues
     * @return
     */
    public static String getTextValue(String code, List<KeyValue> keyValues){
        String value = null;
        if (keyValues != null && keyValues.size() > 0){
            for (KeyValue keyValue : keyValues){
                if (keyValue.getValue().equals(code)){
                    value = keyValue.getKey();
                    break;
                }
            }
        }
        return value;
    }


    /**
     * 通过资源文件值获取
     * @param code
     * @param keyValues
     * @return
     */
    public static String getTextKey(String code, List<KeyValue> keyValues){
        String value = null;
        if (keyValues != null && keyValues.size() > 0){
            for (KeyValue keyValue : keyValues){
                if (keyValue.getKey().equals(code)){
                    value = keyValue.getValue();
                    break;
                }
            }
        }
        return value;
    }


    /**
     * 通过资源文件多值获取
     * @param code
     * @param keyValues
     * @return
     */
    public static String getMultiTextValue(String code, List<KeyValue> keyValues){

        String result = "";
        if (StringUtils.isEmpty(code)){
            return result;
        }

        if (keyValues == null || keyValues.size() == 0){
            return result;
        }

        String[] values = code.split(",");
        List<String> valueList = new ArrayList<>();
        for (int i = 0; i < values.length; i++){
            valueList.add(values[i]);
        }

        if (valueList == null || valueList.size() == 0){
            return result;
        }

        for (String value : valueList){
            String textValueResult = getTextValue(value, keyValues);
            if (!StringUtils.isEmpty(textValueResult)){
                if (StringUtils.isEmpty(result)){
                    result = textValueResult;
                }else {
                    result = result + "," + textValueResult;
                }
            }
        }

        return result;
    }

}
