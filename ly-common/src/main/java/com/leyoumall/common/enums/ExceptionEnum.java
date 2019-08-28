package com.leyoumall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
相当于先创建出了对象，等你拿来用就行了
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnum {
    CATEGORY_NOT_FOUND(404,"商品分类没有查到"),
    BRAND_NOT_FOUND(404,"品牌没有查到"),
    BRAND_SAVA_ERROR(500,"新增品牌失败"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    INVALID_FILE_TYPE(500,"无效文件类型"),
    SPECGROUP_NOT_FOUND(404,"规格组没有查到"),
    SPECPARAM_NOT_FOUND(404,"规格参数没有查到"),
    SPECGROUP_SAVA_ERROR(500,"新增规格组失败"),
    SPECPARAM_SAVA_ERROR(500,"新增规格参数失败"),
    SPU_NOT_FOUND(500,"商品没有查到"),
    GOODS_SAVE_FAIL(500,"商品保存失败"),
    SPUDETAIL_NOT_FOUND(404,"商品明细没有查到"),
    SKU_NOT_FOUND(404,"商品没有查到"),
    GOODS_STOCK_NOT_FOUND(404,"商品库存没有查到"),
    SKU_DELETE_FAIL(404,"SKU删除失败"),
    UPDATE_SPU_FAIL(404,"SPU更新失败"),
    SPUID_ERROR(404,"SPUID错误"),
    USER_DATA_INVOID(404,"用户校验数据类型错误"),
    CODE_INVOID(404,"无效验证码"),
    USERNAME_PASSWORD_INVOID(404,"无效用户名或密码"),
    TOKEN_FAIL(404,"TOKEN生成失败"),
    CART_NOT_FOUND(404,"购物车不存在"),
    ORDER_CREATE_FAIL(404,"订单创建失败"),
    ;
    private int code;
    private String msg;

}
